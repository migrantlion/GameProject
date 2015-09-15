package menus;

import org.newdawn.slick.opengl.Texture;
import static helpers.Artist.*;
import buildings.Tower;

public class TowerPopUp extends PopUp{

	private Tower tower;
	private Texture[] popTex;
	
	public TowerPopUp(Tower tower, float x, float y, float radians, float size) {
		super(x, y, radians, size);
		this.tower = tower;
		this.popTex = tower.getTextures();
	}

	@Override
	public void draw() {
		DrawTriangleTex(popTex[0], triangle);
	}
	
	public Tower getTower(){
		return tower;
	}

}
