package editor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import mapping.Hex;
import mapping.HexGrid;
import mapping.HexType;

public class MapIO {
	public static void saveHexMap(String mapName, HexGrid grid) {
		String mapData = "";
		for (int i = 0; i < grid.getHexesHigh(); i++) {
			for (int j = 0; j < grid.getHexesWide(); j++) {
				mapData += getHexID(grid.getHex(i, j));
			}
		}

		try {
			File file = new File(mapName);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(mapData);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static HexGrid loadHexMap(String mapName) {
		HexGrid grid = new HexGrid();
		try {
			BufferedReader br = new BufferedReader(new FileReader(mapName));
			String data = br.readLine();
			for (int i = 0; i < grid.getHexesHigh(); i++) {
				for (int j = 0; j < grid.getHexesWide(); j++) {
					grid.setHex(i, j,
							getHexType(data.substring(i * grid.getHexesWide() + j, i * grid.getHexesWide() + j + 1)));
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return grid;
	}

	public static HexType getHexType(String ID) {
		HexType type = HexType.NULL;

		switch (ID) {
		case "0":
			type = HexType.Grass;
			break;
		case "1":
			type = HexType.Dirt;
			break;
		case "2":
			type = HexType.Water;
			break;
		case "3":
			type = HexType.NULL;
			break;
		default:
			// error condition
			type = HexType.Selected;
			break;
		}
		return type;
	}

	public static String getHexID(Hex t) {
		String ID = "!"; // error character

		switch (t.getType()) {
		case Grass:
			ID = "0";
			break;
		case Dirt:
			ID = "1";
			break;
		case Water:
			ID = "2";
			break;
		case NULL:
			ID = "3";
			break;
		case Selected:
			break;
		default:
			break;
		}
		return ID;
	}
}
