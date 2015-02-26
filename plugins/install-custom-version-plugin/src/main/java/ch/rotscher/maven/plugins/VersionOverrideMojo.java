package ch.rotscher.maven.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Roger Brechbuehl
 */

@Mojo(name="version-override", defaultPhase= LifecyclePhase.VERIFY)
public class VersionOverrideMojo extends AbstractMojo {

    @org.apache.maven.plugins.annotations.Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException {
        try {

            String versionOverride = System.getProperty("version.override");
            if (versionOverride != null) {
                getLog().info("version.override: rewrite the version from the original pom.xml in pom-version.override.xml");

                //it's very important to set the absolute file name as some classes are using File.equals for comparing
                //to file instances
                File newPomFile = new File(project.getBasedir(), "pom-version.override.xml");
                newPomFile.createNewFile();
                replaceVersion(project.getFile(), newPomFile, versionOverride);
                project.setFile(newPomFile);
            }

        } catch (IOException e) {
            getLog().warn(e);
        } catch (JDOMException e) {
            getLog().warn(e);
        }
    }

    private void replaceVersion(File originalPomFile, File newPomFile, String newVersion) throws IOException, JDOMException {

        //we assume that the version of "internal" dependencies are declared with ${project.version}
        FileWriter writer = new FileWriter(newPomFile);
        SAXBuilder parser = new SAXBuilder();
        XMLOutputter xmlOutput = new XMLOutputter();
        // display nice nice
        xmlOutput.setFormat(Format.getPrettyFormat());

        //parse the document
        Document doc = parser.build(originalPomFile);
        Element versionElem = findVersionElement(doc);
        versionElem.setText(newVersion);
        xmlOutput.output(doc, writer);
        writer.flush();
        writer.close();
    }

    private Element findVersionElement(Document doc) {
        for (Element element : doc.getRootElement().getChildren()) {
            if (element.getName().equals("version")) {
                return element;
            }
        }

        for (Element element : doc.getRootElement().getChildren()) {
            if (element.getName().equals("parent")) {
                for (Element childElem : element.getChildren()) {
                    if (childElem.getName().equals("version")) {
                        return childElem;
                    }
                }
            }
        }
        return null;
    }
}
