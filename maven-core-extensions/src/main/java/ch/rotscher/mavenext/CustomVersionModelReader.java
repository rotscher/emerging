package ch.rotscher.mavenext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.maven.MavenExecutionException;
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
 * <code>-Dmavenext.release.version=A-VERSION</code><br />
 * <br />
 * other options are:<br />
 * <code>-Dmavenext.check.snapshot-dep.failOnError=[true | false]</code><br />
 * <code>-Dmavenext.check.snapshot-dep=[true | false]</code><br />
 * </p>
 * <p>
 * This class helps releasing a project (from a maven point of view, that means no having "SNAPSHOT" in the version) without actually changing (and committing)
 * the pom.xml
 * </p>
 */
@Component(role = ModelReader.class, hint = "custom-version-model-reader")
public class CustomVersionModelReader extends DefaultModelReader implements ModelReader {

    static final String MAVENEXT_RELEASE_VERSION = "mavenext.release.version";
    static final String MAVENEXT_CHECK_SNAPSHOT_DEP_FAIL_ON_ERROR = "mavenext.check.snapshot-dep.failOnError";
    static final String MAVENEXT_CHECK_SNAPSHOT_DEP = "mavenext.check.snapshot-dep";

    @Requirement
    private Logger logger;

    @Override
    public Model read(InputStream input, Map<String, ?> options) throws IOException {
        Model model = super.read(input, options);
        // this property is set from the command line with -Drelease.version
        String version = System.getProperty(MAVENEXT_RELEASE_VERSION);
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

        Boolean checkSnapshotDependencies = Boolean.parseBoolean(System.getProperty(MAVENEXT_CHECK_SNAPSHOT_DEP));
        Boolean failOnError = Boolean.parseBoolean(System.getProperty(MAVENEXT_CHECK_SNAPSHOT_DEP_FAIL_ON_ERROR));

        if (checkSnapshotDependencies == Boolean.TRUE) {
            for (Dependency dep : model.getDependencies()) {
                String depVersion = dep.getVersion();
                if (depVersion != null && depVersion.contains("SNAPSHOT")) {
                    File pomFile = model.getPomFile();
                    String pomFilePath = "[path not available]";
                    if (pomFile != null) {
                        pomFilePath = pomFile.getPath();
                    }
                    String errorMsg = String.format("there is a snapshot dependency (%s) in %s (%s)", dep, model, pomFilePath);
                    logger.error(errorMsg);
                    if (failOnError == Boolean.TRUE) {
                        throw new IOException(new MavenExecutionException(errorMsg, model.getPomFile()));
                    }
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
        logger.warn(String.format(
                        "no groupId found for model %s (no clue why this happens :-(. But has no negative affect :-) Maybe this is the super pom!!??", model));
        return "n/A";
    }

    /**
     * set by the unit test.
     * 
     * @param logger
     *            the logger
     */
    void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * helper class to store the very first groupid which is the base groupid for all following modules:
     * 
     * the modules version is changed if the test <code>if module.groupId starts with GroupIdOfRootPom.groupId</code> is true
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
