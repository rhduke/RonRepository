//package project2311Ron;

import java.io.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

/* 
 * CSE2311 Project - Group 1
 * Ronald Hinh
 * 
 * SimpleConvert ()
 * Converts the data from a text file from command line to a PDF document 
 * called TestPdf.pdf
 */

public class SimpleConvert {
	public static void main (String[] args) {
		
		File file = new File(args[0]);	// Reads the filename from the argument
		StringBuffer text = null;	// Where the data is stored
		Document document = new Document();	// PDF document
		
		/* Reads the data from the file and stores it in the StringBuffer */
		text = getFileData(file);
				
		/* Creates the PDF from the StringBuffer */
		createPDF(text.toString(), document);
				
		System.out.println("Converted " + args[0] + " into TestPdf.pdf");
	}
	
	/* Method getFileData()
	 * Reads in each line from the file and appends it to a StringBuffer. 
	 */
	
	static StringBuffer getFileData(File file) {
		StringBuffer text = new StringBuffer();
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				text.append(line);
				text.append("\n");
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}
	
	/* Method createPDF()
	 * Writes the given string to a new PDF file named TestPdf.pdf
	 */
	static void createPDF(String text, Document document) {
		File RESULT = new File("TestPdf.pdf");
		try {
			PdfWriter.getInstance(document, new FileOutputStream(RESULT));
			document.open();
			document.add(new Paragraph(text.toString()));
			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
