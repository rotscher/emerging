package ch.rotscher.flyway.core;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.resolver.MigrationResolver;
import org.flywaydb.core.dbsupport.DbSupport;
import org.flywaydb.core.dbsupport.DbSupportFactory;
import org.flywaydb.core.util.Location;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by rotscher on 13.03.14.
 */
public class RepeatableFlyway {

    private final Flyway flyway;
    private final Properties properties;
    private final DbSupport dbSupport;
    private String tmpFlywayTable;

    public RepeatableFlyway(Flyway flyway, Properties properties) throws SQLException {
        this.flyway = flyway;
        this.properties = properties;
        dbSupport = DbSupportFactory.createDbSupport(flyway.getDataSource().getConnection(), false);
    }

    public void preMigrate() throws SQLException {
        String sql = "DELETE FROM " + flyway.getTable()
                + " WHERE " + DbSupportFactory.createDbSupport(flyway.getDataSource().getConnection(), false).quote("type") + "='CUSTOM'";

        flyway.setDataSource(properties.getProperty("flyway.url"), properties.getProperty("flyway.user"), properties.getProperty("flyway.password"), sql);

        DbSupport dbSupport = DbSupportFactory.createDbSupport(flyway.getDataSource().getConnection(), false);
        List<MigrationResolver> migrationResolvers = new ArrayList<>();
        for (String locationDescriptor : flyway.getLocations()) {
            migrationResolvers.add(new RepeatableMigrationResolver(flyway, "R", new Location(locationDescriptor), dbSupport));
        }

        flyway.setCustomMigrationResolvers(migrationResolvers.toArray(new MigrationResolver[0]));
    }

    public void preInfo() throws SQLException {
        tmpFlywayTable = flyway.getTable() + "_";
        if (dbSupport.getCurrentSchema().getTable(tmpFlywayTable).exists()) {
            dbSupport.getJdbcTemplate().executeStatement("DROP TABLE " + flyway.getTable() + "_");
        }
        String sql = "CREATE TABLE " + tmpFlywayTable + " AS SELECT * FROM " + flyway.getTable() +
                " WHERE " + DbSupportFactory.createDbSupport(flyway.getDataSource().getConnection(), false).quote("type") + "='CUSTOM'";
        dbSupport.getJdbcTemplate().executeStatement(sql);

        sql = "DELETE FROM " + flyway.getTable()
                + " WHERE " + DbSupportFactory.createDbSupport(flyway.getDataSource().getConnection(), false).quote("type") + "='CUSTOM'";
        flyway.setDataSource(properties.getProperty("flyway.url"), properties.getProperty("flyway.user"), properties.getProperty("flyway.password"), sql);

        List<MigrationResolver> migrationResolvers = new ArrayList<>();
        for (String locationDescriptor : flyway.getLocations()) {
            migrationResolvers.add(new RepeatableMigrationResolver(flyway, "R", new Location(locationDescriptor), dbSupport));
        }

        flyway.setCustomMigrationResolvers(migrationResolvers.toArray(new MigrationResolver[0]));
    }

    public void postInfo() throws SQLException {
        dbSupport.getJdbcTemplate().executeStatement("INSERT INTO " + flyway.getTable() + " SELECT * FROM " + tmpFlywayTable);
        //dbSupport.getJdbcTemplate().executeStatement("DROP TABLE " + flyway.getTable() + "_");
    }

}
