Emerging JAVA software
======================

This repository contains emerging mingle-mangle java software, I personally use in a professional or private way as a software developer.

Project: maven-core-extensions
------------------------------

Latest stable Release: 0.1.1 (November 2012)  
Get it from maven-central: http://search.maven.org/#browse%7C1365347524

Latest SNAPSHOT: 0.2.0-SNAPSHOT  
Get it from https://oss.sonatype.org/content/repositories/snapshots

Install the jar in _$M2_HOME/lib/ext_

Contains the following features

Release 0.2.0 (planned for May 2013)
- integrated a build number generation algorithm in the version.override feature

  :: _mvn clean install -Dversion.override=1.2.3-RC-5_                    
         activate version override and provide a version
          
  :: _mvn clean install -Dversion.override_                               
         just activate version override but don't provide a version, version is taken from pom.xml, incremented buildnumber taken from file _.buildnumber_
         (to be added to svn:ignore or .gitignore)
         
         
  :: _mvn clean install -Dversion.override_ 
         with env BUILD_NUMBER set (e.g. in Jenkins/Hudson), version is taken from pom.xml, buildnumber taken from $BUILD_NUMBER
  
  * version.override best works with version formats x.y.z-CLASSIFIER, e.g. 0.5.1-SNAPSHOT or 1.0-RC (delim in tokenizer: hyphen)       
  * Note that the literal "SNAPSHOT" is shorten to "S", e.g. 0.1.0-SNAPSHOT becomes 0.1.0-S-10
  * Classifiers like "RC" or "beta" are not changed just the buildnumber is appended
  * If there is no classifier like "RC" or "beta", no buildnumber is appended
         
- in case the version is overridden a special install plugin is executed which
  rewrites the original version in the pom which gets installed to the local maven repo  
  
  _ch.rotscher.maven.plugins:install-version_override-plugin:0.2.0-SNAPSHOT:install_ (not yet released, see in oss snapshot repo)

Release 0.1.1 (November 2012)

* override _${project.version}_ on the fly  
    to activate that feature execute: _mvn clean install -Dversion.override=1.2.3-RC-5_

Only works with Maven 3.0.3 - 3.0.5 (especially not with 3.0.2 or any previous versions)!
Read this story about how all this stuff has been established: https://github.com/rotscher/emerging/wiki

dependency-attach-plugin (maven-plugin)
---------------------------------------

Latest stable Release: 0.1.1 (November 2012)  
Get it from maven-central: http://search.maven.org/#browse%7C818638860

Latest SNAPSHOT: 0.2.0-SNAPSHOT  
Get it from https://oss.sonatype.org/content/repositories/snapshots

For more information go to https://github.com/rotscher/emerging/wiki

Example usage:

    <build>
        <plugins>
            <plugin>
                <groupId>ch.rotscher.maven.plugins</groupId>
                <artifactId>dependency-attach-plugin</artifactId>
                <version>0.1.1</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>copy</goal>
                        </goals>
                        <phase>generate-resources</phase>
                        <configuration>
                            <artifactIdAsClassifier>true</artifactIdAsClassifier>  <!--default: true -->
                            <preserveClassifier>false</preserveClassifier>         <!--default: true -->
                            <artifactItems>
                                <artifactItem>
                                    <groupId>a.groupid</groupId>
                                    <artifactId>module</artifactId>
                                    <version>2.0.1</version>
                                </artifactItem>
                            </artifactItems>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
