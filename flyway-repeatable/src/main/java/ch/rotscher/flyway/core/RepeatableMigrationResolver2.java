package ch.rotscher.flyway.core;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationType;
import org.flywaydb.core.api.resolver.MigrationResolver;
import org.flywaydb.core.api.resolver.ResolvedMigration;
import org.flywaydb.core.dbsupport.DbSupport;
import org.flywaydb.core.resolver.sql.SqlMigrationResolver;
import org.flywaydb.core.util.Location;
import org.flywaydb.core.util.PlaceholderReplacer;

import java.util.Collection;

/**
 * Created by rotscher on 01.03.14.
 */
public class RepeatableMigrationResolver2 implements MigrationResolver {

    private Flyway flyway;
    private final String prefix;
    private Location location;
    private DbSupport dbSupport;

    public RepeatableMigrationResolver2(Flyway flyway, String prefix, Location location, DbSupport dbSupport) {
        this.prefix = prefix;
        this.location = location;
        this.flyway = flyway;
        this.dbSupport = dbSupport;
    }


    @Override
    public Collection<ResolvedMigration> resolveMigrations() {

        SqlMigrationResolver delegate = new SqlMigrationResolver(getDbSupport(), null, getLocation(), getPlaceholderReplacer(), getEncoding(), "R", getSqlMigrationSuffix()) {
            protected MigrationType getMigrationType() {
                return MigrationType.CUSTOM;
            }
        };
        return delegate.resolveMigrations();

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

    public DbSupport getDbSupport() {
        return dbSupport;
    }
}