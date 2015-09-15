package mapping;

import java.util.ArrayList;

public class HexMath {

	public static boolean isInHex(float x, float y, Hex hex){
		double angle = 2*Math.PI/6; 

		// find coordinates of upper right corner of hex
		float vert = hex.getFace();
		float horiz = (float) (vert / (Math.tan(angle)));
		
		// reflect x,y in space of hex to query point
		float qx = Math.abs(x - hex.getX());
		float qy = Math.abs(y - hex.getY());
		
		//if length of point q is beyond radius of hex, then it is not inside and can stop here
		if ((qx*qx + qy*qy) > 4*horiz*horiz)
			return false;
		
		// take dot product between normal vector from p2 to p3 (hex point on x-axis) and p2 to (x,y)
		// after point x,y is reflected to first quadrant and expressed from center of hex
		float d = 2*vert*horiz - vert*qx - horiz*qy;
		
		// if determinant is positive, p2-->(x,y) is in same dir as normal, so point is inside hex
		if (d >= 0)
			return true;
		else
			return false;
	}
	
	public static ArrayList<Hex> getHexesInRadius(int steps, Hex centerHex, HexGrid gridMap){
		float radius = 2*centerHex.getFace()*steps;  // distance between hex centers
		float x, y, distance;
		ArrayList<Hex> hexesInRange = new ArrayList<Hex>();
		
		if (steps < 0)
			return hexesInRange;  // empty list
		else if (steps == 0) { 
			hexesInRange.add(centerHex);
			return hexesInRange;  // return just center hex
		} else {
			// cut down search space
			int[] centerNdx = new int[2];
			centerNdx = gridMap.getHexGridIndex(centerHex.getX(), centerHex.getY());
			int minRow = Math.max(0, centerNdx[0] - 2*steps);
			int maxRow = Math.min(centerNdx[0] + 2*steps, gridMap.getHexesHigh());
			int minCol = Math.max(0,  centerNdx[1] - 2*steps);
			int maxCol = Math.min(centerNdx[1] + 2*steps, gridMap.getHexesWide());
			
			for (int row = minRow; row < maxRow; row++)
				for (int col = minCol; col < maxCol; col++) {
					Hex h = gridMap.getHex(row, col);
					x = h.getX() - centerHex.getX();
					y = h.getY() - centerHex.getY();
					distance = (float) Math.sqrt(x*x + y*y);
					if (distance <= radius)
						hexesInRange.add(h);
				}
			return hexesInRange;
		}
	}
	
	public static float getDistance(float a, float b, float x, float y){
		return (float) Math.sqrt((a-x)*(a-x) + (b-y)*(b-y));
	}
}
