package spawners;

import mapping.Hex;
import mapping.HexGrid;
import troops.GroundTrooper;
import troops.SapperTrooper;
import troops.TrooperType;

public class GruntCircle extends SpawnCircle{

	public GruntCircle(SpawnCircleType circleType, TrooperType unitType, Hex startHex, Hex destinationHex,
			HexGrid hexMap, boolean friendly) {
		super(circleType, unitType, startHex, destinationHex, hexMap, friendly);
	}

	@Override
	public void spawn() {
		for (int i = 0; i < troopsPerSpawn; i++)
			troopList.add(new SapperTrooper(unitType, startHex, hexMap, destinationHex, friendly));	
	}

}
