package ch.rotscher.maven.plugins.maven;

import ch.rotscher.flyway.core.RepeatableMigrationResolver;
import ch.rotscher.flyway.core.RepeatableMigrationResolver2;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.resolver.MigrationResolver;
import org.flywaydb.core.dbsupport.DbSupport;
import org.flywaydb.core.dbsupport.DbSupportFactory;
import org.flywaydb.core.util.Location;
import org.flywaydb.core.util.jdbc.DriverDataSource;
import org.flywaydb.maven.MigrateMojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rotscher on 01.03.14.
 * @extendsPlugin flyway-maven-plugin
 * @extendsGoal migrate
 * @goal migrate-custom
 */
public class CustomMigrateMojo2 extends MigrateMojo {

    //maven property inheritance?

    @Override
    protected void doExecute(Flyway flyway) throws Exception {

        String sql = "DELETE FROM " + flyway.getTable()
                    + " WHERE " + DbSupportFactory.createDbSupport(flyway.getDataSource().getConnection(), false).quote("type") + "='CUSTOM'";
        System.out.println("huh: " + getProperty("url"));
        flyway.setDataSource(getProperty("url"), getProperty("user"), getProperty("password"), sql);

        DbSupport dbSupport = DbSupportFactory.createDbSupport(flyway.getDataSource().getConnection(), false);
        List<MigrationResolver> migrationResolvers = new ArrayList<>();
        for (String locationDescriptor : flyway.getLocations()) {
            migrationResolvers.add(new RepeatableMigrationResolver2(flyway, "R", new Location(locationDescriptor), dbSupport));
        }

        flyway.setCustomMigrationResolvers(migrationResolvers.toArray(new MigrationResolver[0]));
        super.doExecute(flyway);
    }
}
