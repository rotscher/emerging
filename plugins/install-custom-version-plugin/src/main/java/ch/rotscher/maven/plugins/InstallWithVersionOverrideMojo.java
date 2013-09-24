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
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * extending the original install mojo for the version.override feature
 * 
 * @extendsPlugin install
 * @extendsGoal install
 * @goal install
 * @author Roger Brechbuehl
 */
public class InstallWithVersionOverrideMojo
        extends InstallMojo
{

    public void execute() throws MojoExecutionException {
        try {

            String versionOverride = System.getProperty("version.override");
            if (versionOverride != null) {
                MavenProject project = getMavenProject();
                if (project != null) {
                    getLog().info("version.override: rewrite the version in the original pom.xom in target/pom.xml");

                    String inputData = IOUtil.toString(new FileInputStream(project.getFile()));
                    String data = inputData.replace("<version>0.5.3-SNAPSHOT</version>", "<version>"+ versionOverride +"</version>");
                    FileUtils.fileWrite("target/pom.xml", "UTF-8", data);
                    //it's very important to set the absolute file name as some classes are using File.equals for comparing
                    //to file instances
                    project.setFile(new File(project.getBasedir(), "target/pom.xml"));
                } else {
                    getLog().warn("could not access the project in InstallMojo: install with version.override did not work!");
                }
            }

        } catch (IOException e1) {
            getLog().warn(e1);
        }

        super.execute();
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