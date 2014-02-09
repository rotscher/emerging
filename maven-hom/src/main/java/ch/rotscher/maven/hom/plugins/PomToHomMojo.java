package ch.rotscher.maven.hom.plugins;

import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

/**
 * transform pom.xml to hom.xml
 * 
 * @author Roger Brechbuehl
 */
@Mojo(name = "pom2hom", threadSafe = true)
public class PomToHomMojo extends AbstractMojo {

	/**
     */
	@Component
	private MavenProject project;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		File pomFile = project.getFile();
		File homFile = new File(project.getBasedir(), "hom.xml");

		try {
			transform(pomFile, homFile);
		} catch (TransformerConfigurationException e) {
			System.err.println("TransformerConfigurationException");
			System.err.println(e);
		} catch (TransformerException e) {
			System.err.println("TransformerException");
			System.err.println(e);
		}
	}

	public void transform(File pomFile, File homFile)
			throws TransformerConfigurationException, TransformerException {

		TransformerFactory factory = TransformerFactory.newInstance();
		StreamSource xslStream = new StreamSource(getClass().getResourceAsStream("/xslt/pom2hom.xslt"));
		Transformer transformer = factory.newTransformer(xslStream);
		StreamSource in = new StreamSource(pomFile);
		StreamResult out = new StreamResult(homFile);
		transformer.transform(in, out);
		getLog().info(String.format("transformed %s to %s"
				+ "", pomFile.getAbsolutePath(), homFile.getAbsolutePath()));
	}

}
