package buildings;

import static helpers.Artist.DrawQuadTex;
import static helpers.Artist.DrawQuadTexRot;
import static helpers.Artist.DrawHealthBar;
import static helpers.Clock.deltaTime;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.opengl.Texture;

import helpers.Entity;
import mapping.Hex;
import mapping.HexGrid;
import mapping.HexMath;
import troops.Trooper;

public abstract class Tower implements Entity{
	private float timeSinceLastShot, firingSpeed, angle;
	private float x, y;
	private int width, height;
	private Trooper target;
	private CopyOnWriteArrayList<Trooper> enemies;
	private boolean targeted;
	private ProjectileType shotType;
	private ArrayList<Hex> targetZone;
	public ArrayList<Projectile> projectiles;
	
	private Texture[] textures;
	public TowerType towerType;
	private float health, startingHealth;
	private boolean alive = true;  // can be destroyed, but still standing.
	private boolean destroyed = false;
	private boolean friendly;
	
	private Hex locationHex;
	protected ArrayList<Trooper> allTroops;
	
	public Tower(TowerType towerType, ProjectileType shotType,  
			Hex posHex, HexGrid hexMap, boolean friendly){
		this.locationHex = posHex;
		this.x = posHex.getX();
		this.y = posHex.getY();
		this.towerType = towerType;
		this.textures = towerType.textures;
		this.startingHealth = towerType.health;
		this.health = startingHealth;
		this.width = towerType.width;
		this.height = towerType.height;
		this.friendly = friendly;

		this.firingSpeed = towerType.firingSpeed;
		this.shotType = shotType;
		
		this.enemies = new CopyOnWriteArrayList<Trooper>();
		this.targeted = false;
		this.timeSinceLastShot = 0f;
		this.projectiles = new ArrayList<Projectile>();
		this.angle = 0f;
		
		this.allTroops = new ArrayList<Trooper>();
		setupTargetZone(shotType.range, hexMap);
	}
	
	private void setupTargetZone(int range, HexGrid hexMap){
		this.targetZone = new ArrayList<Hex>();
		
		targetZone = HexMath.getHexesInRadius(range, hexMap.getCurrentHex(x, y), hexMap);
	}
	
	private Trooper acquireTarget() {
		Trooper closestEnemy = null;
		float closestDistance = 99999;
		for (Trooper e : enemies) {
			if (findDistance(e) < closestDistance && e.isAlive()) {
				closestDistance = findDistance(e);
				closestEnemy = e;
			}
		}

		if (closestEnemy != null)
			targeted = true;
		else
			targeted = false;

		return closestEnemy;
	}

	private boolean isInRange(Trooper e) {
		boolean inZone = false;
		
		for (Hex h : targetZone)
			if (e.getCurrentHex() == h){
				inZone = true;
				break;
			}
		
		return inZone;
	}
	
	private boolean isInZone(float x, float y){
		boolean inZone = false;
		
		for (Hex h : targetZone)
			if (HexMath.isInHex(x, y, h)){
				inZone = true;
				break;
			}
		
		return inZone;
	}
	
	private float findDistance(Trooper e) {
		float xDistance = Math.abs(e.getX() - x);
		float yDistance = Math.abs(e.getY() - y);

		return (float) Math.sqrt(xDistance*xDistance + yDistance*yDistance);
	}

	private float calculateAngle() {
		double angleTemp = Math.atan2(target.getY() - y, target.getX() - x);
		return (float) Math.toDegrees(angleTemp) - 90;
	}

	public abstract void shoot (Trooper target);

	private void updateEnemies(){
		enemies.clear();

		if (allTroops != null)
			for (Hex hex : targetZone)
				enemies.addAll(getFriendlyTroopersInHex(hex, !friendly));
		
	}
	
	private ArrayList<Trooper> getFriendlyTroopersInHex(Hex hex, boolean friendly){
		ArrayList<Trooper> troopsInHex = new ArrayList<Trooper>();
		
		if (allTroops != null)
			for (Trooper trooper : allTroops){
				if (trooper.getCurrentHex() == hex  && trooper.isFriendly() == friendly)
					troopsInHex.add(trooper);
			}
		return troopsInHex;
	}
	
	public void update(ArrayList<Trooper> troops) {
		allTroops.clear();
		allTroops.addAll(troops);
		if (!destroyed) {
			updateEnemies();
			updateTarget();
		}
		
		ArrayList<Projectile> deadShells = null;
		for (Projectile p : projectiles){
			p.update();
			if (!isInZone(p.getX(),p.getY()) || !p.isAlive()) {
				if (p.isAlive())
					p.destroy();
				if (deadShells == null)
					deadShells = new ArrayList<Projectile>();
				deadShells.add(p);
			}
		}
		if (deadShells != null)
			for (Projectile dud : deadShells)
				projectiles.remove(dud);
		
		draw();
		// memory conservation
		allTroops.clear();  
	}
	
	private void updateTarget(){
		if (firingSpeed == 0)
			return;
		
		if (!targeted) {
			target = acquireTarget();
		} else if (!isInRange(target)){
			targeted = false;
			target = acquireTarget();
		}
		else if (timeSinceLastShot > firingSpeed) {
			timeSinceLastShot = 0;
			shoot(target);
		}
		
		if (target == null || target.isAlive() == false)
			targeted = false;
		else
			angle = calculateAngle();
		
		timeSinceLastShot += deltaTime();
	}

	public void draw() {
		float healthPercentage = health/startingHealth;
		
		if (!destroyed){
			DrawQuadTex(textures[0], x, y, width, height, true);
			if (textures.length > 1)
				for (int i = 1; i < textures.length; i++)
					DrawQuadTexRot(textures[i], x, y, width, height, angle, true);
			
			if (healthPercentage < 1){
				DrawHealthBar(x, y, width, healthPercentage, friendly);
			}
		}
		if (destroyed && alive) {
			// draw a rubble image
		}
	}
	
	public void takeDamage(int amount){
		health -= amount;
		if (health <= 0) {
			destroyed = true;
			alive = false;
		}
	}
	
	public float getAngle() {
		return angle;
	}

	public Trooper getTarget() {
		return target;
	}

	public void setFiringSpeed(float firingSpeed) {
		this.firingSpeed = firingSpeed;
	}

	public void setShotType(ProjectileType shotType) {
		this.shotType = shotType;
	}

	public void setTargetZone(int range, HexGrid hexMap) {
		setupTargetZone(range, hexMap);
	}
	
	public ProjectileType getShotType(){
		return shotType;
	}

	public float getX() {
		return x;
	}
	
	public void setX(float value){
		this.x = value;
	}

	public float getY() {
		return y;
	}

	public void setY(float value){
		this.y = value;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
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
	
	public boolean isDestroyed(){
		return destroyed;
	}

	public Hex getLocationHex() {
		return locationHex;
	}
	
	public Texture[] getTextures(){
		return textures;
	}
}
