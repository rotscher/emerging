Emerging JAVA software
======================

This repository contains emerging mingle-mangle java software, I personally use in a professional or private way as a software developer. Depending on how popular or how big a single project becomes a move to a different location is possible.

Project: maven-core-extensions
------------------------------

Latest stable Release: not yet available

Latest SNAPSHOT: 0.1.0-SNAPSHOT
Get it from https://oss.sonatype.org/content/repositories/snapshots

Install the jar in 
    $M2_HOME/lib/ext

Contains the following features
* override ${project.version} on the fly

  to activate that feature execute: 
      mvn clean install -Dversion.override=1.2.3-RC-5

For more information go to https://github.com/rotscher/emerging/wiki


dependency-attach-plugin (maven-plugin)
---------------------------------------

Latest stable Release: not yet available

Latest SNAPSHOT: 0.1.0-SNAPSHOT
Get it from https://oss.sonatype.org/content/repositories/snapshots

For more information go to https://github.com/rotscher/emerging/wiki

Example usage:

    <build>
        <plugins>
            <plugin>
                <groupId>ch.rotscher.maven.plugins</groupId>
                <artifactId>dependency-attach-plugin</artifactId>
                <version>0.1.0-SNAPSHOT</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>copy</goal>
                        </goals>
                        <phase>generate-resources</phase>
                        <configuration>
                            <artifactIdAsClassifier>true</artifactIdAsClassifier>  <!--default is true -->
                            <preserveClassifier>false</preserveClassifier>         <!--default is true -->
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