package troops;

import mapping.Hex;
import mapping.HexGrid;

public class GroundTrooper extends Trooper {

	public GroundTrooper(TrooperType type, Hex startHex, HexGrid hexMap, Hex destinationHex, boolean friendly) {
		super(type, startHex, hexMap, destinationHex, friendly);
	}

	@Override
	protected void updateFighting() {
		super.fightEnemyTrooper();
	}

	@Override
	protected void scanForEnemy() {
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
}
