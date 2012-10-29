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
        // this property is set from the command line with -Drelease.version
        String version = System.getProperty("release.version");
        if (version == null || version.trim().length() < 1) {
            return super.read(input, options);
        }

        Model model = super.read(input, options);
        logger.info(String.format("current module: %s%n", model));
        String currentGroupId = getGroupId(model);
        GroupIdOfRootPom groupId = GroupIdOfRootPom.getInstance(currentGroupId);

        if (currentGroupId.startsWith(groupId.groupId)) {
            logger.info(String.format("  changing version of: %s:%s:%s%n", model.getGroupId(), model.getArtifactId(), model.getVersion()));
            model.setVersion(version);
            Parent parent = model.getParent();
            if (parent != null && parent.getGroupId().startsWith(groupId.groupId)) {
                logger.debug(String.format("  %s:%s:%s (%s)%n", parent.getGroupId(), parent.getArtifactId(), parent.getVersion(), parent));
                parent.setVersion(version);
            }
        }

        // TODO test and document this
        String snapshotCheck = System.getProperty("release.dependency.check.snapshot");
        if (snapshotCheck != null && snapshotCheck.trim().equalsIgnoreCase("true")) {
            for (Dependency dep : model.getDependencies()) {
                String depVersion = dep.getVersion();
                if (depVersion != null && depVersion.contains("SNAPSHOT")) {
                    String errorMsg = String.format("there is a snapshot dependency (%s) in %s%n", dep, model.getPomFile().getPath());
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

        public static GroupIdOfRootPom getInstance(String groupId) {
            if (INSTANCE == null) {
                INSTANCE = new GroupIdOfRootPom(groupId);
            }

            return INSTANCE;

        }
    }

}
