package ch.rotscher.mavenext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.maven.model.Model;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.Test;

public class CustomVersionModelReaderTest {

    @Test
    public void testValidSinglePom() {

        InputStream correctPomFile = CustomVersionModelReaderTest.class.getResourceAsStream("valid/single/pom.xml");

        CustomVersionModelReader customVersionModelReader = new CustomVersionModelReader();
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
    public void testValidMultiModulePom() {

        InputStream correctPomFile = CustomVersionModelReaderTest.class.getResourceAsStream("valid/multi/modules/pom.xml");

        CustomVersionModelReader customVersionModelReader = new CustomVersionModelReader();
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

        System.setProperty("release.version", "1.2.3");
        InputStream correctPomFile = CustomVersionModelReaderTest.class.getResourceAsStream("valid/multi/modules/project2/pom.xml");

        CustomVersionModelReader customVersionModelReader = new CustomVersionModelReader();
        customVersionModelReader.setLogger(new ConsoleLogger());
        try {
            Model model = customVersionModelReader.read(correctPomFile, new HashMap<String, Object>());
            assertNotNull(model);
            assertEquals("1.2.3", model.getParent().getVersion());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

}