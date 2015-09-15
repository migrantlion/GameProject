package data;

import static editor.MapIO.loadHexMap;

import org.newdawn.slick.opengl.Texture;

import buildings.ProjectileType;
import buildings.SentryPost;
import buildings.TowerType;
import cartographer.BoardManager;
import helpers.Artist;
import mapping.HexGrid;
import player.Player;
import spawners.GruntCircle;
import spawners.SpawnCircleType;
import troops.GroundTrooper;
import troops.SapperTrooper;
import troops.Trooper;
import troops.TrooperType;

public class Game {

	private HexGrid grid;
	private BoardManager boardManager;
	private Trooper trooper;
	
	public Game() {
		grid = loadHexMap("saveTest");
		boardManager = new BoardManager(grid);
		trooper = new GroundTrooper(TrooperType.ShockTroop, grid.getHex(10, 2), grid, grid.getHex(10, 2), true);
		
		boardManager.addCircle(new GruntCircle(SpawnCircleType.BasicCircle, TrooperType.Grunt, grid.getHex(8, 1), grid.getHex(7, 6), grid, false));
		boardManager.addTrooper(trooper);
		boardManager.addTower(new SentryPost(TowerType.SniperBunker, ProjectileType.Rifle, grid.getHex(7, 6), grid, true));
		boardManager.addCircle(new GruntCircle(SpawnCircleType.BasicCircle, TrooperType.SuperGrunt, grid.getHex(10, 1), grid.getHex(7, 6), grid, false));
		boardManager.addCircle(new GruntCircle(SpawnCircleType.BasicCircle, TrooperType.SuperGrunt, grid.getHex(2, 8), grid.getHex(7, 6), grid, false));
		boardManager.addCircle(new GruntCircle(SpawnCircleType.BasicCircle, TrooperType.SuperGrunt, grid.getHex(5, 6), grid.getHex(7, 6), grid, false));
		boardManager.addTower(new SentryPost(TowerType.Bunker, ProjectileType.None, grid.getHex(13,5), grid, false));
	} 

	public void update() {
		grid.update();
		boardManager.update();
	}
}
