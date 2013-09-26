package ch.rotscher.mavenext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.maven.MavenExecutionException;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class VersionOverrideModelReaderTest {

    @Before
    public void before() {
        System.clearProperty(VersionOverrideModelReader.MAVENEXT_RELEASE_VERSION);
        System.clearProperty(VersionOverrideModelReader.MAVENEXT_CHECK_SNAPSHOT_DEP);
        System.clearProperty(VersionOverrideModelReader.MAVENEXT_CHECK_SNAPSHOT_DEP_FAIL_ON_ERROR);
    }

    @Test
    public void testValidSinglePom() {

        InputStream correctPomFile = VersionOverrideModelReaderTest.class.getResourceAsStream("valid/single/pom.xml");

        VersionOverrideModelReader customVersionModelReader = new VersionOverrideModelReader();
        customVersionModelReader.setLogger(new ConsoleLogger());
        try {
            Model model = customVersionModelReader.read(correctPomFile, new HashMap<String, Object>());
            assertNotNull(model);
            assertEquals("0.0.1-SNAPSHOT", model.getVersion());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFailingSnapshotDep() {

        System.setProperty(VersionOverrideModelReader.MAVENEXT_RELEASE_VERSION, "1.2.3");
        System.setProperty(VersionOverrideModelReader.MAVENEXT_CHECK_SNAPSHOT_DEP, "TrUe");
        System.setProperty(VersionOverrideModelReader.MAVENEXT_CHECK_SNAPSHOT_DEP_FAIL_ON_ERROR, "TrUe");
        InputStream correctPomFile = VersionOverrideModelReaderTest.class.getResourceAsStream("valid/single/pom.xml");

        VersionOverrideModelReader customVersionModelReader = new VersionOverrideModelReader();
        customVersionModelReader.setLogger(new ConsoleLogger());
        try {
            customVersionModelReader.read(correctPomFile, new HashMap<String, Object>());
            fail("this test should fail with a illegal argument exception");
        } catch (IOException e) {
            if (e.getCause() instanceof MavenExecutionException) {
                assertTrue(e.getCause().getMessage().startsWith("there is a snapshot dependency"));
            } else {
                fail("expected MavenExecutionException");
            }
        }
    }

    @Test
    public void testValidMultiModulePom() {

        InputStream correctPomFile = VersionOverrideModelReaderTest.class.getResourceAsStream("valid/multi/modules/pom.xml");

        VersionOverrideModelReader customVersionModelReader = new VersionOverrideModelReader();
        customVersionModelReader.setLogger(new ConsoleLogger());
        try {
            Model model = customVersionModelReader.read(correctPomFile, new HashMap<String, Object>());
            assertNotNull(model);
            assertEquals("0.0.1-SNAPSHOT", model.getParent().getVersion());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testValidMultiModulePomWithOverriddenVersion() {

        System.setProperty(VersionOverrideModelReader.MAVENEXT_RELEASE_VERSION, "1.2.3");
        InputStream correctPomFile = VersionOverrideModelReaderTest.class.getResourceAsStream("valid/multi/modules/project2/pom.xml");

        VersionOverrideModelReader customVersionModelReader = new VersionOverrideModelReader();
        customVersionModelReader.setLogger(new ConsoleLogger());
        try {
            Model model = customVersionModelReader.read(correctPomFile, new HashMap<String, Object>());
            assertNotNull(model);
            assertEquals("1.2.3", model.getParent().getVersion());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNonFailingSnapshotDep() {
        System.setProperty(VersionOverrideModelReader.MAVENEXT_RELEASE_VERSION, "1.2.3");
        System.setProperty(VersionOverrideModelReader.MAVENEXT_CHECK_SNAPSHOT_DEP, "TrUe");
        // CustomVersionModelReader.MAVENEXT_CHECK_SNAPSHOT_DEP_FAIL_ON_ERROR, is false by default!!
        InputStream correctPomFile = VersionOverrideModelReaderTest.class.getResourceAsStream("valid/single/pom.xml");

        VersionOverrideModelReader customVersionModelReader = new VersionOverrideModelReader();
        customVersionModelReader.setLogger(new ConsoleLogger());
        try {
            Model model = customVersionModelReader.read(correctPomFile, new HashMap<String, Object>());
            assertNotNull(model);
            assertEquals("1.2.3", model.getVersion());
            assertTrue(hasAtLeastOneSnapshotDependency(model.getDependencies()));
        } catch (IOException e) {
            fail("no exception should occur");
        }
    }

    @Test
    public void testNoSnapshotDep() {
        System.setProperty(VersionOverrideModelReader.MAVENEXT_RELEASE_VERSION, "1.2.3");
        System.setProperty(VersionOverrideModelReader.MAVENEXT_CHECK_SNAPSHOT_DEP, "TrUe");
        System.setProperty(VersionOverrideModelReader.MAVENEXT_CHECK_SNAPSHOT_DEP_FAIL_ON_ERROR, "TrUe");
        InputStream correctPomFile = VersionOverrideModelReaderTest.class.getResourceAsStream("no-snapshot-dep/pom.xml");

        VersionOverrideModelReader customVersionModelReader = new VersionOverrideModelReader();
        customVersionModelReader.setLogger(new ConsoleLogger());
        try {
            Model model = customVersionModelReader.read(correctPomFile, new HashMap<String, Object>());
            assertNotNull(model);
            assertEquals("1.2.3", model.getVersion());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * return
     * 
     * @param dependencies
     * @return
     */
    private boolean hasAtLeastOneSnapshotDependency(List<Dependency> dependencies) {
        for (Dependency dep : dependencies) {
            if (dep.getVersion().contains("SNAPSHOT")) {
                return true;
            }
        }
        return false;
    }
}