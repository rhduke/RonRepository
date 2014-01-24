package opengl;

import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class makeSquare{
	float TRx, TRy, TRz, TLx, TLy, TLz, BLx, BLy, BLz, BRx, BRy, BRz;
	float height, width, depth, centerLocx, centerLocy, centerLocz;
	float angle;
	float hypotenuse;
	int face;
	boolean xPlane, yPlane, zPlane;
	public makeSquare(float height, float width, boolean xPlane, boolean yPlane, boolean zPlane, float centerLocx, float centerLocy, float centerLocz){
		this.face = glGenLists(1);
		this.xPlane = xPlane;
		this.yPlane = yPlane;
		this.zPlane = zPlane;
		
		this.height = height;
		this.width = width;
		
		this.centerLocx = centerLocx;
		this.centerLocy = centerLocy;
		this.centerLocz = centerLocz;
		if(xPlane && yPlane){
			this.TRx = this.centerLocx + (this.width/2f);
			this.TRy = this.centerLocy + (this.height/2f);
			this.TRz = this.centerLocz;
			
			this.TLx = this.centerLocx - (this.width/2f);
			this.TLy = this.centerLocy + (this.height/2f);
			this.TLz = this.centerLocz;
			
			this.BLx = this.centerLocx - (this.width/2f);
			this.BLy = this.centerLocy - (this.height/2f);
			this.BLz = this.centerLocz;
			
			this.BRx = this.centerLocx + (this.width/2f);
			this.BRy = this.centerLocy - (this.height/2f);
			this.BRz = this.centerLocz;
			
			
			this.hypotenuse = (float) Math.sqrt(Math.pow(this.height/2, 2) + Math.pow(this.width/2, 2));
			this.angle = 0f;
		}
		else if(xPlane && zPlane){
			this.TRx = this.centerLocx + (this.width/2f);
			this.TRy = this.centerLocy;
			this.TRz = this.centerLocz - (this.height/2f);
			
			this.TLx = this.centerLocx - (this.width/2f);
			this.TLy = this.centerLocy;
			this.TLz = this.centerLocz - (this.height/2f);
			
			this.BLx = this.centerLocx - (this.width/2f);
			this.BLy = this.centerLocy;
			this.BLz = this.centerLocz + (this.height/2f);
			
			this.BRx = this.centerLocx + (this.width/2f);
			this.BRy = this.centerLocy;
			this.BRz = this.centerLocz + (this.height/2f);
			
			
			this.hypotenuse = (float) Math.sqrt(Math.pow(this.height/2, 2) + Math.pow(this.width/2, 2));
			this.angle = 0f;
		}
		else if(yPlane && zPlane){
			this.TRx = this.centerLocx;
			this.TRy = this.centerLocy + (this.height/2f);
			this.TRz = this.centerLocz - (this.width/2f);
			
			this.TLx = this.centerLocx;
			this.TLy = this.centerLocy + (this.height/2f);
			this.TLz = this.centerLocz + (this.width/2f);
			
			this.BLx = this.centerLocx;
			this.BLy = this.centerLocy - (this.height/2f);
			this.BLz = this.centerLocz + (this.width/2f);
			
			this.BRx = this.centerLocx;
			this.BRy = this.centerLocy - (this.height/2f);
			this.BRz = this.centerLocz - (this.width/2f);
			
			
			this.hypotenuse = (float) Math.sqrt(Math.pow(this.height/2, 2) + Math.pow(this.width/2, 2));
			this.angle = 90f;
		}
		
		//Creating the square face
		glNewList(this.face, GL_COMPILE);
		glBegin(GL_QUADS);
			glTexCoord2f(1,0); glVertex3f(this.TRx, this.TRy, this.TRz);
			glTexCoord2f(0,0); glVertex3f(this.TLx, this.TLy, this.TLz);
			glTexCoord2f(0,1); glVertex3f(this.BLx, this.BLy, this.BLz);
			glTexCoord2f(1,1); glVertex3f(this.BRx, this.BRy, this.BRz);
		glEnd();
		glEndList();
	}
	public void drawSquareTexture(String someTexture){
		try {
			TextureLoader.getTexture("png", new FileInputStream(new File("res/" + someTexture + ".png")));
			glCallList(this.face);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void rotateHorizontal(boolean clockwise, float speed){
		if(clockwise){
			
		}
		else{
			
		}
		//Draw new face
		glDeleteLists(this.face, 1);
		glNewList(this.face, GL_COMPILE);
		glBegin(GL_QUADS);
			glTexCoord2f(1,0); glVertex3f(this.TRx, this.TRy, this.TRz);
			glTexCoord2f(0,0); glVertex3f(this.TLx, this.TLy, this.TLz);
			glTexCoord2f(0,1); glVertex3f(this.BLx, this.BLy, this.BLz);
			glTexCoord2f(1,1); glVertex3f(this.BRx, this.BRy, this.BRz);
		glEnd();
		glEndList();	
	}
	
	public void rotateHorizontal(int angle){
			
		//draw new face
		glDeleteLists(this.face, 1);
		glNewList(this.face, GL_COMPILE);
		glBegin(GL_QUADS);
			glTexCoord2f(1,0); glVertex3f(this.TRx, this.TRy, this.TRz);
			glTexCoord2f(0,0); glVertex3f(this.TLx, this.TLy, this.TLz);
			glTexCoord2f(0,1); glVertex3f(this.BLx, this.BLy, this.BLz);
			glTexCoord2f(1,1); glVertex3f(this.BRx, this.BRy, this.BRz);
		glEnd();
		glEndList();		
	}
	
	public int getAngle(){
		return (int)Math.toDegrees(this.angle);
	}
	
	public void translateX(boolean left, float speed){
		if(left){
			this.TRx -= speed;
			this.TLx -= speed;
			this.TLx -= speed;
			this.TRx -= speed;
			
			this.centerLocx -= speed;
		}
		else{
			this.TRx += speed;
			this.TLx += speed;
			this.TLx += speed;
			this.TRx += speed;
			
			this.centerLocx += speed;
		}
		glDeleteLists(this.face, 1);
		glNewList(this.face, GL_COMPILE);
		glBegin(GL_QUADS);
			glTexCoord2f(1,0); glVertex3f(this.TRx, this.TRy, this.TRz);
			glTexCoord2f(0,0); glVertex3f(this.TLx, this.TLy, this.TLz);
			glTexCoord2f(0,1); glVertex3f(this.BLx, this.BLy, this.BLz);
			glTexCoord2f(1,1); glVertex3f(this.BRx, this.BRy, this.BRz);
		glEnd();
		glEndList();
	}
	
	public void translateY(boolean down, float speed){
		if(down){
			this.TRy -= speed;
			this.TLy -= speed;
			this.TLy -= speed;
			this.TRy -= speed;
			
			this.BRy -= speed;
			this.BLy -= speed;
			this.BLy -= speed;
			this.BRy -= speed;
			this.centerLocy -= speed;
		}
		else{
			this.TRy += speed;
			this.TLy += speed;
			this.TLy += speed;
			this.TRy += speed;
			
			this.BRy += speed;
			this.BLy += speed;
			this.BLy += speed;
			this.BRy += speed;
			this.centerLocy += speed;
		}
		glDeleteLists(this.face, 1);
		glNewList(this.face, GL_COMPILE);
		glBegin(GL_QUADS);
			glTexCoord2f(1,0); glVertex3f(this.TRx, this.TRy, this.TRz);
			glTexCoord2f(0,0); glVertex3f(this.TLx, this.TLy, this.TLz);
			glTexCoord2f(0,1); glVertex3f(this.BLx, this.BLy, this.BLz);
			glTexCoord2f(1,1); glVertex3f(this.BRx, this.BRy, this.BRz);
		glEnd();
		glEndList();
	}
	
	public void translateZ(boolean farther, float speed){
		if(farther){
			this.TRz -= speed;
			this.TLz -= speed;
			this.TLz -= speed;
			this.TRz -= speed;
			
			this.BRz -= speed;
			this.BLz -= speed;
			this.BLz -= speed;
			this.BRz -= speed;
			this.centerLocz -= speed;
		}
		else{
			this.TRz += speed;
			this.TLz += speed;
			this.TLz += speed;
			this.TRz += speed;
			
			this.BRz += speed;
			this.BLz += speed;
			this.BLz += speed;
			this.BRz += speed;
			this.centerLocz += speed;
		}
		glDeleteLists(this.face, 1);
		glNewList(this.face, GL_COMPILE);
		glBegin(GL_QUADS);
			glTexCoord2f(1,0); glVertex3f(this.TRx, this.TRy, this.TRz);
			glTexCoord2f(0,0); glVertex3f(this.TLx, this.TLy, this.TLz);
			glTexCoord2f(0,1); glVertex3f(this.BLx, this.BLy, this.BLz);
			glTexCoord2f(1,1); glVertex3f(this.BRx, this.BRy, this.BRz);
		glEnd();
		glEndList();
	}
	
	public float getCenterLocx() {
		return this.centerLocx;
	}
	
	public float getCenterLocy() {
		return this.centerLocy;
	}
	
	public float getCenterLocz() {
		return this.centerLocz;
	}
	
	public void setCenterLocx(float num) {
		this.centerLocx = num;
	}
	
	public void setCenterLocy(float num) {
		this.centerLocy = num;
	}
	
	public void setCenterLocz(float num) {
		this.centerLocz = num;
	}
}

