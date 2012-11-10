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
import org.apache.maven.plugin.dependency.fromConfiguration.CopyMojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

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
     * if configured then the original artifact id is included in the classifier
     * 
     * @optional
     * @parameter expression="${useOriginalArtifactIdInClassifier}"
     *            default-value="false"
     */
    private boolean useOriginalArtifactIdInClassifier;

    /**
     * if configured then the original classifier is included in the classifier
     * 
     * @optional
     * @parameter expression="${useOriginalClassifierInClassifier}"
     *            default-value="false"
     */
    private boolean useOriginalClassifierInClassifier;

    @Override
    public void execute() throws MojoExecutionException {
        super.execute();
        MavenProject project = getProject();

        for (org.apache.maven.plugin.dependency.fromConfiguration.ArtifactItem each :  super.getArtifactItems()) {

            String newClassifier = "";
            if (useOriginalArtifactIdInClassifier) {
                newClassifier = each.getArtifact().getArtifactId();
            }

            if (useOriginalClassifierInClassifier) {
                if (newClassifier.length() > 0) {
                    newClassifier += "-" + each.getArtifact().getClassifier();
                } else {
                    newClassifier = each.getArtifact().getClassifier();
                }
            }

            projectHelper.attachArtifact(project, each.getArtifact().getType(), newClassifier, each.getArtifact().getFile());
        }
    }
}