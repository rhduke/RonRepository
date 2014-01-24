package ttp;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

/*
 * (Update): So it properly reads the title, subtitle and spacing from
 * the file without screwing up the rest of it by creating a separate file
 * reader. 
 * 
 * I split createPDF() into several methods that make it more coder friendly. 
 * Mostly everything is pretty well commented. There are still some things that 
 * Saad needs to explain like how the 2-d list arrays store information because
 * I'm pretty confused about that.
 * 
 * You can change the input file name the program searches for in the
 * constants section.
 * 
 * (Note) There is still a problem where the last 2 staffs aren't being printed.
 * The list variable "dynamic_array" has them stored but the "outerconcat" list does 
 * not properly copy them. This was a problem the past versions had as well.
 * 
 * - Ron
 * 
 */

/**
 * Reads in an input file of guitar tabs and converts the contents into a PDF document.
 * Guitar tabs are stored in a 2-dimensional list. The list is traversed and iteratively
 * added to the output file. 
 */

public class TextToPDFv1_06 {
	
	/* CONSTANTS */
	
	public static final String INPUT_FILENAME = "try.txt";	// Name of input file to search for
	public static final String PDF_FILENAME = "output.pdf";	// Filename of the output PDF file
	
	static final int MAX_DOC_INFO = 3;				// There are 3 doc infos: Title, Subtitle, Spacing
	static final String INPUT_TITLE_PATTERN = "^(TITLE)(=)(.+)+";
	static final String INPUT_SUBTITLE_PATTERN = "^(SUBTITLE)(=)(.+)+";
	static final String INPUT_SPACING_PATTERN = "^(SPACING)(=)(\\d*(\\.)?\\d+)(?![0-9\\.])";
	static final float PDF_PAGE_WIDTH = 612f;
	static final float PDF_LEFT_MARGIN = 36f;
	static final float PDF_RIGHT_MARGIN = 556f;
	static final float PDF_LINE_SPACE = 7f;
	static final float PDF_PARA_SPACE = 15f;
	static final float PDF_TITLE_SIZE = 26f;
	static final float PDF_SUBTITLE_SIZE = 12f;
	static final String CONTAINS_TITLE = "TITLE";
	static final String CONTAINS_SUBTITLE = "SUBTITLE";
	static final String CONTAINS_SPACING = "SPACING";

	static final float UNKNOWN1 = 680f;
	static final float UNKNOWN2 = 5.2f;
	
	/* GLOBAL VARIABLES */
	
	private String title;
	private String subtitle;
	private int spacing;	
	private List<List<String>> dynamic_array = new ArrayList<List<String>>();	// Stores information from the input file
	private List<String> inner;		// Temp variable used when storing list elements
	private List<List<String>> outerconcat;
	private PdfWriter  document;	// Used for writing to the PDF document
	private int count;
	private float currX, currY ;
	
	/**
	 * Runs the main method creatPDF() that reads the guitar tabs from the 
	 * input file and converts it into a PDF file.
	 * 
	 * @param args
	 */
	
	public static void main (String[] args)
	{		
		TextToPDFv1_06 saad = new TextToPDFv1_06();
		
		try {
			saad.createPDF(PDF_FILENAME);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		 catch (IOException e)
		 {
			 e.printStackTrace();
		 }			
		System.out.println("Successfully converted " + INPUT_FILENAME + " to " + PDF_FILENAME + "!");
	}	
	
	/**
	 * Creates a PDF file to write to. Saves the data of the input file and then
	 * writes it to the output file.
	 * 
	 * @param pdfname
	 * @throws DocumentException
	 * @throws IOException
	 */
	
	public void createPDF(String pdfname) throws DocumentException, IOException {
		
		File RESULT = new File(pdfname);	// The output PDF file
		Document group1 = new Document(PageSize.LETTER);	// PDF document
		document = PdfWriter.getInstance(group1, new FileOutputStream(RESULT));	// PDF writer
		       
		/* Open the PDF document for writing */
		group1.open();
		document.open();
		
		/* Reads and saves the Title, Subtitle and Spacing from the input file */
		getDocInfo();
		
		/* Sets the Title and Subtitle for the PDF file */
		group1.add(addDocTitle());
		group1.add(addDocSubtitle());
		
		/* Saves the data from the input file to: List<List<String>> dynamic_array */
		readInput();
		
		/* Sets List<List<String>> outerconcat that will be used when writing to the file */
        	setOuter();

        	/* Writes the stored data to the PDF file */
        	writePDF();
       
        	/* Writing is done, close the PDF file */
        	group1.close();
		document.close();
	}
	
	/**
	 * The method reads the input file and for the first few lines, extracts
	 * the document info and saves them to variables (title, subtitle, spacing).
	 * Uses patterns and matchers to find key words in the input file.
	 */
	
	private void getDocInfo() {
		BufferedReader reader; 
		String line;
		int linecount = 0;
			
		try {
			reader = new BufferedReader(new FileReader (INPUT_FILENAME));
			while ((line = reader.readLine()) != null) {	// Read each line
				
				linecount++;
				/* Stop reading after this many lines */
				if (linecount > MAX_DOC_INFO) {
					break;
				/* Detect a pattern and extract the title, subtitle and spacing */
				} else {
					Matcher mtitle = (Pattern.compile(INPUT_TITLE_PATTERN)).matcher(line);	// Title pattern to look for
					Matcher msub = (Pattern.compile(INPUT_SUBTITLE_PATTERN)).matcher(line);	// Subtitle pattern to look for
					Matcher mspace = (Pattern.compile(INPUT_SPACING_PATTERN)).matcher(line);	// Spacing pattern to look for
					if (mtitle.find())	// If the title pattern is found then set the title
						this.title = mtitle.group(3);
					else if (msub.find())	// If the subtitle is found then set the subtitle
						this.subtitle = msub.group(3);
					else if (mspace.find())	// If the spacing is found then set the spacing
						this.spacing = Integer.parseInt(mspace.group(3));
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The method reads the input file line by line and stores it in a
	 * 2-dimensional list. 
	 */
	
	private void readInput() {
		BufferedReader inputStream = null;
		FileReader file;
		try {
			file = new FileReader (INPUT_FILENAME);
        		inputStream = new BufferedReader(file);
        		String line;
        		inner = new ArrayList<String>(); 
        		count = 0;
        		int linecount = 0;
        	
        		while ((line = inputStream.readLine()) != null) {	// Read each line	
        			if (linecount <= MAX_DOC_INFO) {	// Skip the lines with document info
        				linecount++;
        			} else {
        				/* If line is not empty, delete all spaces and add line to a temp list */
             				if (!line.isEmpty()) {		     			
            				line = line.replaceAll("\\p{Z}", "");	// Deletes spaces
            				inner.add(line);
            				count++;             
            			}
             			/* If line is empty, then add the temp list to the larger list */
            			else if (line.isEmpty()) {		       		
            				dynamic_array.add(new ArrayList<String>(inner));
            				inner.clear();
            				count = 0;		
            			}   
        		}   		   
        	}
        	inputStream.close();
        	dynamic_array.add(new ArrayList<String>(inner));
        	inner.clear();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
        	}
	}
	
	/**
	 * Sets the outerconcat list which will be used when writing to the PDF file.
	 */
	
	private void setOuter() {
		outerconcat = new ArrayList<List<String>>();
        	for (int i =0 ; i < dynamic_array.size()-1; i+=2) {
        		for (int j =0 ; j < dynamic_array.get(i).size();j++)    		 	 
        			inner.add(dynamic_array.get(i).get(j) + dynamic_array.get(i+1).get(j));	
        		outerconcat.add(new ArrayList<String>(inner));
        		inner.clear();
        	}
	}
	
	/**
	 * Iterates through the outerconcat list and adds the guitar tabs to the
	 * PDF file. 
	 * 
	 * @throws DocumentException
	 * @throws IOException
	 */
	
	private void writePDF() throws DocumentException, IOException {
		PdfContentByte cb = document.getDirectContent();
		
		currX = PDF_LEFT_MARGIN;
        	currY = UNKNOWN1;
        
        
        	for (List<String> bag : outerconcat) { // get the lists
        	
        		//currX = currX+6.12f; // save position after drawing border
        		for (int i = 0 ; i < bag.size() ; i ++) { // visit each string in the that  list 
        		
        			DrawLine(0,currY,PDF_LEFT_MARGIN,currY,cb); // draw border horizontal line
        			for (int j = 0 ; j < bag.get(i).length() ; j++) { // visit character in that  string for this list to draw a line with numbers
        		
        				if (bag.get(i).charAt(j) == '|' && i < bag.size()-1) { // i < bag.size()-1 because for each line except last line to draw vertically aline
        			
        				DrawLine(currX,currY,currX,currY-PDF_LINE_SPACE,cb); // draw vertically line
	
        				} else if (bag.get(i).charAt(j) == '|' && i == bag.size()-1) { // i == bag.size()-1 because we don't want for last line to draw vertically
        			
        					//DrawLine(currX,currY,currX,currY,cb); // don't draw
	
        				} else if (bag.get(i).charAt(j) == '-'&& currX < PDF_RIGHT_MARGIN) { // draw horizontal line for each - and check for if ends at 557
        			
        					DrawLine(currX,currY,currX+UNKNOWN2,currY,cb); // draw vertically with respect dashes to -
        					currX = currX+UNKNOWN2; // update current position of x to draw the next dash or character
        				} else {
        			
        					InsertText(bag.get(i).charAt(j)+"",currX,currY-2,cb);// write character taking account how many points it takes
        					currX = currX+UNKNOWN2;// update current position of x to draw the next dash or character
        				}
    		
        				if (currX >= PDF_RIGHT_MARGIN) { // draw the rest of completing lines
        			
        					DrawLine(currX,currY,PDF_PAGE_WIDTH,currY,cb);
        				}
    	
        			}
	        		currX = PDF_LEFT_MARGIN; // reset margin
        			currY = currY - PDF_LINE_SPACE; // go to nextline
        		}
        		currX = PDF_LEFT_MARGIN; // reset margin
        		currY = currY - PDF_PARA_SPACE; // draw to list
        	}	    
	}
	
	/**
	 * The method creates a title in PDF format.
	 * 
	 * @return		The title of the document. Returns null if no title is found.
	 */
	
	private Paragraph addDocTitle() {
		BaseFont bf_title;
		Paragraph title = null;
		try {
			bf_title = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
			Font title_font = new Font(bf_title,PDF_TITLE_SIZE);
			title = new Paragraph(this.title,title_font);
			title.setAlignment(Element.TITLE);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return title;
	}
	
	/**
	 * The method creates a subtitle in PDF format.
	 * 
	 * @return		The subtitle of the document. Returns null if no subtitle is found.
	 */
	
	private Paragraph addDocSubtitle() {
		BaseFont bf_subtitle;
		Paragraph subtitle = null;
		try {
			bf_subtitle = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
			Font subtitle_font = new Font(bf_subtitle,PDF_SUBTITLE_SIZE);
			subtitle = new Paragraph(this.subtitle, subtitle_font);
			subtitle.setAlignment(Element.ALIGN_CENTER);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return subtitle;
	}
	
	/**
	 * The method writes the given text to an (x,y) coordinate in the PDF file.
	 * 
	 * @param text
	 * @param x
	 * @param y
	 * @param cb
	 * @throws DocumentException
	 * @throws IOException
	 */
	
	 private static void InsertText(String text, float x, float y , PdfContentByte cb) throws DocumentException, IOException {
		   
		      BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		      cb.saveState();
		      cb.beginText();
		      //cb.moveText(x, y);
		      cb.setTextMatrix(x, y);
		      cb.setFontAndSize(bf, 8);
		      cb.showText(text);
		      cb.endText();
		      cb.restoreState();
		   
	 }
	 
	 /**
	  * The method draws a line from an x to a y coordinate.
	  * 
	  * @param x
	  * @param y
	  * @param toX
	  * @param toY
	  * @param cb
	  */
	 
	 private static void DrawLine(float x , float y , float toX, float toY,PdfContentByte cb ) {
		cb.setLineWidth(0.5f); // Make a bit thicker than 1.0 default
		cb.setGrayStroke(0.20f);// 1 = black, 0 = white   
		cb.moveTo(x,y); 
		cb.lineTo(toX,toY);
		cb.stroke();	 
	 }
	
	
	
}
