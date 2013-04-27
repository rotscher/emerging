package ch.admin.estv.insieme.build.ext.dependency;

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

/**
 * goal for download a list of artifacts and attach them for redeploying with a new groupId, artifactId and version. See the properties on how the classifier
 * can be set.
 * 
 * @extendsPlugin install
 * @extendsGoal install
 * @goal install
 * @author Roger Brechbuehl
 */
public class DeployDependencyMojo
        extends InstallMojo
{

    public void execute() throws MojoExecutionException {
        try {

            String versionOverride = System.getProperty("version.override");
            if (versionOverride != null) {
                MavenProject project = getProject();

                String inputData = IOUtil.toString(new FileInputStream(project.getFile()));
                String data = inputData.replace("<version>0.5.2-SNAPSHOT</version>", "<version>"+ versionOverride +"</version>");
                FileUtils.fileWrite("target/pom.xml", "UTF-8", data);
                project.setFile(new File("target/pom.xml"));
            }

        } catch (IOException e1) {
            getLog().warn(e1);
        }

        super.execute();
    }
}