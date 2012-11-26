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
* override _${project.version}_ on the fly  
    to activate that feature execute: _mvn clean install -Dversion.override=1.2.3-RC-5_

Only works with Maven 3.0.3 or 3.0.4!
For more information go to https://github.com/rotscher/emerging/wiki


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
