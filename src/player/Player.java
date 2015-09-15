package player;

import static helpers.Artist.HEIGHT;
import static helpers.Clock.*;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import buildings.ProjectileType;
import buildings.SentryPost;
import buildings.Tower;
import buildings.TowerType;
import mapping.Hex;
import mapping.HexGrid;
import mapping.HexType;
import menus.PopUpManager;
import spawners.GruntCircle;
import spawners.SpawnCircle;
import spawners.SpawnCircleType;
import troops.GroundTrooper;
import troops.Trooper;
import troops.TrooperType;

public class Player {
	private HexGrid board;
	
	private Trooper selectedTrooper;
	private Hex selectedHex, currentHex;
	
	private boolean friendly;
	private boolean mouseDown = false;
	private boolean keyDown = false;
	private boolean popUpActive = false;
	private float pauseTimer = 0;
	private final float PAUSETIME = 1.0f;
	
	private PopUpManager menu;
	
	private Tower createdTower;
	private boolean registeringTower = false;
	private Trooper createdTrooper;
	private boolean registeringTrooper = false;
	private SpawnCircle createdCircle;
	private boolean registeringCircle = false;

	public Player(HexGrid board, boolean friendly){
		this.board = board;
		this.friendly = friendly;
		highlightHex();
	}
	
	public boolean isRegistering(){
		return (registeringTower || registeringCircle || registeringTrooper);
	}
	
	public void update(ArrayList<Trooper> troops, ArrayList<Tower> towers, ArrayList<SpawnCircle> circles){
//		setUpMenu(troops, towers, circles);
		if (!popUpActive)
			highlightHex();
		
		inputListener();
	}
	
	private void inputListener(){
		float x = Mouse.getX();
		float y = HEIGHT - Mouse.getY() -1;
	
		if (Mouse.isButtonDown(0) && !mouseDown){
			setNewDirection(x, y);
			mouseDown = true;
		} else
			mouseDown = false;
		if (!isRegistering() && !keyDown){
			if (Keyboard.isKeyDown(Keyboard.KEY_A) && currentHex.isPassable()){
				this.createdTrooper = new GroundTrooper(TrooperType.Grunt, currentHex, board, currentHex, friendly);
				this.registeringTrooper = true;
				keyDown = true;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_S) && currentHex.isBuildable()){
				this.createdCircle = new GruntCircle(SpawnCircleType.BasicCircle, TrooperType.Grunt, currentHex, currentHex, board, friendly);
				this.registeringCircle = true;
				currentHex.setBuildable(false);
				keyDown = true;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_D) && currentHex.isBuildable()){
				this.createdTower = new SentryPost(TowerType.SniperBunker, ProjectileType.Rifle, currentHex, board, friendly);
				this.registeringTower = true;
				currentHex.setBuildable(false);
				keyDown = true;
			}
		}
		
		if (keyDown){
			pauseTimer += deltaTime();
			if (pauseTimer > PAUSETIME) {
				pauseTimer = 0;
				keyDown = false;
			}
		}
		
		
//		if (Mouse.isButtonDown(1) && !mouseDown){
//			popUpSelector(x, y);
//			mouseDown = true;
//		} else
//			mouseDown = false;
	}
	
	private void setNewDirection(float x, float y){
		selectedTrooper.setDestinationHex(board.getCurrentHex(x, y));
	}
	
	private void highlightHex(){
		float x = Mouse.getX();
		float y = HEIGHT - Mouse.getY() -1;

		currentHex = board.getCurrentHex(x, y);
//		DrawHexOutline(x,y,currentHex.getFace());
		int[] point = board.getHexGridIndex(x, y);
		Hex hex = new Hex(board.getHex(point[0], point[1]).getX(),
				board.getHex(point[0], point[1]).getY(),
				board.getHex(point[0], point[1]).getFace(), 
				HexType.Selected);
		hex.draw();
	}
	
	private void setUpMenu(ArrayList<Trooper> troops, ArrayList<Tower> towers, ArrayList<SpawnCircle> circles){
		this.menu = new PopUpManager(getMyTroopersInHex(currentHex,troops), 
				getMyTowersInHex(currentHex,towers), getMyCirclesInHex(currentHex, circles), currentHex);
	}
	
	public boolean isFriendly(){
		return friendly;
	}
	
	public void selectTrooper(Trooper trooper){
		selectedTrooper = trooper;
	}

	public Trooper getSelectedTrooper() {
		return selectedTrooper;
	}

	public Tower getCreatedTower() {
		return createdTower;
	}

	public boolean isRegisteringTower() {
		return registeringTower;
	}

	public Trooper getCreatedTrooper() {
		return createdTrooper;
	}

	public boolean isRegisteringTrooper() {
		return registeringTrooper;
	}

	public SpawnCircle getCreatedCircle() {
		return createdCircle;
	}

	public boolean isRegisteringCircle() {
		return registeringCircle;
	}
	
	public void clearRegister(){
		registeringCircle = false;
		registeringTrooper = false;
		registeringTower = false;
		createdTower = null;
		createdTrooper = null;
		createdCircle = null;
	}
	
	private ArrayList<Tower> getMyTowersInHex(Hex hex, ArrayList<Tower> towers){
		ArrayList<Tower> towersInHex = new ArrayList<Tower>();
		
		for (Tower t : towers){
			if (t.isFriendly() == friendly && board.getCurrentHex(t.getX(), t.getY()) == currentHex)
				towersInHex.add(t);
		}
		return towersInHex;
	}
	
	private ArrayList<Trooper> getMyTroopersInHex(Hex hex, ArrayList<Trooper> troops){
		ArrayList<Trooper> troopsInHex = new ArrayList<Trooper>();
		
		for (Trooper t : troops){
			if (t.isFriendly() == friendly && board.getCurrentHex(t.getX(), t.getY()) == currentHex)
				troopsInHex.add(t);
		}
		return troopsInHex;
	}

	private ArrayList<SpawnCircle> getMyCirclesInHex(Hex hex, ArrayList<SpawnCircle> circles){
		ArrayList<SpawnCircle> circlesInHex = new ArrayList<SpawnCircle>();
		
		for (SpawnCircle t : circles){
			if (t.isFriendly() == friendly && board.getCurrentHex(t.getX(), t.getY()) == currentHex)
				circlesInHex.add(t);
		}
		return circlesInHex;
	}

}
