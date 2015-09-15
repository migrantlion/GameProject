package buildings;

import troops.Trooper;

public class ProjectileIceBall extends Projectile {

	public ProjectileIceBall(ProjectileType shot, Trooper target, float x, float y, boolean friendly) {
		super(shot, target, x, y, friendly);
	}
	
	@ Override
	public void onHit(){
		float enemyspeed = super.getTarget().getSpeed();
		super.getTarget().setSpeed(9*enemyspeed/10);
		super.onHit();
	}

}
