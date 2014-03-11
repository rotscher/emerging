package ch.rotscher.maven.plugins.maven;

import ch.rotscher.flyway.core.RepeatableMigrationResolver2;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfoService;
import org.flywaydb.core.api.resolver.MigrationResolver;
import org.flywaydb.core.dbsupport.DbSupport;
import org.flywaydb.core.dbsupport.DbSupportFactory;
import org.flywaydb.core.info.MigrationInfoDumper;
import org.flywaydb.core.util.Location;
import org.flywaydb.maven.InfoMojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rotscher on 01.03.14.
 * @extendsPlugin flyway-maven-plugin
 * @extendsGoal info
 * @goal info
 */
public class RepeatableInfoMojo extends InfoMojo {

    @Override
    protected void doExecute(Flyway flyway) throws Exception {

        String tmpFlywayTable =  flyway.getTable() + "_";
        DbSupport dbSupport = DbSupportFactory.createDbSupport(flyway.getDataSource().getConnection(), false);
        if (dbSupport.getCurrentSchema().getTable(tmpFlywayTable).exists()) {
            dbSupport.getJdbcTemplate().executeStatement("DROP TABLE " + flyway.getTable() + "_");
        }
        String sql = "CREATE TABLE " + tmpFlywayTable + " AS SELECT * FROM " + flyway.getTable() +
                " WHERE " + DbSupportFactory.createDbSupport(flyway.getDataSource().getConnection(), false).quote("type") + "='CUSTOM'";
        dbSupport.getJdbcTemplate().executeStatement(sql);

        sql = "DELETE FROM " + flyway.getTable()
                + " WHERE " + DbSupportFactory.createDbSupport(flyway.getDataSource().getConnection(), false).quote("type") + "='CUSTOM'";
        System.out.println("huh: " + getProperty("url"));
        flyway.setDataSource(getProperty("url"), getProperty("user"), getProperty("password"), sql);

        List<MigrationResolver> migrationResolvers = new ArrayList<>();
        for (String locationDescriptor : flyway.getLocations()) {
            migrationResolvers.add(new RepeatableMigrationResolver2(flyway, "R", new Location(locationDescriptor), dbSupport));
        }

        flyway.setCustomMigrationResolvers(migrationResolvers.toArray(new MigrationResolver[0]));
        super.doExecute(flyway);
        dbSupport.getJdbcTemplate().executeStatement("INSERT INTO " + flyway.getTable() + " SELECT * FROM " + tmpFlywayTable);
        //dbSupport.getJdbcTemplate().executeStatement("DROP TABLE " + flyway.getTable() + "_");
    }
}
