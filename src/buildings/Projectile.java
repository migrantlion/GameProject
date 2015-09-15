package buildings;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

import helpers.Entity;
import troops.Trooper;

import static helpers.Clock.*;
import static helpers.Artist.*;

public abstract class Projectile implements Entity {

	private Texture texture;
	private float x, y, speed;
	private Vector2f velocityDir;
	private int damage;
	private int width, height;
	private Trooper target;
	private boolean alive;
	private boolean friendly;

	public Projectile(ProjectileType shot, Trooper target, float x, float y, boolean friendly) {
		this.texture = shot.texture;
		this.target = target;
		this.alive = true;
		this.x = x;
		this.y = y;
		this.width = shot.width;
		this.height = shot.height;
		this.speed = shot.speed;
		this.damage = shot.damage;
		this.friendly = friendly;
		this.velocityDir = new Vector2f(0,0);

		calculateDirection();
	}

	private void calculateDirection() {
		velocityDir = new Vector2f(target.getX() - x, target.getY() - y);
		velocityDir = velocityDir.normalise(velocityDir);
	}
	
	public void onHit() {
		target.takeDamage(damage);
		alive = false;
	}

	public void update() {
		if (alive) {
			x += deltaTime() * velocityDir.x * speed;
			y += deltaTime() * velocityDir.y * speed;

			if (CheckCollision(x, y, width, height, target.getX(), target.getY(), target.getWidth(),
					target.getHeight())) {
				onHit();
			}
			draw();
		}
	}
	
	public void destroy(){
		alive = false;
		// TODO: can put some animation here
	}

	public void draw() {
		DrawQuadTex(texture, x, y, width, height, true);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public Trooper getTarget() {
		return target;
	}

	public void setTarget(Trooper target) {
		this.target = target;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public boolean isFriendly() {
		return friendly;
	}

	public void setFriendly(boolean state) {
		this.friendly = state;
	}
}
