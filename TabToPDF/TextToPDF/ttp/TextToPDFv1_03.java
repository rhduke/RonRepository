package ttp;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.*;
import com.itextpdf.text.Font.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.*;
public class TextToPDFv1_03 {
        
        /* CONSTANTS */
        
        final float LEFT_MARGIN = 36.0f;
        final float RIGHT_MARGIN = 556.0f;
        final float LINE_SPACE = 7.0f;
        final float PARA_SPACE = 15.0f;
        final float TITLE_SIZE = 26.0f;
        final float SUBTITLE_SIZE = 12.0f;
        final String CONTAINS_TITLE = "TITLE";
        final String CONTAINS_SUBTITLE = "SUBTITLE";
        final String CONTAINS_SPACING = "SPACING";
        
        private String title;
        private String subtitle;
        private int spacing;
        
        private List<List<String>> dynamic_array = new ArrayList<List<String>>();
        private List<String> inner;
        private List<List<String>> outerconcat;
        PdfWriter document;
        private int count;
        private float currX, currY ;
        
        
        
        
        
        public void createPDF(String pdfname)throws DocumentException , IOException
        {
                PrintStream output = new PrintStream(System.out);
                
                Document group1 = new Document(PageSize.LETTER);
                
                File RESULT = new File(pdfname);
                PdfWriter document = PdfWriter.getInstance(group1, new FileOutputStream(RESULT));
        
                group1.open();
                document.open();
                PdfContentByte cb = document.getDirectContent();
                BaseFont bf1 = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1250, BaseFont.EMBEDDED);
        
                //Font font = new Font(FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
                //Font f1 = new Font (bf1,12);
                 BaseFont bf_title = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
                Font title_font = new Font(bf_title,26);
                 BaseFont bf_subtitle = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
                Font subtitle_font = new Font(bf_subtitle,12);

                //Font f1 = FontFactory.getFont(BaseFont.COURIER,BaseFont.IDENTITY_H);
                       
                //Paragraph title = new Paragraph("Moonlight Sonata,title_font);
                //title.setAlignment(Element.TITLE);
                //group1.add(title);
                
                //Paragraph subtitle = new Paragraph("Ludwig van Beethoven", subtitle_font);
                //subtitle.setAlignment(Element.ALIGN_CENTER);
                //group1.add(subtitle);
                System.out.printf(" testing is %f\n",document.getVerticalPosition(true));
                System.out.printf("%f\n",group1.leftMargin());
                FileReader file = new FileReader ("try.txt");
        BufferedReader inputStream = null;
        
        try
        {
        
                inputStream = new BufferedReader(file);
                
                
                String line;
                inner = new ArrayList<String>();
                count= 0;
                int linecount = 0;
                
                Pattern p = Pattern.compile("[TITLE|SUBTITLE|SPACING]=(.+)+");
                
                while ( (line = inputStream.readLine()) != null)
                {
                        linecount++;
                        
                        if (linecount < 4) {
                                Matcher m1 = p.matcher(line);
                            if(m1.find() && !line.isEmpty())
                           System.out.println(m1.group(1));
                            
                            
                        if (linecount == 1) {
                        	Paragraph title = new Paragraph(m1.group(1),title_font);
                            title.setAlignment(Element.TITLE);
                            group1.add(title); 
                        }
                        
                        if (linecount == 2) {
                        	Paragraph subtitle = new Paragraph(m1.group(1), subtitle_font);
                            subtitle.setAlignment(Element.ALIGN_CENTER);
                            group1.add(subtitle);
                        }
                        	
                        
                      } else {
                        
                        if (!line.isEmpty()) {
                                
                                line = line.replaceAll("\\p{Z}", "");
                                inner.add(line);
                                count++;
               
             
                        }
                        else if (line.isEmpty())
                        {                
                        
                                dynamic_array.add(new ArrayList<String>(inner));
                                inner.clear();
                                count = 0;                
                        }
                 }
        }

                dynamic_array.add(new ArrayList<String>(inner));
                inner.clear();
        } finally
                        {
                                if (inputStream != null)
                                inputStream.close();
                        }

        outerconcat = new ArrayList<List<String>>();
     for (int i =0 ; i < dynamic_array.size()-1; i+=2)
     {
             for (int j =0 ; j < dynamic_array.get(i).size();j++)
             {
                             
                     inner.add(dynamic_array.get(i).get(j) + dynamic_array.get(i+1).get(j));        
             }
             outerconcat.add(new ArrayList<String>(inner));
         inner.clear();

     }
   
        currX = 36.0f;
        currY = 680.0f;
        
        
    for (List<String> bag : outerconcat) // get the lists
    {
            
            //currX = currX+6.12f; // save position after drawing border
            for (int i = 0 ; i < bag.size() ; i ++) // visit each string in the that list
            {
                    DrawLine(0,currY,36,currY,cb); // draw border horizontal line
            for (int j = 0 ; j < bag.get(i).length() ; j++) // visit character in that string for this list to draw a line with numbers
            {
                    if (bag.get(i).charAt(j) == '|' && i < bag.size()-1) // i < bag.size()-1 because for each line except last line to draw vertically aline
                    {
                     DrawLine(currX,currY,currX,currY-7f,cb); // draw vertically line
        
                    }
                    else if (bag.get(i).charAt(j) == '|' && i == bag.size()-1)// i == bag.size()-1 because we don't want for last line to draw vertically
                    {
                     //DrawLine(currX,currY,currX,currY,cb); // don't draw
        
                    }
                    else if (bag.get(i).charAt(j) == '-'&& currX < 557)// draw horizontal line for each - and check for if ends at 557
                    {
                            DrawLine(currX,currY,currX+5.20f,currY,cb); // draw vertically with respect dashes to -
                            currX = currX+5.20f; // update current position of x to draw the next dash or character
                    }
                    else
                    {
                            InsertText(bag.get(i).charAt(j)+"",currX,currY-2,cb);// write character taking account how many points it takes
                            currX = currX+5.20f;// update current position of x to draw the next dash or character
                    }
                    
                    if (currX >= 556f) // draw the rest of completing lines
                    {
                            DrawLine(currX,currY,612,currY,cb);
                    }
                    
                    
            }
            currX = 36f; // reset margin
            currY = currY - 7f; // go to nextline
            }
            currX = 36f; // reset margin
            currY = currY - 15f; // draw to list
    }
 
         System.out.printf("%f\n",document.getVerticalPosition(true));

        
         System.out.printf("%f\n",group1.leftMargin());

     System.out.printf("the page width is %f and the page height is %f", group1.getPageSize().getWidth(),group1.getPageSize().getHeight());

        group1.close();
        document.close();

                    //System.out.printf("%s\n",dynamic_array.get(0).get(0).replace('-', '\u23AF'));
    
                    
                    
            
     
        }
        
        private String getTitle(String line) {
                String out = "";
                if (line.regionMatches(true, 0, CONTAINS_TITLE, 0, CONTAINS_TITLE.length())) {
                        title = line.substring(CONTAINS_TITLE.length()+1, line.length() - 1);
                        System.out.println(title);
                        out = title;
                }
                return out;
        }
        
        private String getSubtitle(String line) {
                String out = "";
                if (line.regionMatches(true, 0, CONTAINS_SUBTITLE, 0, CONTAINS_SUBTITLE.length())) {
                        subtitle = line.substring(CONTAINS_SUBTITLE.length()+1, line.length() - 1);
                        System.out.println(title);
                        out = subtitle;
                }
                return out;
        }
        
        

        
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
         private static void DrawLine(float x , float y , float toX, float toY,PdfContentByte cb )
         {
                 cb.setLineWidth(0.5f); // Make a bit thicker than 1.0 default
         cb.setGrayStroke(0.20f);// 1 = black, 0 = white
         cb.moveTo(x,y);
         cb.lineTo(toX,toY);
         cb.stroke();        
         }
        
        
        public static void main (String[] args)
        {
                
                
                TextToPDFv1_03 saad = new TextToPDFv1_03();
                
                try {
                        saad.createPDF("heloo");
                } catch (DocumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                 catch (IOException e)
                 {
                        
                 }
        
        
        
                
        }

}
