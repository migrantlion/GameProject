package spawners;

import static helpers.Artist.DrawQuadTex;
import static helpers.Artist.DrawHealthBar;
import static helpers.Clock.deltaTime;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.opengl.Texture;

import helpers.Entity;
import mapping.Hex;
import mapping.HexGrid;
import troops.Trooper;
import troops.TrooperType;

public abstract class SpawnCircle implements Entity{
	private float FLARE_DURATION = 0.25f;
	
	private float x, y;
	private int width, height;
	private Texture[] textures;
	
	private float timeSinceLastSpawn, spawnTime;
	protected TrooperType unitType;
	protected Hex startHex, destinationHex;
	protected HexGrid hexMap;
	protected ArrayList<Trooper> troopList;
	protected int troopsPerSpawn;
	private boolean paused = false;

	private boolean destroyed = false, alive = true;
	private float health, startingHealth;
	protected boolean friendly;
	
	public SpawnCircle(SpawnCircleType circleType, TrooperType unitType, 
			Hex startHex, Hex destinationHex, HexGrid hexMap, boolean friendly) {
		this.x = startHex.getX();
		this.y = startHex.getY();
		this.width = circleType.width;
		this.height = circleType.height;
		this.textures = circleType.textures;
		
		this.friendly = friendly;
		this.health = circleType.health;
		this.startingHealth = health;
		
		this.unitType = unitType;
		this.spawnTime = circleType.spawnSpeed;
		this.timeSinceLastSpawn = 0;
		this.troopsPerSpawn = circleType.troopsPerSpawn;

		this.troopList = new ArrayList<Trooper>();
		this.startHex = startHex;
		this.destinationHex = destinationHex;
		this.hexMap = hexMap;
	}

	public void update() {
		// if you don't spawn troops, then do nothing.
		draw();
		
		if (troopsPerSpawn < 1)
			return;
		
		timeSinceLastSpawn += deltaTime();
		if ((timeSinceLastSpawn > spawnTime) && !paused) {
			spawn();
			timeSinceLastSpawn = 0;
		}

		ArrayList<Trooper> troopCounter = new ArrayList<Trooper>();
		troopCounter.addAll(troopList);
		for (Trooper t : troopCounter) {
			if (!t.isAlive())
				troopList.remove(t);
		}
	}
	
	public void draw(){
		float healthPercentage = health/startingHealth;
		
		if (!destroyed)
			if (spawnTime - timeSinceLastSpawn <= FLARE_DURATION)
				DrawQuadTex(textures[1], x, y, width, height, true);
			else
				DrawQuadTex(textures[0], x, y, width, height, true);
		
		if (healthPercentage < 1)
			DrawHealthBar(x, height, width, healthPercentage, friendly);
		
		if (destroyed && alive){
			// add rubble texture image.
		}
	}

	public abstract void spawn();

	public TrooperType getUnitType() {
		return unitType;
	}

	public void setUnitType(TrooperType unitType) {
		this.unitType = unitType;
	}

	public Hex getStartHex() {
		return startHex;
	}

	public void setStartHex(Hex startHex) {
		this.startHex = startHex;
	}

	public Hex getDestinationHex() {
		return destinationHex;
	}

	public void setDestinationHex(Hex destinationHex) {
		this.destinationHex = destinationHex;
	}

	public ArrayList<Trooper> getTroopList() {
		return troopList;
	}

	public void setSpawnTime(float spawnTime) {
		this.spawnTime = spawnTime;
	}

	public void setTroopsPerSpawn(int troopsPerSpawn) {
		this.troopsPerSpawn = troopsPerSpawn;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isFriendly() {
		return friendly;
	}

	public void setFriendly(boolean state) {
		this.friendly = state;
	}

	public boolean isAlive(){
		return alive;
	}
	
	public Texture getTexture(){
		return textures[0];
	}
}
