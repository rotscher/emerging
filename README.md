Emerging JAVA software
======================

This repository contains emerging mingle-mangle java software, I personally use in a professional or private way as a software developer.

Project: lightweight release builds
-----------------------------------

The motivation for all this is written down here: http://diplingfh.blogspot.ch/2013/02/how-has-our-release-process-matured.html

Latest stable Release: 0.3.x (September 2013)  
Get it from maven-central: http://search.maven.org/#search|ga|1|ch.rotscher

- Install the maven-core-extensions.jar in _$M2_HOME/lib/ext_
- Configure the install-custom-version-plugin in your pom (see below)

Contains the following features

Release 0.3.x  
- install-custom-version-plugin for replacing the overridden version in the pom.xml which gets installed  into the local and deployed to the remote repository

this release has been tested with  
- JDK 1.6/1.7
- Maven 3.0.3, 3.0.4, 3.0.5, 3.1.0

All other version are not supported!

Release 0.2.x  
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
  * IMPORTANT: specify the version of internal dependencies with ${project.version}!

Release 0.1.1 (November 2012)

* override _${project.version}_ on the fly  
    to activate that feature execute: _mvn clean install -Dversion.override=1.2.3-RC-5_


configure install-custom-version-plugin
---------------------------------------
Here is an example configuration to put into the root pom.xml

    <profiles>
        <profile>
            <id>version.override</id>
            <activation>
                <property>
                    <name>version.override</name>
                </property>
            </activation>
            <build>
                <pluginManagement>
                    <plugins>
                        <!-- deactivate the default install plugin -->
                        <plugin>
                            <groupId>org.apache.maven.plugins</groupId>
                            <artifactId>maven-install-plugin</artifactId>
                            <executions>
                                <execution>
                                    <id>default-install</id>
                                    <phase>none</phase>
                                </execution>
                            </executions>
                        </plugin>
                    </plugins>
                </pluginManagement>
                <plugins>
                    <!-- configure the custom install plugin for lightweight release builds -->
                    <plugin>
                        <groupId>ch.rotscher.maven.plugins</groupId>
                        <artifactId>install-custom-version-plugin</artifactId>
                        <version>0.3.1</version>
                        <executions>
                            <execution>
                                <id>custom-install</id>
                                <phase>install</phase>
                                <goals>
                                    <goal>install</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>

dependency-attach-plugin (maven-plugin)
---------------------------------------

No more releases planned.

Latest stable Release: 0.2.0 (May 2013)
no changes

Release: 0.1.1 (November 2012)  
Get it from maven-central: http://search.maven.org/#browse%7C818638860

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
