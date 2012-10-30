package ch.rotscher.mavenext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.DefaultModelReader;
import org.apache.maven.model.io.ModelReader;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

/**
 * <p>
 * extension of {@link org.apache.maven.model.io.ModelReader} for changing ${project.version} from the pom.xml with the value given as a Java system property:
 * </p>
 * <p>
 * -Drelease.version=A-VERSION
 * </p>
 * <p>
 * This class helps releasing a project (from a maven point of view, that means no having "SNAPSHOT" in the version) without actually changing (and committing)
 * the pom.xml
 * </p>
 */
@Component(role = ModelReader.class, hint = "custom-version-model-reader")
public class CustomVersionModelReader extends DefaultModelReader implements ModelReader {

    @Requirement
    private Logger logger;

    @Override
    public Model read(InputStream input, Map<String, ?> options) throws IOException {
        Model model = super.read(input, options);
        // this property is set from the command line with -Drelease.version
        String version = System.getProperty("release.version");
        if (version == null || version.trim().length() < 1) {
            return model;
        }

        String currentGroupId = getGroupId(model);
        GroupIdOfRootPom groupId = GroupIdOfRootPom.getInstance(currentGroupId, logger);

        if (currentGroupId.startsWith(groupId.groupId)) {
            logger.info(String.format("changing version of  %s  to %s", model, version));
            model.setVersion(version);
            Parent parent = model.getParent();
            if (parent != null && parent.getGroupId().startsWith(groupId.groupId)) {
                logger.info(String.format("changing version of parent  %s  to %s", parent, version));
                parent.setVersion(version);
            }
        }

        // TODO test and document this
        String snapshotCheck = System.getProperty("release.dependency.check.snapshot");
        if (snapshotCheck != null && snapshotCheck.trim().equalsIgnoreCase("true")) {
            for (Dependency dep : model.getDependencies()) {
                String depVersion = dep.getVersion();
                if (depVersion != null && depVersion.contains("SNAPSHOT")) {
                    String errorMsg = String.format("there is a snapshot dependency (%s) in %s", dep, model.getPomFile().getPath());
                    logger.error(errorMsg);
                    throw new IllegalArgumentException(errorMsg);
                }
            }
        }

        return model;
    }

    /**
     * get the groupId of the given model. if not set then ask the parent
     * 
     * @param model
     * @return the groupId of the given module
     */
    private String getGroupId(Model model) {
        if (model.getGroupId() != null) {
            return model.getGroupId();
        }

        if (model.getParent() != null) {
            return model.getParent().getGroupId();
        }

        // TODO: return null if no groupId can be found!
        logger.warn(String.format("no groupId found for model %s (no clue why this happens :-(. But has no negative affect :-) Maybe this is the super pom!!??", model));
        return "n/A";
    }

    /**
     * accessed by the unit test
     * 
     * @param logger
     *            the logger
     */
    void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * 
     * @author rotscher
     * 
     */
    static class GroupIdOfRootPom {
        private static GroupIdOfRootPom INSTANCE;

        private final String groupId;

        private GroupIdOfRootPom(String groupId) {
            this.groupId = groupId;
        }

        public static GroupIdOfRootPom getInstance(String groupId, Logger logger) {
            if (INSTANCE == null) {
                INSTANCE = new GroupIdOfRootPom(groupId);
                logger.info(String.format("initialize groupId to '%s', the version of all modules starting with this groupdId will be changed!", groupId));
            }

            return INSTANCE;

        }
    }

}
