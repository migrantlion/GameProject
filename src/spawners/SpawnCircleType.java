package spawners;

import org.newdawn.slick.opengl.Texture;
import static helpers.Artist.*;

public enum SpawnCircleType {

	BasicCircle(new Texture[]{QuickLoadTex("basicCircle"),QuickLoadTex("basicCircleFlare")}, 
			HEX_SIZE, HEX_SIZE, 1, 100, 10);

	Texture[] textures;
	int width, height, troopsPerSpawn;
	float health;
	float spawnSpeed;
	
	SpawnCircleType(Texture[] textures, int width, int height, int troopsPerSpawn, float health, float spawnSpeed) {
		this.textures = textures;
		this.width = width;
		this.height = height;
		this.troopsPerSpawn = troopsPerSpawn;
		this.health = health;
		this.spawnSpeed = spawnSpeed;
	}

	
}
