package troops;

import java.util.ArrayList;

import buildings.Tower;
import mapping.Hex;
import mapping.HexGrid;
import mapping.HexMath;

public class SapperTrooper extends Trooper {

	private int inflictTowerDamage;
	private Tower targetTower = null;
	
	public SapperTrooper(TrooperType type, Hex startHex, HexGrid hexMap, Hex destinationHex, boolean friendly) {
		super(type, startHex, hexMap, destinationHex, friendly);
		this.inflictTowerDamage = type.inflictTowerDamage;
	}

	@Override
	protected void updateFighting() {
		// if don't inflict Tower damage, don't fight them
		if (inflictTowerDamage > 0)
			updateTowerAttack();
		
		// if don't inflict enemy damage, don't fight them
		if (inflictDamage > 0 && targetTower == null)
				super.fightEnemyTrooper();
	}

	@Override
	protected void scanForEnemy() {
		// if don't inflict Tower damage, don't look for them
		if (inflictTowerDamage > 0)
			seeIfTowerNear();
		
		// if don't inflict enemy damage, don't look to fight them
		if (inflictDamage > 0)
			// Sappers look for towers first.  Only then look to enemies
			if (!chasingEnemy && targetTower == null)
				super.seeIfEnemyNear();
	}
	
	@Override
	protected void attack(Trooper enemy){
		attacking = true;
		if (timeSinceLastAttack > attackSpeed){
			enemy.takeDamage(inflictDamage);
			timeSinceLastAttack = 0;
		}
		if (!enemy.isAlive()){
			attacking = false;
		}
			
	}
	
	private void updateTowerAttack(){
		if (targetTower == null) {
			if (attacking) {  // if we were attacking, but now there is no target
				attacking = false;
				// roll back to milestone before attack
				for (int Ndx = milestones.size() - 1; Ndx > milestoneIndexBeforeAttack; Ndx--)
					milestones.remove(Ndx);
			}
			targetTower = getEnemyTower(rangeAttack);
		} else {  // we do have a target
			if (!super.isInHexRange(rangeAttack, targetTower)){
				// lost him for attack
				attacking = false;
				// clear the target unless chase is using it
				if (!chasingEnemy)
					targetTower = null;
			} else {
				// if we weren't yet attacking, save what we were doing before we attacked
				if (!attacking)
					milestoneIndexBeforeAttack = milestones.size()-1;
				// then attack
				attackTower(targetTower);
			}
		}
		if (targetTower != null) {
			attacking = true;
			targetPos = targetTower.getLocationHex();
		}
	}
	
	private void seeIfTowerNear(){
		if (targetTower == null){
			if (chasingEnemy)
				chasingEnemy = false;
			targetTower = getEnemyTower(rangeEnemyDetection);
		} else {
			if (!isInHexRange(rangeEnemyDetection, targetTower)){
				// lost him for chase
				chasingEnemy = false;
				// clear the target unless attack is using it.
				if (!attacking)
					targetTower = null;

			}
		}
		if (targetTower != null) {
			chasingEnemy = true;
			targetPos = targetTower.getLocationHex();
		}
	}

	private void attackTower(Tower enemy) {
		attacking = true;
		if (timeSinceLastAttack > attackSpeed){
			enemy.takeDamage(inflictTowerDamage);
			timeSinceLastAttack = 0;
		}
		if (!enemy.isDestroyed()){
			attacking = false;
			targetTower = null;
		}
	}

	private Tower getEnemyTower(int range) {
		ArrayList<Tower> enemies = new ArrayList<Tower>();
		Tower target = null;
		
		for (Hex h : HexMath.getHexesInRadius(range, super.getCurrentHex(), hexMap))
			enemies.addAll(super.getFriendlyTowersInHex(h,!friendly));
		
		float closestDistance = 999999f;
		float distance;
		for (Tower enemy : enemies) {
			distance = HexMath.getDistance(enemy.getX(), enemy.getY(), super.getX(), super.getY());
			if (distance < closestDistance){
				closestDistance = distance;
				target = enemy;
			}
		}
		return target;
	}

}
