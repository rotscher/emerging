package ch.rotscher.flyway.core;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.MigrationType;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.resolver.MigrationExecutor;
import org.flywaydb.core.api.resolver.MigrationResolver;
import org.flywaydb.core.api.resolver.ResolvedMigration;
import org.flywaydb.core.dbsupport.DbSupport;
import org.flywaydb.core.dbsupport.DbSupportFactory;
import org.flywaydb.core.resolver.MigrationInfoHelper;
import org.flywaydb.core.resolver.sql.SqlMigrationExecutor;
import org.flywaydb.core.util.Location;
import org.flywaydb.core.util.Pair;
import org.flywaydb.core.util.PlaceholderReplacer;
import org.flywaydb.core.util.Resource;
import org.flywaydb.core.util.scanner.classpath.ClassPathScanner;
import org.flywaydb.core.util.scanner.filesystem.FileSystemScanner;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.zip.CRC32;

/**
 * Created by rotscher on 01.03.14.
 */
public class RepeatableMigrationResolver implements MigrationResolver {

    private Flyway flyway;
    private final String prefix;
    private Location location;

    public RepeatableMigrationResolver(Flyway flyway, String prefix, Location location) {
        this.prefix = prefix;
        this.location = location;
        this.flyway = flyway;
    }

    @Override
    public Collection<ResolvedMigration> resolveMigrations() {
        List<ResolvedMigration> migrations = new ArrayList<ResolvedMigration>();

        Resource[] resources = new Resource[0];
        try {
            if (getLocation().isClassPath()) {
                resources =
                        new ClassPathScanner(null).scanForResources(getLocation().getPath(), getPrefix(), getSqlMigrationSuffix());
            } else if (getLocation().isFileSystem()) {
                resources =
                        new FileSystemScanner().scanForResources(getLocation().getPath(), getPrefix(), getSqlMigrationSuffix());
            }

            for (Resource resource : resources) {
                ResolvedMigration resolvedMigration = extractMigrationInfo(resource);
                migrations.add(resolvedMigration);
            }
        } catch (IOException e) {
            throw new FlywayException("Unable to scan for SQL migrations with prefix " + prefix + " in location: " + getLocation(), e);
        }

        //Collections.sort(migrations);
        return migrations;
    }

    /**
     * Extracts the migration info for this resource.
     *
     * @param resource The resource to analyse.
     * @return The migration info.
     */
    private ResolvedMigration extractMigrationInfo(final Resource resource) {

        final Pair<MigrationVersion, String> info =
                MigrationInfoHelper.extractVersionAndDescription(resource.getFilename(), getSqlMigrationPrefix(), getSqlMigrationSuffix());

        final File file = new File(resource.getLocationOnDisk());

        ResolvedMigration migration = new ResolvedMigration() {

            @Override
            public MigrationVersion getVersion() {
                return MigrationVersion.fromVersion(info.getLeft().toString() + "_" + file.lastModified());
                //return info.getLeft();
            }

            @Override
            public String getDescription() {
                return info.getRight();
            }

            @Override
            public String getScript() {
                return extractScriptName(resource);
            }

            @Override
            public Integer getChecksum() {
                return calculateChecksum(resource.loadAsBytes());
            }

            @Override
            public MigrationType getType() {
                return MigrationType.CUSTOM;
            }

            @Override
            public String getPhysicalLocation() {
                return resource.getLocationOnDisk();
            }

            @Override
            public MigrationExecutor getExecutor() {
                PlaceholderReplacer placeholderReplacer =getPlaceholderReplacer();
                try {
                    return new SqlMigrationExecutor(getDbSupport(), resource, placeholderReplacer, getEncoding());
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };

        return migration;
    }

    /**
     * Extracts the script name from this resource.
     *
     * @param resource The resource to process.
     * @return The script name.
     */
    /* private -> for testing */ String extractScriptName(Resource resource) {
        int start = 0;

        if (getLocation().getPath().length() > 0) {
            start = getLocation().getPath().length() + "/".length();
        }

        return resource.getLocation().substring(start);
    }

    /**
     * Calculates the checksum of these bytes.
     *
     * @param bytes The bytes to calculate the checksum for.
     * @return The crc-32 checksum of the bytes.
     */
    private static int calculateChecksum(byte[] bytes) {
        final CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        return (int) crc32.getValue();
    }


    public String getPrefix() {
        return prefix;
    }

    public String getSqlMigrationSuffix() {
        return flyway.getSqlMigrationSuffix();
    }

    public Location getLocation() {
        return location;
    }

    public String getSqlMigrationPrefix() {
        return flyway.getSqlMigrationPrefix();
    }

    public String getEncoding() {
        return flyway.getEncoding();
    }

    public PlaceholderReplacer getPlaceholderReplacer() {
        return new PlaceholderReplacer(flyway.getPlaceholders(), flyway.getPlaceholderPrefix(), flyway.getPlaceholderSuffix());
    }

    public DbSupport getDbSupport() throws SQLException {
        return DbSupportFactory.createDbSupport(flyway.getDataSource().getConnection(), false);
    }
}
