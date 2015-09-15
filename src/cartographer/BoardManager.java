package cartographer;

import java.util.ArrayList;

import buildings.Tower;
import mapping.HexGrid;
import player.Player;
import spawners.SpawnCircle;
import troops.Trooper;

public class BoardManager {

	private ArrayList<SpawnCircle> allCircles;
	private ArrayList<Tower> allTowers;
	private ArrayList<Trooper> allTroopers, roninTroopers, circleTroopers;
	private HexGrid board;
	private Player player, opponent;
	
	public BoardManager(HexGrid board){
		this.board = board;
		this.allCircles = new ArrayList<SpawnCircle>();
		this.allTowers = new ArrayList<Tower>();
		this.allTroopers = new ArrayList<Trooper>();
		this.roninTroopers = new ArrayList<Trooper>();
		this.circleTroopers = new ArrayList<Trooper>();
		this.player = new Player(board, true);
	}
	
	public void update(){
		ArrayList<Trooper> troopSub = new ArrayList<Trooper>();
		ArrayList<Tower> towerSub = new ArrayList<Tower>();
		ArrayList<SpawnCircle> circleSub = new ArrayList<SpawnCircle>();
		
		for (SpawnCircle c : allCircles){
			c.update();
			if (c.isFriendly() == player.isFriendly())
				circleSub.add(c);
		}
		rebuildTroopList();
		for (Tower t : allTowers) {
			t.update(allTroopers);
			if (t.isFriendly() == player.isFriendly())
				towerSub.add(t);
		}
		for (Trooper t : allTroopers) {
			t.update(allTroopers, allTowers);
			if (t.isFriendly() == player.isFriendly())
				troopSub.add(t);
		}
		removeDeadTowers();

		player.selectTrooper(roninTroopers.get(0));
		player.update(troopSub, towerSub, circleSub);
		
		registerNewEntities();
	}
	
	private void registerNewEntities(){
		if (player.isRegistering()){
			if (player.isRegisteringTrooper())
				roninTroopers.add(player.getCreatedTrooper());
			else if (player.isRegisteringTower())
				allTowers.add(player.getCreatedTower());
			else
				allCircles.add(player.getCreatedCircle());
			
			player.clearRegister();
		}
	}
	
	private void removeDeadTowers(){
		ArrayList<Tower> discards = new ArrayList<Tower>();
		
		for (Tower t : allTowers)
			if (!t.isAlive())
				discards.add(t);
		
		for (Tower t : discards)
			allTowers.remove(t);
	}
	
	private void rebuildTroopList(){
		// first remove dead Ronin troopers
		ArrayList<Trooper> discards = new ArrayList<Trooper>();
		for (Trooper t : roninTroopers)
			if (!t.isAlive())
				discards.add(t);
		for (Trooper t : discards)
			roninTroopers.remove(t);
		
		// now get all the troops from the circles
		circleTroopers.clear();
		for (SpawnCircle c : allCircles)
			circleTroopers.addAll(c.getTroopList());
		
		// now combine both lists to get them all
		allTroopers.clear();
		allTroopers.addAll(roninTroopers);
		allTroopers.addAll(circleTroopers);
	}
	
	public void addCircle(SpawnCircle spawner){
		if (allCircles == null)
			allCircles = new ArrayList<SpawnCircle>();
		allCircles.add(spawner);
		spawner.getStartHex().setBuildable(false);
	}
	
	public void addTower(Tower tower){
		if (allTowers == null)
			allTowers = new ArrayList<Tower>();
		allTowers.add(tower);
		tower.getLocationHex().setBuildable(false);
	}
	
	public void addTrooper(Trooper troop){
		if (roninTroopers == null)
			roninTroopers = new ArrayList<Trooper>();
		roninTroopers.add(troop);
	}
	
	public void setBoard(HexGrid newBoard){
		this.board = newBoard;
	}
}