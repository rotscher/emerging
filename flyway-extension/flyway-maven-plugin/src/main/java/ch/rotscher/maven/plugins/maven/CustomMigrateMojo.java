package ch.rotscher.maven.plugins.maven;

import ch.rotscher.flyway.core.RepeatableMigrationResolver;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.resolver.MigrationResolver;
import org.flywaydb.core.dbsupport.DbSupport;
import org.flywaydb.core.dbsupport.DbSupportFactory;
import org.flywaydb.core.util.Location;
import org.flywaydb.core.util.PlaceholderReplacer;
import org.flywaydb.maven.MigrateMojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rotscher on 01.03.14.
 * @extendsPlugin flyway-maven-plugin
 * @extendsGoal migrate
* @goal custom-migrate
 */
public class CustomMigrateMojo extends MigrateMojo {

    @Override
    protected void doExecute(Flyway flyway) throws Exception {

        List<MigrationResolver> migrationResolvers = new ArrayList<>();
        for (String locationDescriptor : flyway.getLocations()) {
            migrationResolvers.add(new RepeatableMigrationResolver(flyway, "R", new Location(locationDescriptor)));
        }

        flyway.setCustomMigrationResolvers(migrationResolvers.toArray(new MigrationResolver[0]));
        super.doExecute(flyway);
    }
}
