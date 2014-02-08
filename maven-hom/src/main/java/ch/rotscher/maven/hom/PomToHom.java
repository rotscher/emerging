package ch.rotscher.maven.hom;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class PomToHom {

	public static void main(String[] args)
    {
        String dataXML = "/home/rotscher/projects/github/emerging/plugins/install-custom-version-plugin/pom.xml";
        String inputXSL = "/home/rotscher/projects/github/emerging/maven-hom/src/main/resources/xslt/pom2hom.xslt";
        String outputHTML = "/home/rotscher/projects/github/emerging/plugins/install-custom-version-plugin/hom.xml";

        PomToHom st = new PomToHom();
        try
        {
            st.transform(dataXML, inputXSL, outputHTML);
        }
        catch (TransformerConfigurationException e)
        {
            System.err.println("TransformerConfigurationException");
            System.err.println(e);
        }
        catch (TransformerException e)
        {
            System.err.println("TransformerException");
            System.err.println(e);
        }
    }

    public void transform(String dataXML, String inputXSL, String outputHTML)
            throws TransformerConfigurationException,
            TransformerException
    {

        TransformerFactory factory = TransformerFactory.newInstance();
        StreamSource xslStream = new StreamSource(inputXSL);
        Transformer transformer = factory.newTransformer(xslStream);
        StreamSource in = new StreamSource(dataXML);
        StreamResult out = new StreamResult(outputHTML);
        transformer.transform(in, out);
        System.out.println("The generated HTML file is:" + outputHTML);
    }
	
}
