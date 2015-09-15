package buildings;

import mapping.Hex;
import mapping.HexGrid;
import troops.Trooper;

public class SentryPost extends Tower {

	public SentryPost(TowerType type, ProjectileType shotType,Hex posHex, HexGrid hexMap, boolean friendly) {
		super(type, shotType, posHex, hexMap, friendly);
	}

	@Override
	public void shoot(Trooper target) {
		super.projectiles.add(new ProjectileOneShot(super.getShotType(), super.getTarget(), super.getX(), super.getY(),true));
	}

}
