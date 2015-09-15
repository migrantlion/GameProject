package helpers;

import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class EquiTriangle {

	private static final double ANGLE = Math.PI/3;
	
	public Vector2f p1;
	public Vector2f p2;
	public Vector2f p3;
	private Vector2f center = new Vector2f(0,0);
	private float side;
	
	public EquiTriangle(Vector2f bottom, double rotation, float side) {
		this.side = side;

		this.p1 = new Vector2f(0,0);
		this.p2 = new Vector2f(side,0);
		this.p3 = new Vector2f(side/2, (side/2)*(float)Math.tan(ANGLE));
		this.center = new Vector2f(side/2, (side/2)*(float)Math.tan(ANGLE/2));
		
		rotateAboutOrigin(rotation);
		translate(bottom);
	}

	public Vector2f getCenter(){
		return center;
	}
	
	public float getSide(){
		return side;
	}
	
	public void rotate(double radians){
		Vector2f p1Placeholder = new Vector2f(p1.x, p1.y);
		
		//translate to put p1 at origin
		translate(new Vector2f(-p1.x, -p1.y));
		// rotate by radians
		rotateAboutOrigin(radians);
		//translate back
		translate(p1Placeholder);
	}

	private void rotateAboutOrigin(double rotation){
		Matrix2f rotMatrix = new Matrix2f();
		rotMatrix.m00 = rotMatrix.m11 = (float) Math.cos(rotation);
		rotMatrix.m01 = (float) Math.sin(rotation);
		rotMatrix.m10 = (float) -Math.sin(rotation);
		
		p1 = Matrix2f.transform(rotMatrix, p1, p1);
		p2 = Matrix2f.transform(rotMatrix, p2, p2);
		p3 = Matrix2f.transform(rotMatrix, p3, p3);
		center = Matrix2f.transform(rotMatrix, center, center);
	}
	
	public void translate(Vector2f move){
		p1 = Vector2f.add(p1, move, p1);
		p2 = Vector2f.add(p2, move, p2);
		p3 = Vector2f.add(p3, move, p3);
		center = Vector2f.add(center, move, center);
	}
	
	public boolean PointInTriangle(float x, float y){
		Vector2f point = new Vector2f(x,y);
		
		if (SameSide(p1, p2, p3, point) && SameSide(p2, p3, p1, point) && SameSide(p3, p1, p2, point))
			return true;
		else
			return false;
	}
	
	private boolean SameSide(Vector2f A, Vector2f B, Vector2f point1, Vector2f point2){
		// check that point1 and point2 are on the same side of the line A-->B
		Vector3f cp1 = new Vector3f();
		Vector3f cp2 = new Vector3f();
		
		// (A-->B) x (A-->Point1)
		cp1 = Vector3f.cross(new Vector3f(B.x - A.x, B.y - A.y, 0), new Vector3f(point1.x - A.x, point1.y - A.y, 0), cp1);
		// (A-->B) x (A-->Point2)
		cp2 = Vector3f.cross(new Vector3f(B.x - A.x, B.y - A.y, 0), new Vector3f(point2.x - A.x, point2.y - A.y, 0), cp1);
		
		// check that cp1 and cp2 are pointing in the same direction
		if (Vector3f.dot(cp1,cp2) >= 0)
			return true;
		else 
			return false;
	}
}
	
