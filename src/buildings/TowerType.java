package buildings;

import org.newdawn.slick.opengl.Texture;
import static helpers.Artist.QuickLoadTex;

public enum TowerType {

	SniperBunker(new Texture[]{QuickLoadTex("cannonBase"),QuickLoadTex("cannonGun")}, 20, ProjectileType.Rifle, 1, 32, 32),
	CannonBlue(new Texture[]{QuickLoadTex("blueCannonBase"),QuickLoadTex("blueCannonGun")}, 20, ProjectileType.BBGun, 1, 32, 32),
	CannonIce(new Texture[]{QuickLoadTex("iceCannonBase"),QuickLoadTex("iceGun")}, 20, ProjectileType.IceBall, 2, 32, 32),
	Bunker(new Texture[]{QuickLoadTex("cannonBase")}, 10, ProjectileType.None, 0, 32, 32);
	
	Texture[] textures;
	int width, height, troopsPerSpawn;
	float firingSpeed;
	float health;
	float spawnSpeed;
	ProjectileType shotType;
	
	TowerType(Texture[] textures, float health, ProjectileType shotType, float firingSpeed, int width, int height){
		this.textures = textures;
		this.shotType = shotType;
		this.health = health;
		this.firingSpeed = firingSpeed;
		this.width = width;
		this.height = height;
	}
}
