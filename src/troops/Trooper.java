package troops;


import static helpers.Artist.DrawHealthBar;
import static helpers.Artist.DrawQuadTex;
import static helpers.Artist.HEIGHT;
import static helpers.Artist.WIDTH;
import static helpers.Clock.deltaTime;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

import buildings.Tower;
import helpers.Entity;
import mapping.Hex;
import mapping.HexGrid;
import mapping.HexMath;

public abstract class Trooper implements Entity{
	private static float CLOSE = 3f;
	private int width, height;
	private float x, y, speed; 
	private int health, startHealth;
	private Texture texture;
	
	protected int inflictDamage;
	private Hex destinationHex;
	private Hex currentHex;
	protected HexGrid hexMap;
	private boolean alive = true;
	private boolean isAtAttention = false;
	protected boolean friendly;
	
	protected boolean chasingEnemy = false;
	private Trooper targetEnemy = null;
	protected Hex targetPos = null;
	protected int rangeEnemyDetection;
	protected int rangeAttack;
	protected int milestoneIndexBeforeAttack;
	protected float attackSpeed;
	protected float timeSinceLastAttack;
	protected boolean attacking = false;
	
	protected ArrayList<Hex> milestones;
	protected ArrayList<Trooper> otherTroops;
	protected ArrayList<Tower> towerList;
	
	public Trooper(TrooperType type, Hex startHex, HexGrid hexMap, Hex destinationHex, boolean friendly){
		this.texture = type.texture;
		this.friendly = friendly;
		this.currentHex = startHex;
		this.destinationHex = destinationHex;
		this.hexMap = hexMap;
		this.width = type.width;
		this.height = type.height;
		
		this.speed = type.speed;
		
		this.health = type.startHealth;
		this.startHealth = type.startHealth;
		this.inflictDamage = type.inflictDamage;
		
		this.rangeAttack = type.rangeAttack;
		this.rangeEnemyDetection = type.rangeEnemyDetection;
		this.attackSpeed = type.attackSpeed;
		
		this.x = startHex.getX() + CLOSE;
		this.y = startHex.getY() + CLOSE;
		this.alive = true;

		this.milestones = new ArrayList<Hex>();
		milestones.add(destinationHex);
		
		this.towerList = new ArrayList<Tower>();
		this.otherTroops = new ArrayList<Trooper>();
	}
	
	public void update(ArrayList<Trooper> troops, ArrayList<Tower> towers){
		otherTroops.clear();
		otherTroops.addAll(troops);
		towerList.clear();
		towerList.addAll(towers);
		
		timeSinceLastAttack += deltaTime();
		updateFighting();
		scanForEnemy();
		move();
		checkCollisions();
		if (currentHex != hexMap.getCurrentHex(x, y)){
			currentHex = hexMap.getCurrentHex(x, y);
		}
		draw();
		// memory conservation
		otherTroops.clear();
		towerList.clear();
	}
	
	private void move(){
		float currentSpeed = speed + currentHex.getMoveModify();
		Vector2f walkDir = new Vector2f(0,0);
		
		if (!isAtAttention){
			if (milestones.size() > 0) {
				if (attacking || chasingEnemy)
					if (milestones.get(milestones.size()-1) != targetPos)
						milestones.add(targetPos);
				walkDir = getNextPath(milestones.get(milestones.size()-1));
			} else {
				if (attacking || chasingEnemy){ // since no milestone, need to add one
					milestones.add(targetPos);
					walkDir = getNextPath(targetPos);
				}
			}
			x += deltaTime() * walkDir.x * currentSpeed;
			y += deltaTime() * walkDir.y * currentSpeed;
		}

		if (milestones.size() > 0) {
			if (Math.abs(x - currentHex.getX()) < CLOSE && Math.abs(y - currentHex.getY()) < CLOSE)
				milestones.remove(currentHex);
			else if (walkDir.x == 0 && walkDir.y == 0 && (!attacking || !chasingEnemy))  // stuck
				milestones.remove(milestones.size()-1);
		}
		
		// don't go off map
		if (x < 0)
			x = 0;
		if (x > WIDTH)
			x = WIDTH;
		if (y < 0)
			y = 0;
		if (y > HEIGHT)
			y = HEIGHT;
	}
	
	public void draw() {
		float healthPercentage = ((float) health)/((float) startHealth);
		DrawQuadTex(texture, x, y, width, height, true);
		
		if (healthPercentage < 1){
			DrawHealthBar(x, y - height/2, width, healthPercentage, friendly);
		}
	}
	
	protected abstract void updateFighting();
	
	protected void fightEnemyTrooper(){
		if (targetEnemy == null) {
			if (attacking) {  // if we were attacking, but now there is no target
				attacking = false;
				// roll back to milestone before attack
				for (int Ndx = milestones.size() - 1; Ndx > milestoneIndexBeforeAttack; Ndx--)
					milestones.remove(Ndx);
			}
			targetEnemy = getEnemyTarget(rangeAttack);
		} else {
			if (!isInHexRange(rangeAttack, targetEnemy)){
				// lost him
				attacking = false;
				// clear enemy unless chasing is using it
				if (!chasingEnemy)
					targetEnemy = null;
			} else {
				// if we weren't yet attacking, save what we were doing before we attacked
				if (!attacking)
					milestoneIndexBeforeAttack = milestones.size()-1;
				attack(targetEnemy);
			}
		}
		if (targetEnemy != null){
			targetPos = targetEnemy.currentHex;
		}
	}
	
	protected abstract void scanForEnemy();
	
	protected void seeIfEnemyNear(){
		if (targetEnemy == null){
			if (chasingEnemy)
				chasingEnemy = false;
			targetEnemy = getEnemyTarget(rangeEnemyDetection);
		} else {
			if (!isInHexRange(rangeEnemyDetection, targetEnemy)){
				// lost him
				chasingEnemy = false;
				// clear target unless attacking is using it
				if (!attacking)
					targetEnemy = null;
			}
		}
		if (targetEnemy != null) {
			chasingEnemy = true;
			targetPos = targetEnemy.currentHex;
		}
	}
	
	protected abstract void attack(Trooper enemy);
	
	private Trooper getEnemyTarget(int range){
		ArrayList<Trooper> enemies = new ArrayList<Trooper>();
		Trooper target = null;
		
		for (Hex h : HexMath.getHexesInRadius(range, currentHex, hexMap))
			enemies.addAll(getFriendlyTroopersInHex(h,!friendly));
		
		float closestDistance = 999999f;
		float distance;
		for (Trooper enemy : enemies) {
			distance = HexMath.getDistance(enemy.x, enemy.y, x, y);
			if (distance < closestDistance){
				closestDistance = distance;
				target = enemy;
			}
		}
		return target;
	}
	
	private ArrayList<Trooper> getAllTroopersInHex(Hex hex){
		ArrayList<Trooper> troopsInHex = new ArrayList<Trooper>();
		
		for (Trooper trooper : otherTroops){
			if (trooper != this  && trooper.getCurrentHex() == hex)
				troopsInHex.add(trooper);
		}
		return troopsInHex;
	}

	private ArrayList<Trooper> getFriendlyTroopersInHex(Hex hex, boolean friendly){
		ArrayList<Trooper> troopsInHex = new ArrayList<Trooper>();
		
		for (Trooper trooper : otherTroops){
			if (trooper != this  && trooper.getCurrentHex() == hex  && trooper.isFriendly() == friendly)
				troopsInHex.add(trooper);
		}
		return troopsInHex;
	}
	
	private ArrayList<Tower> getAllTowersInHex(Hex hex){
		ArrayList<Tower> towerInHex = new ArrayList<Tower>();
		
		for (Tower tower : towerList){
			if (tower.getLocationHex() == hex)
				towerInHex.add(tower);
		}
		return towerInHex;
	}

	protected ArrayList<Tower> getFriendlyTowersInHex(Hex hex, boolean friendly){
		ArrayList<Tower> towerInHex = new ArrayList<Tower>();
		
		for (Tower tower : towerList){
			if (tower.getLocationHex() == hex  && tower.isFriendly() == friendly)
				towerInHex.add(tower);
		}
		return towerInHex;
	}
	
	private boolean isInHexRange(int range, Trooper target){
		if (range < 0)
			return false;
		
		ArrayList<Trooper> enemies = new ArrayList<Trooper>();
		for (Hex h : HexMath.getHexesInRadius(range, currentHex, hexMap))
			enemies.addAll(getFriendlyTroopersInHex(h, !friendly));
		
		boolean found = false;
		for (Trooper enemy : enemies)
			if (enemy == target){
				found = true;
				break;
			}
		
		return found;
	}

	protected boolean isInHexRange(int range, Tower target){
		if (range < 0)
			return false;
		
		ArrayList<Tower> enemies = new ArrayList<Tower>();
		for (Hex h : HexMath.getHexesInRadius(range, currentHex, hexMap))
			enemies.addAll(getFriendlyTowersInHex(h, !friendly));
		
		boolean found = false;
		for (Tower enemy : enemies)
			if (enemy == target){
				found = true;
				break;
			}
		
		return found;
	}
	
	public void chaseEnemy(Trooper enemy){
		this.chasingEnemy = true;
		this.targetEnemy = enemy;
	}
	
	public void takeDamage(int amount) {
		health -= amount;
		if (health <= 0) {
			health = 0;
			die();
		}
	}

	private void die() {
		alive = false;
	}
	
	public Hex getCurrentHex() {
		return currentHex;
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

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getInflictDamage() {
		return inflictDamage;
	}

	public void setInflictDamage(int inflictDamage) {
		this.inflictDamage = inflictDamage;
	}

	public Hex getDestinationHex() {
		return destinationHex;
	}

	public void setDestinationHex(Hex destinationHex) {
		this.destinationHex = destinationHex;
		this.milestones.clear();
		this.milestones.add(destinationHex);
	}
	
	public void addDestinationHex(Hex nextdestination){
		ArrayList<Hex> temp = new ArrayList<Hex>();
		
		if (milestones.size() > 0) {
			for (Hex h:milestones)
				temp.add(h);
			milestones.clear();
			milestones.add(nextdestination);
			for (Hex h : temp)
				milestones.add(h);
		} else
			this.milestones.add(nextdestination);
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isAtAttention() {
		return isAtAttention;
	}

	public void setAtAttention(boolean isAtAttention) {
		this.isAtAttention = isAtAttention;
	}
	
	private void checkCollisions(){
		float boundingRadius = ((float) width + (float) height)/4;
		float otherRadius, distance;
		
		// there are others in this hex with us
		for (Trooper t : getAllTroopersInHex(currentHex)){
			if (t != this){  // don't collide with yourself
				otherRadius = ((float)t.getWidth() + (float)t.getHeight())/4;
				distance = HexMath.getDistance(x, y, t.getX(), t.getY()) - (otherRadius + boundingRadius);
				if ( distance < 0){
					//collision!
					Vector2f offset = new Vector2f(t.getX()- x, t.getY() - y);
					if (offset.x == 0 && offset.y == 0) {
						// right on top of each other, so move back 1/2 width and 1/2 height
						x -= width/2;
						y -= height/2;
					} else {
						offset = offset.normalise(offset);
						//move back
						x += offset.x * distance;
						y += offset.y * distance;
					}
				}
			}
		}
		for (Tower t : getAllTowersInHex(currentHex)){
			otherRadius = ((float)t.getWidth() + (float)t.getHeight())/4;
			distance = HexMath.getDistance(x, y, t.getX(), t.getY()) - (otherRadius + boundingRadius);
			if ( distance < 0){
				//collision!
				Vector2f offset = new Vector2f(t.getX()- x, t.getY() - y);
				offset = offset.normalise(offset);
				//move back
				x += offset.x * distance;
				y += offset.y * distance;
			}
		}
	}

	private Vector2f getNextPath(Hex finish){
		Vector2f walkDir = new Vector2f(finish.getX()-x, finish.getY()-y);
		
		if (currentHex == finish) {
			// no need to check other hexes, we are in the hex we want.  Just need to get to the center.
			if (walkDir.x == 0 && walkDir.y == 0)
				return walkDir;
			else
				walkDir = walkDir.normalise(walkDir);
			return walkDir;
		}
		
		// can we walk this way?
		Hex nextHex = getNextHex(walkDir,0);
		if (!nextHex.isPassable()) {
			nextHex = getNextHex(walkDir,1);
			if (nextHex.isPassable()) {
				walkDir = new Vector2f(nextHex.getX()-x,nextHex.getY()-y);
				if (nextHex != finish)
					milestones.add(nextHex);
			} else {
				nextHex = getNextHex(walkDir,2);
				if (nextHex.isPassable()) {
					walkDir = new Vector2f(nextHex.getX()-x,nextHex.getY()-y);
					if (nextHex != finish)
						milestones.add(nextHex);
				} else {
					walkDir = new Vector2f(0,0);  // no way to go but backwards
					milestones.clear();  // since stuck.  stop trying to walk places.
					return walkDir;
				}
			}
		}
		// since walkDir is not zero, we should normalize it.
		walkDir = walkDir.normalise(walkDir);
		if (nextHex != finish)
			milestones.add(nextHex);
		return walkDir;
	}
	
	private Hex getNextHex(Vector2f walkDir, int nearestQuad){
		double walkAngle;
		double HEXANGLE = 2*Math.PI/6;
		
		int[] index = new int[2];
		index = hexMap.getHexGridIndex(x, y);
		
		// note in coordinate system y is negative going up, so need to flip sign of y
		walkAngle = Math.atan2(-walkDir.y, walkDir.x);
		if (walkAngle < 0)
			walkAngle += 2*Math.PI;
		
		int quadrant = (int) Math.floor(walkAngle/HEXANGLE);
		boolean upperLower = ((walkAngle % HEXANGLE) > 0.5f * HEXANGLE);
		
		// if nearest == 0, then want the Hex in the direction given by quadrant
		if (nearestQuad == 1) {
			// next nearest direction.  modify quadrant by upper/lower
			if (upperLower)
				quadrant += 1;
			else
				quadrant -= 1;
		} else if (nearestQuad == 2){
			// go the other way
			if (upperLower)
				quadrant -= 1;
			else
				quadrant += 1;
		}
		// adjust around the hex if needed
		if (quadrant > 5)
			quadrant = 0;
		if (quadrant < 0)
			quadrant = 5;

		int[] modify = quadIndexLookup(quadrant);
		index[0] += modify[0];
		index[1] += modify[1];
		return hexMap.getHex(index[0], index[1]);
	}
	
	private int[] quadIndexLookup(int quadrant){
		int[] answer = new int[2];
		int[] index = new int[2];
		index = hexMap.getHexGridIndex(x, y);
		
		if ((index[1] & 1) == 0) {
			//even columns
			switch (quadrant){
			case 0:
				answer[0] = 0;
				answer[1] = 1;
				break;
			case 1:
				answer[0] = -1;
				answer[1] = 0;
				break;
			case 2:
				answer[0] = 0;
				answer[1] = -1;
				break;
			case 3:
				answer[0] = 1;
				answer[1] = -1;
				break;
			case 4:
				answer[0] = 1;
				answer[1] = 0;
				break;
			case 5:
				answer[0] = 1;
				answer[1] = 1;
				break;
			default:
				answer[0] = 0;
				answer[1] = 0;
				break;
			}
		} else {
			//odd columns
			switch (quadrant){
			case 0:
				answer[0] = -1;
				answer[1] = 1;
				break;
			case 1:
				answer[0] = -1;
				answer[1] = 0;
				break;
			case 2:
				answer[0] = -1;
				answer[1] = -1;
				break;
			case 3:
				answer[0] = 0;
				answer[1] = -1;
				break;
			case 4:
				answer[0] = 1;
				answer[1] = 0;
				break;
			case 5:
				answer[0] = 0;
				answer[1] = 1;
				break;
			default:
				answer[0] = 0;
				answer[1] = 0;
				break;
			}
		}
		return answer;
	}

	public boolean isFriendly() {
		return friendly;
	}

	public void setFriendly(boolean state) {
		this.friendly = state;
	}

	public boolean isChasingEnemy() {
		return chasingEnemy;
	}

	public void setChasingEnemy(boolean chasingEnemy) {
		this.chasingEnemy = chasingEnemy;
	}

	public Trooper getTargetEnemy() {
		return targetEnemy;
	}

	public void setTargetEnemy(Trooper targetEnemy) {
		this.targetEnemy = targetEnemy;
	}

	public int getRangeEnemyDetection() {
		return rangeEnemyDetection;
	}

	public int getRangeAttack() {
		return rangeAttack;
	}
	
	public Texture getTexture(){
		return texture;
	}
	
}
