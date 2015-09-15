package troops;

import static helpers.Artist.QuickLoadTex;

import org.newdawn.slick.opengl.Texture;

public enum TrooperType {

	Grunt(QuickLoadTex("bullet32"), 32, 32, 20, 5, 
			1, 1.5f, 1, 0, 1),
	ShockTroop(QuickLoadTex("bullet32ice"), 32, 32, 25, 8, 
			3, 2.0f, 1, 0, 1),
	SuperGrunt(QuickLoadTex("face"), 25, 25, 20, 6, 
			1, 1.8f, 1, 0, 2),
	Sapper(QuickLoadTex("purple"), 20, 20, 25, 5, 
			0, 1.5f, 2, 1, 5);
	
	int width, height;
	float speed;
	int inflictDamage, inflictTowerDamage, startHealth;
	float attackSpeed;
	Texture texture; 
	
	int rangeEnemyDetection;
	int rangeAttack;

	TrooperType(Texture texture, int width, int height, float speed, int startHealth, 
			int inflictDamage, float attackSpeed, int rangeDetect, int rangeAttack, int inflictTowerDamage){
		this.texture = texture;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.inflictDamage = inflictDamage;
		this.inflictTowerDamage = inflictTowerDamage;
		this.attackSpeed = attackSpeed;
		this.rangeAttack = rangeAttack;
		this.rangeEnemyDetection = rangeDetect;
		this.startHealth = startHealth;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public Texture getTexture() {
		return texture;
	}

}
