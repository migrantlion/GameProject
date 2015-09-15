package mapping;

import static helpers.Artist.HEIGHT;
import static helpers.Artist.HEX_SIZE;
import static helpers.Artist.WIDTH;

public class HexGrid {
	private float xStep = (float) Math.sqrt(3) * HEX_SIZE;
	private float yStep = 2 * HEX_SIZE;
	
	public Hex[][] map;
	private int hexesWide, hexesHigh;
	
	public HexGrid(){
		this.hexesWide = (int) Math.floor(WIDTH / xStep) + 1;
		this.hexesHigh = (int) Math.floor(HEIGHT / yStep) + 1;
		this.map = new Hex[hexesHigh][hexesWide];
		
		float x, y;
		for (int row = 0; row < hexesHigh; row++){
			for (int col = 0; col < hexesWide; col++){
				x = col * xStep;
				if ((col & 1)== 1) // is col odd?
					y = yStep * row - 0.5f * yStep;
				else
					y = yStep * row;
				
				map[row][col] = new Hex(x, y, HEX_SIZE, HexType.Dirt);
			}
		}
	}
	
	public void draw(){
		for (int row = 0; row < map.length; row++) {
			for (int col = 0; col < map[row].length; col++) {
				map[row][col].draw();
			}
		}
	}
	
	public void update(){
		for (int row = 0; row < map.length; row++) {
			for (int col = 0; col < map[row].length; col++) {
				map[row][col].draw();
			}
		}
	}
	
	public void setHex(int row, int col, HexType type){
		float x = col * xStep;
		float y;
		if ((col & 1)== 1) // is col odd?
			y = yStep * row - 0.5f * yStep;
		else
			y = yStep * row;
		
		map[row][col] = new Hex(x, y, HEX_SIZE, type);
	}
	
	public Hex getHex(int row, int col){
		if (row < hexesHigh && col < hexesWide && row > -1 && col > -1)
			return map[row][col];
		else
			return new Hex(0, 0, HEX_SIZE, HexType.NULL);
	}
	
	public int[] getHexGridIndex(float x, float y){
		if (x < 0)
			x = 0;
		if (x > WIDTH)
			x = WIDTH;
		if (y < 0)
			y = 0;
		if (y > HEIGHT)
			y = HEIGHT;

		int[] index = {-1, -1};
		int row = 0, col = 0;
		for (row = 0; row < map.length; row++){
			for (col = 0; col < map[row].length; col++){
				if (HexMath.isInHex(x, y, map[row][col])){
					index[0] = row;
					index[1] = col;
					return index;
				}
			}
		}
		System.out.println("didn't find it! x = "+x+"  y = "+y+"  row/col "+row+","+col+"  x0 "+map[row][col].getX());
		return index;
	}
	
	public Hex getCurrentHex(float x, float y){
		int[] index = new int[2];
		index = getHexGridIndex(x, y);
		
		return getHex(index[0],index[1]);
	}

	public int getHexesWide() {
		return hexesWide;
	}

	public int getHexesHigh() {
		return hexesHigh;
	}

	public float getHexXSize(){
		return xStep;
	}
	
	public float getHexYSize(){
		return yStep;
	}
}
