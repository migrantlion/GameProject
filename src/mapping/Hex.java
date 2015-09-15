package mapping;

import static helpers.Artist.*;

import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;

import buildings.Tower;
import troops.Trooper;


public class Hex {
	private float x, y; // center
	private float face;  // perpendicular distance from center to face edge
	private Texture texture;
	private HexType type;
	
	boolean buildable;

	// other buffs go here.  These are for example.  Not used yet.
	boolean passable = true;
	boolean flyOver = true;
	float moveModify = 0;
	
//	// who is in the Hex
//	private ArrayList<Tower> towerList;
//	private ArrayList<Trooper> soldiers;
//
	
	public Hex(float x, float y, float face, HexType type) {
		this.x = x;
		this.y = y;
		this.face = face;
		this.type = type;
		this.texture = QuickLoadTex(type.textureName);
		this.buildable = type.buildable;
		this.passable = type.passable;
		this.flyOver = type.flyOver;
		this.moveModify = type.moveModify;
//		
//		this.towerList = new ArrayList<Tower>();
//		this.soldiers = new ArrayList<Trooper>();
	}
	
	public void draw(){
		DrawHexTex(texture,x,y,face);
	}
	
//	public void update(){
//		ArrayList<Tower> towerDiscards = new ArrayList<Tower>();
//		for (Tower tower : towerList)
//			if (!tower.isAlive())
//				towerDiscards.add(tower);
//		
//		ArrayList<Trooper> trooperDiscards = new ArrayList<Trooper>();
//		for (Trooper soldier : soldiers)
//			if (!soldier.isAlive() || soldier.getCurrentHex().x != x || soldier.getCurrentHex().y != y)
//				trooperDiscards.add(soldier);
//		
//		for (Tower t : towerDiscards)
//			towerList.remove(t);
//		for (Trooper t : trooperDiscards)
//			soldiers.remove(t);
//	}

//	public void addTower(Tower tower){
//		towerList.add(tower);
//	}
//	
//	public void addTrooper(Trooper unit){
//		soldiers.add(unit);
//	}
//	
//	public ArrayList<Tower> getTowers(){
//		return towerList;
//	}
//	
//	public ArrayList<Trooper> getTroopers(){
//		return soldiers;
//	}
//	
//	public ArrayList<Trooper> getFriendlyTroopers(boolean friendly){
//		ArrayList<Trooper> friendlies = new ArrayList<Trooper>();
//		
//		for (Trooper soldier : soldiers)
//			if (soldier.isFriendly() == friendly)
//				friendlies.add(soldier);
//		
//		return friendlies;
//	}
//	
//	public ArrayList<Tower> getFriendlyTowers(boolean friendly){
//		ArrayList<Tower> friendlies = new ArrayList<Tower>();
//		
//		for (Tower tower : towerList)
//			if (tower.isFriendly() == friendly)
//				friendlies.add(tower);
//		
//		return friendlies;
//	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getFace() {
		return face;
	}

	public Texture getTexture() {
		return texture;
	}

	public HexType getType() {
		return type;
	}

	public void setType(HexType type) {
		this.type = type;
		this.texture = QuickLoadTex(type.textureName);
	}

	public boolean isBuildable() {
		return buildable;
	}

	public void setBuildable(boolean buildable) {
		this.buildable = buildable;
	}

	public boolean isPassable() {
		return passable;
	}

	public void setPassable(boolean passable) {
		this.passable = passable;
	}

	public boolean isFlyOver() {
		return flyOver;
	}

	public void setFlyOver(boolean flyOver) {
		this.flyOver = flyOver;
	}

	public float getMoveModify() {
		return moveModify;
	}

	public void setMoveModify(float moveModify) {
		this.moveModify = moveModify;
	}
}
