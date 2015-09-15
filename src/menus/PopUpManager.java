package menus;

import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;

import buildings.Tower;
import mapping.Hex;
import spawners.SpawnCircle;
import troops.Trooper;
import static helpers.Artist.*;

public class PopUpManager {

	private final String ADDMENU = "addButton";
	private final String SELECTMENU = "selectButton";
	
	private Hex hex;
	private ArrayList<PopUp> menu;
	private ArrayList<Tower> towers;
	private ArrayList<Trooper> troopers;
	private ArrayList<SpawnCircle> circles;
	private boolean menuActivated;
	
	public PopUpManager(ArrayList<Trooper> troopers, ArrayList<Tower> towers, ArrayList<SpawnCircle> circles, Hex hex){
		this.troopers = troopers;
		this.towers = towers;
		this.circles = circles;
		this.hex = hex;
		this.menuActivated = false;
		this.menu = new ArrayList<PopUp>();
		setupMenu();
	}
	
	public void update(){
//		mouseListener();
		if (menuActivated) {
			
		}
	}
	
	private void setupMenu(){
		double ANGLE = Math.PI/3;
		int step = 0, index = 0;
		
		// set the add button up
		menu.add(new SelectorPopUp(QuickLoadTex(ADDMENU), hex.getX(), hex.getY(), 0, hex.getFace()));
		step++;
	}
}
