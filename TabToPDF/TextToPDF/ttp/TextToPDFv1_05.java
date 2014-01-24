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
public class TextToPDFv1_05 {
        
                /* What is new :
                 * the method of detecting was for title,subtitle,linespacing but not 100 (see line 162) % .
                 * Apperantly its adding an empty List<string> to the dynamic_array. I tried to make method
                 * to remove emtpy array but did not work
                 * I fixed the method of detecting subtitle ( not detected b string regex ). In fact it was being mached along with title method
                 * removal of dependency of linecount ( to detect title, substring, linespace) making it only dependable on regex . Plus
                 * I made sure that it only detects TITLE=string, subTITLE=string only no numbers, subTITLE=string, only no numbers , spacing=float number no string
                 * I added extra if statement for currY to move to add new page when needed
                 */
                /* what needed to be fixed or add :
                 * removal of empty list from dynamic array
                 * when concatinating of list from dynamic array , if the size of dynamic array is even , it does
                 * all of them. If the size is odd it does not add the last one. added manually , check line 186
                 * add feature to make variable line space
                 */
        
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
        private int enable_add = 0;
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
                Font subtitle_font = new Font(bf_subtitle,14);

                //Font f1 = FontFactory.getFont(BaseFont.COURIER,BaseFont.IDENTITY_H);
                       
                //Paragraph title = new Paragraph("Moonlight Sonata,title_font);
                //title.setAlignment(Element.TITLE);
                //group1.add(title);
                
                //Paragraph subtitle = new Paragraph("Ludwig van Beethoven", subtitle_font);
                //subtitle.setAlignment(Element.ALIGN_CENTER);
                //group1.add(subtitle);
                //System.out.printf(" testing is %f\n",document.getVerticalPosition(true));
                //System.out.printf("%f\n",group1.leftMargin());
                FileReader file = new FileReader ("try.txt");
                BufferedReader inputStream = null;
        
        try
        {
        
                inputStream = new BufferedReader(file);
                String line;
                inner = new ArrayList<String>();
             
                Pattern p_title = Pattern.compile("^(TITLE)(=)(.+)+");
                Pattern p_subtitle = Pattern.compile("^(SUBTITLE)(=)(.+)+");
                Pattern p_spacing = Pattern.compile("^(SPACING)(=)(\\d*(\\.)?\\d+)(?![0-9\\.])");
                Pattern p_music = Pattern.compile("^(\\|)(.+)+");
                while ( (line = inputStream.readLine()) != null)
                {
              
                        Matcher m_title = p_title.matcher(line);
                        Matcher m_subtitle = p_subtitle.matcher(line);
                        Matcher m_spacing = p_spacing.matcher(line);
                        Matcher m_music = p_music.matcher(line);

                     
                            
                         if (m_title.find() && !line.isEmpty()) {
                                 

                            Paragraph title = new Paragraph(m_title.group(3),title_font);
                            title.setAlignment(Element.TITLE);
                            group1.add(title);
                        }
                        
                        else if (m_subtitle.find() && !line.isEmpty()) {

                                Paragraph subtitle = new Paragraph(m_subtitle.group(3), subtitle_font);
                            subtitle.setAlignment(Element.ALIGN_CENTER);
                            group1.add(subtitle);
                            
                        }
                        else if (m_spacing.find() && !line.isEmpty()) {
                                //output.printf("%s\n", m_spacing.group(3));
                        }
                        else if (m_music.find() && !line.isEmpty()) {
                        
                                if (!line.isEmpty()) {
                                    
                                        line = m_music.group(0);
                                            line = line.replaceAll("\\p{Z}", "");
                                           // output.printf("this is %s\n",line);
                                            inner.add(line);
                                            enable_add = 1;        
                                    }
                                                                         
                        }
                        else if (line.isEmpty() && enable_add == 1 ) {
                                dynamic_array.add(new ArrayList<String>(inner));
                                        inner.clear();
                                        enable_add =0;
                        }
                        else;
                        
                        
                        
                } // end of while loop
                 
                dynamic_array.add(new ArrayList<String>(inner));
                inner.clear();
              for (List<String> s : dynamic_array)
{
        for (String e : s)
                output.printf("%s\n", e);
        output.printf("%s\n", "------");
}
                //output.printf("++%s\n",dynamic_array.get(0).get(0));

                
        } finally{
                        
                        if (inputStream != null)
                        inputStream.close();
                         }
        
        //dynamic_array.remove(35);
   
        outerconcat = new ArrayList<List<String>>();
     for (int i =0 ; i < dynamic_array.size()-1; i+=2)
     {
             for (int j =0 ; j < dynamic_array.get(i).size();j++)
             {
                             
                     inner.add(dynamic_array.get(i).get(j) + dynamic_array.get(i+1).get(j));
                     //output.printf("++%s\n",dynamic_array.get(i).get(j));

             }
             outerconcat.add(new ArrayList<String>(inner));
             //outerconcat.add(new ArrayList<String>(dynamic_array.size()-1));
         inner.clear();

     }
     if (dynamic_array.size() % 2 != 0) {
    	 outerconcat.add(new ArrayList<String>(dynamic_array.get(dynamic_array.size()-1)));
     }
     //outerconcat.add(new ArrayList<String>( dynamic_array.get(34)));
     for (List<String> s : outerconcat)
     {
             for (String e : s)
                     output.printf("%s\n", e);
             output.printf("%s\n", "------");
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
                    if (currY <= 16f) { // draw the rest of completing lines
                    
                            group1.newPage();
                            currX = 36.0f;
                            currY = 680.0f;
                            continue;
                           
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
         
         private static void DrawInclinedLine(float x , float y ,PdfContentByte cb )
         {
                         
                 cb.setLineWidth(0.5f); // Make a bit thicker than 1.0 default
                cb.setGrayStroke(0.20f);// 1 = black, 0 = white
                cb.moveTo(x,y);
                cb.lineTo(x+0.1f,y+0.1f);
                cb.stroke();
                cb.setLineWidth(0.5f); // Make a bit thicker than 1.0 default
                cb.setGrayStroke(0.20f);// 1 = black, 0 = white
                cb.moveTo(x,y);
                cb.lineTo(x-0.1f,y-0.1f);
                cb.stroke();
                
         }
         
         // this method remove any empty list inside the outer list
        /* private static List<List<String>> RemoveEmtpyList (List<List<String>> list)
{
         for (List<String> inside : list){
                 if (inside.isEmpty()){
                         list.remove(list.indexOf((List<String>)inside));
                 }        
         }
                return list;
}*/
        
        
        public static void main (String[] args)
        {
                
                
                TextToPDFv1_05 saad = new TextToPDFv1_05();
                
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

