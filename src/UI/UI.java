package UI;

import java.util.ArrayList;
import org.lwjgl.input.Mouse;
import static helpers.Artist.*;

public class UI {

	private ArrayList<Button> buttonList;
//	private boolean isLeftButtonDown = false;
	
	public UI(){
		buttonList = new ArrayList<Button>();
	}
	
	public void addButton(String name, String textureName, int x, int y){
		buttonList.add(new Button(name, QuickLoadTex(textureName), x, y));
	}
	
	public void draw(){
		for (Button b: buttonList){
			DrawQuadTex(b.getTexture(), b.getX(), b.getY(), b.getWidth(), b.getHeight());
		}
	}
	
	public boolean isButtonClicked(String buttonName){
		Button b = getButton(buttonName);
		float mouseY = HEIGHT - Mouse.getY() - 1;
		if (Mouse.getX() > b.getX() && Mouse.getX()< b.getX()+ b.getWidth() &&
				mouseY > b.getY() && mouseY < b.getY() + b.getHeight()){
			return true;
		}
//		isLeftButtonDown = Mouse.isButtonDown(0);
		return false;
	}
	
	private Button getButton(String buttonName){
		for (Button b: buttonList){
			if (b.getName().equals(buttonName))
				return b;
		}
		return null;
	}
}
