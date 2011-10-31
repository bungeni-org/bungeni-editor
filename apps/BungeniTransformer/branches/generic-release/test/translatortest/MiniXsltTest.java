package translatortest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import org.bungeni.translators.utility.files.FileUtility;
import org.bungeni.translators.utility.transformer.XSLTTransformer;

public class MiniXsltTest {
	public static void main (String[] args) {
		String inputXslt = "resources/metalex2akn/minixslt/judgement/ref.xsl";
		String inputFile = "tests/testresults/test_judgement_header.mlx";
		System.out.println((new File(inputXslt)).getAbsolutePath());
		
		StreamSource siFile;
		try {
			siFile = FileUtility.getInstance().FileAsStreamSource(inputFile);
		
		StreamSource siXslt = FileUtility.getInstance().FileAsStreamSource(inputXslt);
		StreamSource iteratedDocument = XSLTTransformer.getInstance().transform(siFile, siXslt);
		if (iteratedDocument != null) {
			StringReader outReader = (StringReader) iteratedDocument.getReader();
			BufferedReader in4 = new BufferedReader(outReader);
			PrintWriter out1 = new PrintWriter(System.out);
		      int lineCount = 1;
		      String s = null;
		      while((s = in4.readLine()) != null )
		        out1.println(lineCount++ + ": " + s);
		      out1.close();
		}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
}
