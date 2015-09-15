package mapping;

public enum HexType {
	Grass("grass", true, true, -5), 
	Dirt("mud", false, true), 
	Water("water", false, false), 
	Selected("bullet32", false, false), 
	NULL("purple", false, false);
	
	String textureName;
	boolean buildable;

	// other buffs go here.  These are for example.  Not used yet.
	boolean passable = true;
	boolean flyOver = true;
	float moveModify = 0;
	
	HexType(String textureName, boolean buildable, boolean passable){
		this.textureName = textureName;
		this.buildable = buildable;
		this.passable = passable;
	}

	HexType(String textureName, boolean buildable, boolean passable, float moveModify){
		this.textureName = textureName;
		this.buildable = buildable;
		this.passable = passable;
		this.moveModify = moveModify;
	}

	public boolean isBuildable() {
		return buildable;
	}

	public void setBuildable(boolean buildable) {
		this.buildable = buildable;
	}

	public boolean isPassable() {
		return passable;
	}

	public void setPassable(boolean passable) {
		this.passable = passable;
	}

	public boolean isFlyOver() {
		return flyOver;
	}

	public void setFlyOver(boolean flyOver) {
		this.flyOver = flyOver;
	}

	public float getMoveModify() {
		return moveModify;
	}

	public void setMoveModify(float moveModify) {
		this.moveModify = moveModify;
	}
	
}
