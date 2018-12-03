package game.scenes.game;

import static game.scenes.game.GameAssets.*;

import java.util.ArrayList;

import cnge.core.BlockSet;
import cnge.core.level.Level;
import cnge.core.morph.Morph;
import game.MelonBlock;
import game.scenes.game.entities.Melon;

abstract public class MelonLevel extends Level<MelonMap, MelonBlock> {
	
	public static final int VALUE_TOP = 1;
	
	/*
	 * contains wall data
	 */
	public int[][] values;
	
	public int[][] melonSpawns;
	public int[][] carrotSpawns;
	
	public int numMelons;
	public int numCarrots;
	
	public MelonLevel(String ip, BlockSet<MelonBlock> bs) {
		super(ip, bs);
	}
	
	public int makeValueUp(int value) {
		return value |= 128;
	}
	
	public int makeValueRight(int value) {
		return value |= 64;
	}
	
	public int makeValueDown(int value) {
		return value |= 32;
	}
	
	public int makeValueLeft(int value) {
		return value |= 16;
	}
	
	public static boolean isUpWall(int wallValue) {
		return (wallValue & 128) == 128;
	}
	
	public static boolean isRightWall(int wallValue) {
		return (wallValue & 64) == 64;
	}
	
	public static boolean isDownWall(int wallValue) {
		return (wallValue & 32) == 32;
	}
	
	public static boolean isLeftWall(int wallValue) {
		return (wallValue & 16) == 16;
	}
	
	public void onLevelLoad() {
		int w = tiles[0].length;
		int h = tiles[0][0].length;
		
		values = new int[w][h];
		
		ArrayList<int[]> tempMelons = new ArrayList<int[]>();
		ArrayList<int[]> tempCarrots = new ArrayList<int[]>();
		
		for(int i = 0; i < w; ++i) {
			for(int j = 0; j < h; ++j) {
				
				MelonBlock bl = (blockSet.get(tiles[0][i][j]));
				if(bl.solid) {
					
					int wallValue = 0;
					if(j != 0 && (!(blockSet.get(tiles[0][i][j - 1])).solid || tiles[0][i][j - 1] == BLOCK_BREAKABLE) ) {
						wallValue = makeValueUp(wallValue);
					}
					if(i != w - 1 && (!(blockSet.get(tiles[0][i + 1][j])).solid || tiles[0][i + 1][j] == BLOCK_BREAKABLE) ) {
						wallValue = makeValueRight(wallValue);
					}
					if(j != h - 1 && (!(blockSet.get(tiles[0][i][j + 1])).solid || tiles[0][i][j + 1] == BLOCK_BREAKABLE) ) {
						wallValue = makeValueDown(wallValue);
					}
					if(i != 0 && (!(blockSet.get(tiles[0][i - 1][j])).solid || tiles[0][i - 1][j] == BLOCK_BREAKABLE) ) {
						wallValue = makeValueLeft(wallValue);
					}
					values[i][j] = wallValue;
				}
				
				int entity = entityTiles[0][i][j];
				if(entity != -1) {
					if(entity == ENTITY_MELON_REGULAR) {
						tempMelons.add(new int[] {i * 32, j * 32, Melon.TYPE_REGULAR});
					} else if (entity == ENTITY_MELON_MINI) {
						tempMelons.add(new int[] {i * 32, j * 32, Melon.TYPE_MINI});
					} else if (entity == ENTITY_MELON_HUGE) {
						tempMelons.add(new int[] {i * 32, j * 32, Melon.TYPE_HUGE});
					} else if (entity == ENTITY_CARROT) {
						tempCarrots.add(new int[] {i * 32, j * 32});
					}
				}
			}
		}
		
		//un temporalize our carrot and melon spawns
		
		numMelons = tempMelons.size();
		melonSpawns = new int[numMelons][3];
		melonSpawns = tempMelons.toArray(melonSpawns);
		
		numCarrots = tempCarrots.size();
		carrotSpawns = new int[numCarrots][2];
		carrotSpawns = tempCarrots.toArray(carrotSpawns);
	}
	
	public MelonMap mapCreate(int i, Object... params) {
		return new MelonMap(values);
	}

}