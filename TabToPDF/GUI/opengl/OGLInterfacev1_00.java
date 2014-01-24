package opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFileChooser;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class OGLInterfacev1_00 {
	
	private static final boolean fullscreen = false;
	public static Texture loadTexture(String key)
    {
    try {
		return TextureLoader.getTexture("png", new FileInputStream(new File("res/" + key + ".png")));
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} 
    return null;
    }
	
    public static void main(String[] args) {
        // Initialization code Display
        try {
        	if (fullscreen) {
                Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
            } else {
                Display.setResizable(true);
                Display.setDisplayMode(new DisplayMode(980, 980));
            }
        	Display.setVSyncEnabled(true);
            Display.setTitle("TXT to PDF Converter");
            Display.create();
           
        } catch (LWJGLException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }
        //Creating the background, add button, and convert button.
        makeSquare background = new makeSquare(1f, 1f, true, true, false, 0f, 0f, -11f);
        makeSquare addButton = new makeSquare(0.015f, 0.1f, true, true, false, -0.075f, 0f, -10.4f);
        makeSquare convertButton = new makeSquare(0.015f, 0.1f, true, true, false, -0.075f, -0.025f, -10.4f);
        // Initialization code OpenGL
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective((float) 30, 640f / 480f, 0.001f, 50);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        //enabling transparency
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glTranslatef(0,0,10);
        glRotatef(-0.2f,1f,0,0);
        //Program will keep running as long as the program is not closed.
        while (!Display.isCloseRequested()){
        	if (Display.wasResized()) {
                glViewport(0, 0, Display.getWidth(), Display.getHeight());
            }
            //Clear the screen of its filthy contents.
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glTranslatef(0, 0, 0);
            
            //Draw the background with the texture 'background' located in the res folder.
	        background.drawSquareTexture("background");
	        
	        //See if mouse is hovering in the addButton location.
	        if((float)Mouse.getX()/Display.getWidth() >= 0.0655f && (float)Mouse.getX()/Display.getWidth() <= 0.411f && (float)Mouse.getY()/Display.getHeight() <= 0.362f && (float)Mouse.getY()/Display.getHeight() >= 0.295f){
	        	//Check if left mouse is pressed.
	        	if(Mouse.isButtonDown(0)){
	        		addButton.drawSquareTexture("addFileSelected2");
	        		JFileChooser chooser = new JFileChooser();
	                chooser.setCurrentDirectory(new java.io.File("."));
	                chooser.setDialogTitle("Browse the folder to process");
	                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	                chooser.setAcceptAllFileFilterUsed(false);
	                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	                    System.out.println("getCurrentDirectory(): "+ chooser.getCurrentDirectory());
	                    System.out.println("getSelectedFile() : "+ chooser.getSelectedFile());
	                } else {
	                    System.out.println("No Selection ");
	                }
	        	}
	        	addButton.drawSquareTexture("addFileSelected");
	        }
	        else{
	        	addButton.drawSquareTexture("addFile");
	        }
	        
	      //See if mouse is hovering in the convert button location.
	        if((float)Mouse.getX()/Display.getWidth() >= 0.0655f && (float)Mouse.getX()/Display.getWidth() <= 0.411f && (float)Mouse.getY()/Display.getHeight() <= 0.246f && (float)Mouse.getY()/Display.getHeight() >= 0.181f){
	        	//Check if left mouse is pressed.
	        	if(Mouse.isButtonDown(0)){
	        		convertButton.drawSquareTexture("convertSelected2");
	        		System.out.println("Do convert code.");
	        	}
	        	convertButton.drawSquareTexture("convertSelected");
	        }
	        else{
	        	convertButton.drawSquareTexture("convert");
	        }
	        
            // Update the display
            Display.update();
            // Wait until the frame-rate is 31fps
            Display.sync(31);
        }
        // Dispose of the display
        Display.destroy();
        // Exit the JVM
        System.exit(0);
    }
}
