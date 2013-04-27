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
import org.apache.maven.plugin.dependency.fromConfiguration.CopyMojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

import java.util.List;

/**
 * goal for download a list of artifacts and attach them for redeploying
 * with a new groupId, artifactId and version. See the properties on how
 * the classifier can be set.
 * 
 * @extendsPlugin dependency
 * @extendsGoal copy
 * @goal copy
 * @author Roger Brechbuehl
 */
public class DeployDependencyMojo extends CopyMojo {

    /**
     * @component
     */
    private MavenProjectHelper projectHelper;

    /**
     * <p>attach with the dependencies' artifactId as classifier.
     * Default is true</p>
     * <p>If this flag is set to false but there are more than one artifacts in the list, then the flag is
     *    set to true!
     * </p>
     * 
     * @optional
     * @parameter expression="${artifactIdAsClassifier}"
     *            default-value="true"
     */
    private boolean artifactIdAsClassifier;

    /**
     * preserve the dependencies' classifier if available.
     * Default is true
     * 
     * @optional
     * @parameter expression="${preserveClassifier}"
     *            default-value="true"
     */
    private boolean preserveClassifier;

    @Override
    public void execute() throws MojoExecutionException {
        super.execute();
        MavenProject project = getProject();

        if (!artifactIdAsClassifier && hasMoreThanOneArtifacts(super.getArtifactItems())) {
            //override the flag to true to avoid that an artifact gets ignored
            getLog().warn("your configuration of artifactIdAsClassifier is overriden and set to true " +
                    "as there is more than one artifactItem configured");
            artifactIdAsClassifier = true;
        }

        if (!preserveClassifier && hasMoreThanOneArtifacts(super.getArtifactItems())) {
            //set the flag to true to avoid that an artifact gets ignored
            getLog().warn("your configuration of preserveClassifier is overriden and set to true " +
                    "as there is more than one artifactItem configured");
            preserveClassifier = true;
        }


        for (org.apache.maven.plugin.dependency.fromConfiguration.ArtifactItem each :  super.getArtifactItems()) {

            String newClassifier = "";
            if (artifactIdAsClassifier) {
                newClassifier = each.getArtifact().getArtifactId();
            }

            if (preserveClassifier && each.getArtifact().getClassifier() != null) {
                if (newClassifier.length() > 0) {
                    newClassifier += "-" + each.getArtifact().getClassifier();
                } else {
                    newClassifier = each.getArtifact().getClassifier();
                }
            }

            projectHelper.attachArtifact(project, each.getArtifact().getType(), newClassifier, each.getArtifact().getFile());
        }
    }

    private boolean hasMoreThanOneArtifacts(List<?> artifactsItems) {
        return artifactsItems.size() > 1;
    }
}