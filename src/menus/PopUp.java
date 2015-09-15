package menus;

import static helpers.Artist.HEIGHT;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import helpers.EquiTriangle;

public abstract class PopUp {

	protected boolean selected = false;
	protected boolean clickedMe = false;
	protected ArrayList<PopUp> submenu;
	protected EquiTriangle triangle;
	
	public PopUp(float x, float y, float radians, float size){
		setVariables(x,y,radians,size);
	}
	
	private void setVariables(float x, float y, float radians, float size){
		this.triangle = new EquiTriangle(new Vector2f(x,y), size, radians);
		this.selected = false;
		this.submenu = null;
	}

	public abstract void draw();

	protected void mouseListener(){
		// check to see if the user clicked the mouse button
		// if it is left click over the popUp, then it is selected
		// if there are no submenus, then it is also clicked
		
		
		
	}
	
	protected boolean hasSubMenu(){
		if (submenu == null)
			return false;
		else if (submenu.size() > 0)
			return true;
		else
			return false;
	}
	
	public boolean isClickedMe(){
		return clickedMe;
	}
	
	public boolean isSelected(){
		if (selected && hasSubMenu())
			return selected;
		else {
			selected = collisionMousePopup();
		}
		return selected;
	}
	
	public void deSelect(){
		selected = false;
	}
	
	public void addSubMenu(PopUp popup){
		if (submenu == null)
			submenu = new ArrayList<PopUp>();
		
		// center the submenu in this popup's triangle
		popup.triangle.translate(triangle.getCenter());
		
		submenu.add(popup);
		setSubmenuAngles();
	}
	
	private void setSubmenuAngles(){
		double ANGLE = Math.PI/3;
		int sector = 0;
		for (PopUp p : submenu){
			p.triangle.rotate(sector*ANGLE);
			sector++;
		}
	}
	
	public ArrayList<PopUp> getSubmenu(){
		return submenu;
	}
	
	private boolean collisionMousePopup(){
		float mousex = Mouse.getX();
		float mousey = HEIGHT - Mouse.getY() - 1;
		
		return triangle.PointInTriangle(mousex, mousey);
	}	
}
