package ch.rotscher.maven.plugins.maven;

import ch.rotscher.flyway.core.RepeatableFlyway;
import org.flywaydb.core.Flyway;
import org.flywaydb.maven.InfoMojo;
import org.flywaydb.maven.ValidateMojo;

import java.util.Properties;

/**
 * Created by rotscher on 01.03.14.
 * @extendsPlugin flyway-maven-plugin
 * @extendsGoal validate
 * @goal validate
 */
public class RepeatableValidateMojo extends ValidateMojo {

    @Override
    protected void doExecute(Flyway flyway) throws Exception {


        Properties props = new Properties();
        props.put("flyway.url", getProperty("url"));
        props.put("flyway.user", getProperty("user"));
        props.put("flyway.password", getProperty("password"));

        RepeatableFlyway repeatableFlyway = new RepeatableFlyway(flyway, props);
        repeatableFlyway.preInfo();
        super.doExecute(flyway);
        repeatableFlyway.postInfo();

    }
}
