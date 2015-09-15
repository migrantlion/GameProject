package editor;

import static helpers.Artist.HEIGHT;
import static editor.MapIO.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import mapping.Hex;
import mapping.HexGrid;
import mapping.HexType;

public class Editor {

	private HexGrid grid;
	private int index;
	private HexType[] types;

	public Editor() {
		this.grid = new HexGrid();
		this.index = 0;
		this.types = new HexType[3];
		this.types[0] = HexType.Grass;
		this.types[1] = HexType.Dirt;
		this.types[2] = HexType.Water;
	}

	public void update() {
		grid.draw();
		
		// Handle mouse inputs
		if (Mouse.isButtonDown(0)) {
			SetHex();
		}
		
		// draw a highlight hex over where the mouse is
		highlightMouse();

		// Handle keyboard inputs
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT && Keyboard.getEventKeyState()) {
				MoveIndex();
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_S && Keyboard.getEventKeyState()) {
				saveHexMap("saveTest", grid);
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_L && Keyboard.getEventKeyState()) {
				grid = loadHexMap("saveTest");
			}
		}
	}

	private void SetHex() {
		// need improved method to pick which hex you are on
		float x = Mouse.getX();
		float y = HEIGHT - Mouse.getY() -1;
		
		int[] point = grid.getHexGridIndex(x, y);
		if (point[0] >= 0 && point[1] >=0)
			grid.setHex(point[0], point[1], types[index]);
	}
	
	private void highlightMouse(){
		float x = Mouse.getX();
		float y = HEIGHT - Mouse.getY() -1;
		
		int[] point = grid.getHexGridIndex(x, y);
		Hex hex = new Hex(grid.getHex(point[0], point[1]).getX(),
				grid.getHex(point[0], point[1]).getY(),
				grid.getHex(point[0], point[1]).getFace(), 
				HexType.Selected);
		hex.draw();
	}
	
	private void MoveIndex() {
		index++;
		if (index > types.length - 1) {
			index = 0;
		}

	}
}
