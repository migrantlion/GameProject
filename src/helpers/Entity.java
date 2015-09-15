package helpers;

public interface Entity {
	public float getX();
	public float getY();
	public void setX(float x);
	public void setY(float y);
	public int getWidth();
	public int getHeight();
	public void setWidth(int width);
	public void setHeight(int height);
	public void draw();
	public boolean isFriendly();
	public void setFriendly(boolean state);
	public boolean isAlive();
}
