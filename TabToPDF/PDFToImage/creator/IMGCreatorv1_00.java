package creator;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.FontMappings;

/**
 * Sample image creator - converts the first page of a pdf into png
 * NOTE: Code only for testing basic jpedal functions
 */
public class IMGCreatorv1_00
{
	public static void main(String[] args)
	{
		File outputfile = new File("E:/image.png");						// Location of Image file

		/** instance of PdfDecoder to convert PDF into image */
		PdfDecoder decode_pdf = new PdfDecoder(true);

		/** set mappings for non-embedded fonts to use */
		FontMappings.setFontReplacements();

		/** open the PDF file - can also be a URL or a byte array */
		try
		{
			decode_pdf.openPdfFile("E:/Your_Tab.pdf");					// Location of PDF file

			decode_pdf.setExtractionMode(0, 1f);
			BufferedImage img = decode_pdf.getPageAsImage(1);			// Page to convert

			try
			{
				ImageIO.write(img, "png", outputfile);					// Saving the image to png
			} catch (IOException exception) {}
			
			/** close the PDF file */
			decode_pdf.closePdfFile();
			
		} catch (PdfException e)
		{
			e.printStackTrace();
		}
	}
}
