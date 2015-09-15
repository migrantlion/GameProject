package helpers;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.io.IOException;
import java.io.InputStream;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Artist {

	public static final int WIDTH = 1280, HEIGHT = 960;
	public static final int HEX_SIZE = 32;
	
	public static void BeginSession() {
		Display.setTitle("SRS Game");
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

	}
	
	public static boolean CheckCollision(float x1, float y1, float width1, float height1,
			float x2, float y2, float width2, float height2){
		if (x1 + width1 > x2 && x1 < x2 + width2 && y1 + height1 > y2 && y1 < y2 + height2)
			return true;
		return false;
	}

	public static void DrawQuad(float x, float y, float width, float height) {
		glBegin(GL_QUADS);
		glVertex2f(x, y);
		glVertex2f(x + width, y);
		glVertex2f(x + width, y + height);
		glVertex2f(x, y + height);
		glEnd();
	}

	public static void DrawQuadTex(Texture tex, float x, float y, float width, float height) {
		tex.bind();
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(1, 0);
		glVertex2f(width, 0);
		glTexCoord2f(1, 1);
		glVertex2f(width, height);
		glTexCoord2f(0, 1);
		glVertex2f(0, height);
		glEnd();
		glLoadIdentity();
	}
	
	public static void DrawQuadTex(Texture tex, float x, float y, float width, float height, boolean centered) {
		tex.bind();
		if (centered){
			x -= width/2;
			y -= height/2;
		}
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(1, 0);
		glVertex2f(width, 0);
		glTexCoord2f(1, 1);
		glVertex2f(width, height);
		glTexCoord2f(0, 1);
		glVertex2f(0, height);
		glEnd();
		glLoadIdentity();
	}
	
	public static void DrawHexOutline(float x, float y, float face){ 
		float[] xCoords = {1, 0.5f, -0.5f, -1, -0.5f, 0.5f, 1};
		float[] yCoords = {0, 1, 1, 0, -1, -1, 0 };
		int TOT_PNTS = 7;
		double hexAngle = 2*Math.PI/6;
		float radius =  face / ((float) Math.sin(hexAngle));
		
		GL11.glBegin(GL11.GL_LINE_LOOP);
		glLineWidth(1.0f);
		for (int i = 0; i < TOT_PNTS; i++){
			glVertex2f(radius*xCoords[i], face*yCoords[i]);
		}
		GL11.glEnd();
		glLoadIdentity();
	}
	
	public static void DrawHexTex(Texture tex, float x, float y, float face){
		int num_vertices = 18;
		double hexAngle = 2*Math.PI/6;
		float yfactor = (float) Math.sin(hexAngle);
		float radius = face / yfactor;
		
		float[] xCoords = {
				0, 1, 0.5f, 
				0, 0.5f, -0.5f,
				0, -0.5f, -1,
				0, -1, -0.5f,
				0, -0.5f, 0.5f,
				0, 0.5f, 1 };
		float[] yCoords = {
				0, 0, 1, 
				0, 1, 1,
				0, 1, 0,
				0, 0, -1,
				0, -1, -1,
				0, -1, 0 };
		
		tex.bind();
		glTranslatef(x, y, 0);
		glBegin(GL_TRIANGLE_FAN);	
		
		for (int i = 0; i < num_vertices; i++){
			glTexCoord2f(xCoords[i]/2+0.5f, yfactor*yCoords[i]/2+0.5f);
			glVertex2f(radius*xCoords[i], face*yCoords[i]);
		}
		glEnd();
		glLoadIdentity();
	}

	public static void DrawQuadTexRot(Texture tex, float x, float y, 
			float width, float height, float angle) {
		tex.bind();
		glTranslatef(x + width / 2, y + height / 2, 0);
		glRotatef(angle, 0, 0, 1);
		glTranslatef(-width / 2, -height / 2, 0);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(1, 0);
		glVertex2f(width, 0);
		glTexCoord2f(1, 1);
		glVertex2f(width, height);
		glTexCoord2f(0, 1);
		glVertex2f(0, height);
		glEnd();
		glLoadIdentity();
	}
	
	public static void DrawQuadTexRot(Texture tex, float x, float y, 
			float width, float height, float angle, boolean centered) {
		tex.bind();
		if (centered)
			glTranslatef(x, y, 0);
		else 
			glTranslatef(x + width / 2, y + height / 2, 0);
		glRotatef(angle, 0, 0, 1);
		glTranslatef(-width / 2, -height / 2, 0);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(1, 0);
		glVertex2f(width, 0);
		glTexCoord2f(1, 1);
		glVertex2f(width, height);
		glTexCoord2f(0, 1);
		glVertex2f(0, height);
		glEnd();
		glLoadIdentity();
	}

	private static Matrix2f rotateMatrix(double angle){
		Matrix2f matrix = new Matrix2f();
		matrix.m00 = (float) Math.cos(angle);
		matrix.m01 = (float) Math.sin(angle);
		matrix.m10 = -(float) Math.sin(angle);
		matrix.m11 = (float) Math.cos(angle);

		return matrix;
	}
	
	public static void DrawTriangleTex(Texture tex, EquiTriangle triangle){
		Texture triangletex = QuickLoadTex("purple");
		
		triangletex.bind();
		glBegin(GL_TRIANGLES);
		
		glTexCoord2f(0,0);
		glVertex2f(triangle.p1.x,triangle.p1.y);
		
		glTexCoord2f(0,1);
		glVertex2f(triangle.p2.x, triangle.p2.y);
		
		glTexCoord2f(0.5f, 0.5f * (float) Math.tan(Math.PI/3));
		glVertex2f(triangle.p3.x, triangle.p3.y);
		
		glEnd();
		glLoadIdentity();
		
		DrawQuadTex(tex, triangle.getCenter().x, triangle.getCenter().y, triangle.getSide()/3, triangle.getSide()/3, true);
	}
	
//	public static void DrawTriangleTexRot(Texture tex, float x, float y, 
//			float side, double radians) {
//		Texture triangletex = QuickLoadTex("purple");
//		double angle = Math.PI/3;
//		Vector2f center = new Vector2f(0, -0.66f * side * ((float)Math.cos(angle/2)));
//		Vector2f vertice1 = new Vector2f(0,0);
//		Vector2f vertice2 = new Vector2f(side*((float) Math.sin(angle/2)), -side*((float) Math.cos(angle/2)));
//		Vector2f vertice3 = new Vector2f(-side*((float) Math.sin(angle/2)), -side*((float) Math.cos(angle/2)));
//		Matrix2f rotMatrix = new Matrix2f();
//		rotMatrix = rotateMatrix(radians);
//		
//		//rotate the triangle
//		center = rotMatrix.transform(rotMatrix, center, center);
//		
//		triangletex.bind();
//		glBegin(GL_TRIANGLES);
//		
//		glTexCoord2f(vertice1.x, vertice1.y);
//		//rotate to position
//		vertice1 = rotMatrix.transform(rotMatrix, vertice1, vertice1);		
//		glVertex2f(vertice1.x + x, vertice1.y + y);
//		
//		glTexCoord2f(vertice1.x, vertice1.y);
//		//rotate to position
//		vertice2 = rotMatrix.transform(rotMatrix, vertice2, vertice2);
//		glVertex2f(vertice2.x + x, vertice2.y + y);
//		
//		glTexCoord2f(vertice1.x, vertice1.y);
//		//rotate to position
//		vertice3 = rotMatrix.transform(rotMatrix, vertice3, vertice3);
//		glVertex2f(vertice3.x + x, vertice3.y + y);
//		
//		glEnd();
//		glLoadIdentity();
//		
//		DrawQuadTex(tex, center.x + x, center.y + y, side/3, side/3, true);
//	}
	
	public static Texture LoadTexture(String path, String filetype) {
		Texture tex = null;
		InputStream in = ResourceLoader.getResourceAsStream(path);
		try {
			tex = TextureLoader.getTexture(filetype, in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tex;
	}

	public static Texture QuickLoadTex(String path) {
		Texture tex = null;
		InputStream in = ResourceLoader.getResourceAsStream("res/" + path + ".png");
		try {
			tex = TextureLoader.getTexture("PNG", in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tex;
	}
	
	public static void DrawHealthBar(float x, float y, float width, float healthPercentage, boolean friendly){
		int BAR_HEIGHT = 6;
		int BAR_OFFSET = 3;
		
		Texture healthBackground = QuickLoadTex("healthBackground");
		Texture healthBorder = QuickLoadTex("healthBorder");
		Texture healthForegroundGreen = QuickLoadTex("healthForeground");
		Texture healthForegroundRed = QuickLoadTex("healthForegroundRed");

		x -= width/2;
		y -= (BAR_OFFSET + BAR_HEIGHT/2);

		DrawQuadTex(healthBackground, x, y, width, BAR_HEIGHT);
		if (friendly)
			DrawQuadTex(healthForegroundGreen, x, y, width * healthPercentage, BAR_HEIGHT);
		else
			DrawQuadTex(healthForegroundRed, x, y, width * healthPercentage, BAR_HEIGHT);
		DrawQuadTex(healthBorder, x, y, width, BAR_HEIGHT);
	}
}
