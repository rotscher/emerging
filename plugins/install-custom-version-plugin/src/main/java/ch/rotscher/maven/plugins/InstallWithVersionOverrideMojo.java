package ch.rotscher.maven.plugins;

/**
 * Copyright 2012 Roger Brechbühl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.install.InstallMojo;
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
import java.lang.reflect.Field;

/**
 * extending the original install mojo for the version.override feature
 *
 * @author Roger Brechbuehl
 * @extendsPlugin install
 * @extendsGoal install
 * @goal install
 */
public class InstallWithVersionOverrideMojo
        extends InstallMojo {

    public void execute() throws MojoExecutionException {
        try {

            String versionOverride = System.getProperty("version.override");
            if (versionOverride != null) {
                MavenProject project = getMavenProject();
                if (project != null) {
                    getLog().info("version.override: rewrite the version in the original pom.xom in target/pom.xml");

                    //it's very important to set the absolute file name as some classes are using File.equals for comparing
                    //to file instances
                    File targetDir = new File(project.getBasedir(), "target");
                    if (!targetDir.exists()) {
                        targetDir.mkdir();
                    }
                    File newPomFile = new File(targetDir, "pom.xml");
                    newPomFile.createNewFile();
                    replaceVersion(project.getFile(), newPomFile, versionOverride);
                    project.setFile(newPomFile);
                } else {
                    getLog().warn("could not access the project in InstallMojo: install with version.override did not work!");
                }
            }

        } catch (IOException e) {
            getLog().warn(e);
        } catch (JDOMException e) {
            getLog().warn(e);
        }

        super.execute();
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


    private MavenProject getMavenProject() {
        try {
            Field privateStringField = InstallMojo.class.
                    getDeclaredField("project");
            privateStringField.setAccessible(true);

            return (MavenProject) privateStringField.get(this);
        } catch (NoSuchFieldException e) {
            getLog().warn(e);
        } catch (IllegalAccessException e) {
            getLog().warn(e);
        }

        return null;
    }
}