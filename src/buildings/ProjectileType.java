package buildings;

import static helpers.Artist.QuickLoadTex;

import org.newdawn.slick.opengl.Texture;

public enum ProjectileType {
	BBGun(QuickLoadTex("bullet32"), 1, 200, 1, 10, 10),
	Rifle(QuickLoadTex("bullet32"), 2, 300, 2, 10, 10),
	IceBall(QuickLoadTex("bullet32ice"), 0, 400, 5, 10, 10),
	None(null, 0, 0, 0, 0, 0);
	
	int damage, range, width, height;
	float speed;
	Texture texture;
	
	ProjectileType(Texture texture, int damage, float speed, int hexRange, int width, int height){
		this.texture = texture;
		this.damage = damage;
		this.speed = speed;
		this.range = hexRange;
		this.width = width;
		this.height = height;
	}
	
}
