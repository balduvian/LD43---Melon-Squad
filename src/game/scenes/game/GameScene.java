package game.scenes.game;

import cnge.core.AssetBundle.SceneLoadAction;
import cnge.core.BlockSet;
import cnge.core.morph.CustomMorph;
import cnge.core.morph.Morph;
import cnge.graphics.Transform;
import cnge.core.Scene;
import cnge.core.Timer;
import game.MelonBlock;
import game.scenes.game.entities.Carrot;
import game.scenes.game.entities.CarrotTransition;
import game.scenes.game.entities.Explosion;
import game.scenes.game.entities.Gib;
import game.scenes.game.entities.Melon;
import game.scenes.game.entities.Melon.MelonType;
import game.scenes.game.entities.Selector;
import game.scenes.game.entities.SkyBackground;
import game.scenes.game.entities.TntTimer;
import game.scenes.win.WinScene;

import static game.scenes.game.GameAssets.*;
import static org.lwjgl.glfw.GLFW.*;

public class GameScene extends Scene<GameScene> {
	
	public static final float GRAVITY = 800;
	
	public boolean pressJump;
	public boolean pressUp;
	public boolean pressLeft;
	public boolean pressRight;
	public boolean pressDown;
	public boolean pressDetonate;
	
	//to make sure we can't just hold down on select screen
	public boolean leftLock;
	public boolean rightLock;
	public boolean jumpLock;
	
	public float currentMapHeight;
	public float deathBarrier;
	
	public int currentMelon;
	public int numMelons;
	
	public int numCarrots;
	
	public boolean[] melonsAlive;
	public int melonsLeft;
	public int carrotsLeft;
	
	public float melonCenterX;
	public float melonCenterY;
	
	public int cameraCount;
	public Morph cameraMorph;
	public boolean carrotCamera;
	public boolean melonCamera;
	public boolean playerCamera;
	
	public CustomMorph selectFadeMorph;
	public float selectFade;
	
	public int numExplosions;
	public int numGibs;
	
	public boolean lose;
	public boolean win;
	
	public int levelNumber;
	
	public boolean killSuggest;
	public boolean explosionSuggest;
	
	public GameScene() {
		super(
			new GameAssets(
				
				new SceneLoadAction<GameScene>() {
					public void load(GameScene s) {
						melonDeathTimer = new Timer(
							1.5,
							() -> {
								if(s.carrotsLeft == 0) {
									winLevelTimer.start();
									s.createEntity(carrotTransition = new CarrotTransition(false), 0, 0);
									s.win = true;
								} else if(s.melonsLeft == 0) {
									s.lose();
								} else {
									s.numExplosions = 0;
									s.numGibs = 0;
									s.melonCamera = true;
									s.cameraCount = 0;
									s.playerCamera = false;
									s.melonCameraUpdate();
								}
							}
						);
					}
				},
				
				new SceneLoadAction<GameScene>() {
					public void load(GameScene s) {
						loseLevelTimer = new Timer(
							2,
							() -> {
								//restart ya thot
								s.startMap(currentLevel);
							}
						);
					}
				},
				
				new SceneLoadAction<GameScene>() {
					public void load(GameScene s) {
						bomberTimer = new Timer(
							//time should be reset
							1000,
							() -> { 
								s.killSuggest(true);
							}
						);
					}
				},
				
				new SceneLoadAction<GameScene>() {
					public void load(GameScene s) {
						winLevelTimer = new Timer(
							2,
							() -> {
								++s.levelNumber;
								if(s.levelNumber == gameLevels.length) {
									base.setScene(new WinScene());
									gameSong.stop();
								}else {
									s.startMap(gameLevels[s.levelNumber]);
								}
							}
						);
					}
				},
				
				new SceneLoadAction<GameScene>() {
					public void load(GameScene s) {
						s.levelNumber = 0;
					}
				},
				
				//actually start this shit
				new SceneLoadAction<GameScene>() {
					public void load(GameScene s) {
						gameSong.setVolume(0.85f);
						gameSong.play(true);
						s.startMap(gameLevels[0]);
					}
				}
				
			)
		);
	}
	
	//routine when we start a new map in the scene
	public void startMap(MelonLevel m){
		currentLevel = m;
		
		m.load();
		currentMap = m.createMap(0, 0, 0);
		currentMapHeight = currentMap.getHeight();
		deathBarrier = (currentMapHeight + 4) * 32;
		
		createEntity(skyBackground = new SkyBackground(), 0, 0);
		
		numMelons = currentLevel.numMelons;
		melonsLeft = numMelons;
		melons = new Melon[numMelons];
		selectors = new Selector[numMelons];
		melonsAlive = new boolean[numMelons];
		int[][] melonSpawns = currentLevel.melonSpawns;
		melonCenterX = 0;
		melonCenterY = 0;
		for(int i = 0; i < numMelons; ++i) {
			melonCenterX += melonSpawns[i][0];
			melonCenterY += melonSpawns[i][1];
			//get the type of melon                          VVVVV
			createEntity(melons[i] = new Melon(i, melonSpawns[i][2]), melonSpawns[i][0], melonSpawns[i][1]);
			melonsAlive[i] = true;
		}
		melonCenterX /= numMelons;
		melonCenterY /= numMelons;
		melonCenterY -= 64;
		
		//we have no explosions at this point sir
		numExplosions = 0;
		numGibs = 0;
		
		for(int i = 0; i < numMelons; ++i) {
			//create the selectors above the melons
			createEntity(selectors[i] = new Selector(), melonSpawns[i][0] - ccx(melonCenterX, 32), melonSpawns[i][1] - 48 - ccy(melonCenterY, 32) );
		}
		
		numCarrots = currentLevel.numCarrots;
		carrotsLeft = numCarrots;
		carrots = new Carrot[numCarrots];
		int[][] carrotSpawns = currentLevel.carrotSpawns;
		for(int i = 0; i < numCarrots; ++i) {
			createEntity(carrots[i] = new Carrot(), carrotSpawns[i][0], carrotSpawns[i][1]);
		}
		
		//you cannot win or lose right away, dummy
		lose = false;
		win = false;
		
		setCameraCenter(melonCenterX, melonCenterY);
		
		currentMelon = 0;
		
		melonCamera = false;
		playerCamera = false;
		carrotCamera = true;
		cameraCount = 0;
		focusOnCarrot(0);
		
		selectFade = 0;
		
		num2char(remainingText, 20, 0);
		
		createEntity(carrotTransition = new CarrotTransition(true), 0, 0);
	}
	
	public void killSuggest(boolean e) {
		killSuggest = true;
		explosionSuggest = e;
	}
	
	public void focusOnCarrot(int c) {
		Transform crt = carrots[c].getTransform();
		Transform cat = camera.getTransform();
		
		float x = ccx(crt.x, crt.width);
		float y = ccy(crt.y, crt.height);
		
		cameraMorph = new Morph(cat, Morph.COSINE, 1.5).addPositionX(x).addPositionY(y);
	}
	
	public void focusOnMelons() {
		Transform cat = camera.getTransform();
		
		float x = ccx(melonCenterX, 32);
		float y = ccy(melonCenterY, 32);
		
		cameraMorph = new Morph(cat, Morph.COSINE, 1.5).addPositionX(x).addPositionY(y);
	}
	
	/**
	 * the singular melon
	 */
	public void focusOnMelon(int m) {
		Transform mlt = melons[m].getTransform();
		Transform cat = camera.getTransform();
		
		float x = ccx(mlt.x, mlt.width);
		float y = ccy(mlt.y, mlt.height);
		
		cameraMorph = new Morph(cat, Morph.COSINE, 0.5).addPositionX(x).addPositionY(y);
	}
	
	@Override
	public void update() {	
		eUpdate(skyBackground);
		
		killSuggest = false;
		
		if(carrotCamera) {
			carrotCameraUpdate();
		} else if(melonCamera) {
			melonCameraUpdate();
		} else if(playerCamera) {
			playerCameraUpdate();
		}	
		
		for(int i = 0; i < numGibs; ++i) {
			eUpdate_OS(gibs[i]);
		}
		
		//idles waiting before play round
		for(int i = 0; i < numMelons; ++i) {
			if(melons[i] != null) {
				if(playerCamera) {
					if(i != currentMelon) {
						melons[i].fakeUpdate();
					}
				} else {
					melons[i].fakeUpdate();
				}
			}
		}
		
		//carrot idles
		for(int i = 0; i < numCarrots; ++i) {
			if(carrots[i] != null) {
				if(!playerCamera) {
					carrots[i].fakeUpdate();
				}
			}
		}
		
		eUpdate_S(carrotTransition);
		
		currentMap.onScreenUpdate();
		currentMap.update();
		
		if(killSuggest) {
			die(currentMelon, explosionSuggest, playerMelon.melonType.radius);
		}
	}
	
	public void carrotCameraUpdate() {
		if(window.keyPressed(GLFW_KEY_SPACE)) {
			carrotCamera = false;
			melonCamera = true;
			cameraCount = 0;
		}else {
			if(cameraMorph.update()) {
				++cameraCount;
				//increase our visual carrot counnter
				num2char(remainingText, 20, cameraCount);
				if(cameraCount != numCarrots) {
					focusOnCarrot(cameraCount);
				}else {
					carrotCamera = false;
					melonCamera = true;
					cameraCount = 0;
				}
			}
		}
	}
	
	public void melonCameraUpdate() {
		if(!rightLock) {
			pressRight = window.keyPressed(GLFW_KEY_D);
		} else {
			rightLock = window.keyPressed(GLFW_KEY_D);
		}
		
		if(!leftLock) {
			pressLeft = window.keyPressed(GLFW_KEY_A);
		} else {
			leftLock = window.keyPressed(GLFW_KEY_A);
		}
		
		boolean pressRestart = window.keyPressed(GLFW_KEY_R);
		
		//the initial entry VVV
		if(cameraCount == 0) {
			selectFadeMorph = new CustomMorph(0, 1, Morph.lINEAR, 1.5);
		
			++cameraCount;
			focusOnMelons();
			
			//find the first melon we can get
			currentMelon = 0;
			while (selectors[currentMelon].mode == Selector.MODE_DEAD){
				++currentMelon;
				currentMelon = Math.floorMod(currentMelon, numMelons);
			}
			selectors[currentMelon].mode = Selector.MODE_ACTIVE;
			
			if(window.keyPressed(GLFW_KEY_SPACE)) {
				jumpLock = true;
			}
			
			num2char(remainingText, 20, carrotsLeft);
		}
		if(cameraCount == 1) {
			//fade in our selectors
			selectFade = selectFadeMorph.update();
			for(int i = 0; i < numMelons; ++i) {
				selectors[i].alpha = selectFade;
			}
			
			if(cameraMorph.update()) {
				++cameraCount;
				selectFade = 1;
				for(int i = 0; i < numMelons; ++i) {
					selectors[i].alpha = 1;
				}
			}
		}
		
		if(!lose) {
			if(pressRight) {
				rightLock = true;
				pressRight = false;
				
				selectors[currentMelon].mode = Selector.MODE_IDLE;
				do {
					++currentMelon;
					currentMelon = Math.floorMod(currentMelon, numMelons);
				} while (selectors[currentMelon].mode == Selector.MODE_DEAD);
				selectors[currentMelon].mode = Selector.MODE_ACTIVE;
			}
			
			if(pressLeft) {
				leftLock = true;
				pressLeft = false;
				
				selectors[currentMelon].mode = Selector.MODE_IDLE;
				do {
					--currentMelon;
					currentMelon = Math.floorMod(currentMelon, numMelons);
				} while (selectors[currentMelon].mode == Selector.MODE_DEAD);
				selectors[currentMelon].mode = Selector.MODE_ACTIVE;
			}
			
			if(!jumpLock) {
				if(window.keyPressed(GLFW_KEY_SPACE)) {
					selectFade = 0;
					melonCamera = false;
					playerCamera = true;
					cameraCount = 0;
					//set who our player is
					playerMelon = melons[currentMelon];
					createEntity(tntTimer = new TntTimer(), 0, 0);
				}
			}else {
				jumpLock = window.keyPressed(GLFW_KEY_SPACE);
			}
		}
		
		if(!lose && pressRestart) {
			lose();
		}
		
		loseLevelTimer.update();
	}
	
	public void playerCameraUpdate() {
		//initial entry VVV
		if(cameraCount == 0) {
			++cameraCount;
			focusOnMelon(currentMelon);
		}
		if(cameraCount == 1) {
			if(cameraMorph.update()) {
				++cameraCount;
				playerMelon.controllable = true;
				sparkSound.play(true);
				bomberTimer.setTime(playerMelon.melonType.fuse);
				bomberTimer.start();
				tntTimer.going = true;
			}
		} else if (cameraCount == 2) {
			
			if(!lose) {
				pressJump = window.keyPressed(GLFW_KEY_SPACE);
				pressUp = window.keyPressed(GLFW_KEY_W);
				pressLeft = window.keyPressed(GLFW_KEY_A);
				pressRight = window.keyPressed(GLFW_KEY_D);
				pressDown = window.keyPressed(GLFW_KEY_S);
				pressDetonate = window.keyPressed(GLFW_KEY_LEFT_SHIFT);
			}
			
			boolean pressRestart = window.keyPressed(GLFW_KEY_R);
			
			eUpdate_S(playerMelon);
			
			for(int i = 0; i < numExplosions; ++i) {
				eUpdate_S(explosions[i]);
			}
			
			for(int i = 0; i < numCarrots; ++i) {
				//we need to see if onscreen to do carrot behavior
				//but bullet still needs a finna
				if(carrots[i] != null) {
					carrots[i].onScreenUpdate();
					carrots[i].update();
				}
			}

			if(playerMelon != null) {
				Transform t = playerMelon.getTransform();
				setCameraCenter(t.x + 16, t.y + 16);
			}
			
			cameraDownLimit(deathBarrier);
			
			num2char(remainingText, 20, carrotsLeft);
			
			if(pressDetonate) {
				bomberTimer.forceEnd();
			}
			
			eUpdate(tntTimer);

			loseLevelTimer.update();
			winLevelTimer.update();
			melonDeathTimer.update();
			bomberTimer.update();
			
			if(!lose && pressRestart) {
				sparkSound.stop();
				lose();
			}
		}
		
		//cameraDownLimit(currentMapHeight * 32);
		//cameraLeftLimit(0);
	}
	
	public int[] getPath(int n, int r) {
		int[] ret = new int[r];
		for(int i = 0; i < r; ++i) {
			ret[i] = Math.floorMod( (int)Math.floor( n / Math.pow(4, i) ), 4);
		}
		return ret;
	}
	
	public class PathThread implements Runnable {
		
		private boolean[][] spaces;
		private int originX;
		private int originY;
		private int radius;
		private int start;
		private int end;
		
		public PathThread(boolean[][] p, int r, int x, int y, int s, int e) {
			spaces = p;
			radius = r;
			originX = x;
			originY = y;
			start = s;
			end = e;
		}
		
		public void run() {
			for(int i = start; i < end; ++i) {
				
				BlockSet<MelonBlock> set = currentMap.getBlockSet();
				
				int[] path = getPath(i, radius);
				
				int tempX = 0;
				int tempY = 0;
				
				for(int j = 0; j < radius; ++j) {
					switch(path[j]) {
						case 0:
							++tempX;
							break;
						case 1:
							--tempX;
							break;
						case 2:
							++tempY;
							break;
						case 3:
							--tempY;
							break;
					}
					int blockId = currentMap.mapAccess(originX + tempX, originY + tempY);
					if( (set.get(blockId)).solid) {
						//we extend explosion into breakable blocks
						if(blockId == BLOCK_BREAKABLE) {
							spaces[radius + tempX][radius + tempY] = true;
						}
						break;
					} else {
						spaces[radius + tempX][radius + tempY] = true;
					}
				}
				
			}
		}
		
	}
	
	public void lose() {
		loseLevelTimer.start();
		createEntity(carrotTransition = new CarrotTransition(false), 0, 0);
		lose = true;
	}
	
	public void die(int mIndex, boolean explode, int radius) {
		
		int carrotsKilled = 0;
		int blocksKilled = 0;
		
		float[][] carrotP = null;
		float[][] blockP = null;
		
		Transform t = playerMelon.getTransform();
		float x = t.x + 16;
		float y = t.y + 16;
		
		if(explode) {
			
			explosionSound.play(false);
			
			//TESTING GROUND_HIGHTLY EXPERIMENTAL
			
			int spaceSize = radius * 2 + 1;
			boolean[][] spaces = new boolean[spaceSize][spaceSize];
			
			//make sure the center is an explosion
			spaces[radius][radius] = true;
			
			//System.out.println(spaceSize);
			
			int originX = currentMap.atX(x);
			int originY = currentMap.atY(y);
			
			int combos = (int)Math.pow(4, radius);
			int cores = Runtime.getRuntime().availableProcessors();
			
			Thread[] threads = new Thread[cores];
			for(int i = 0; i < cores; ++i) {
				int start = (int)Math.round(i * (float)combos / cores);
				int end = (int)Math.round((i + 1) * (float)combos / cores);
				(threads[i] = new Thread(new PathThread(spaces, radius, originX, originY, start, end))).run();
			}
			
			try {
				for(int i = 0; i < cores; ++i) {
						threads[i].join();
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}

			
			numExplosions = spaceSize * spaceSize;
			explosions = new Explosion[numExplosions];
			
			for(int i = 0; i < spaceSize; ++i) {
				for(int j = 0; j < spaceSize; ++j) {
					if(spaces[i][j]) {
						float ex = currentMap.getX(i + originX - radius) + rand(-8f, 8f);
						float ey = currentMap.getY(j + originY - radius) + rand(-8f, 8f);
						int ind = ( (i * spaceSize) + j);
						createEntity(explosions[ind] = new Explosion(ind, rand(1, 1.5f), rand(0, 0), rand(0.05, 0.1)), ex, ey);
					}
				}
			}

			carrotP = new float[carrotsLeft][2];
			
			for(int i = 0; i < numCarrots; ++i) {
				Carrot c = carrots[i];
				if(c != null) {
					Transform ct = c.getTransform();
					int mcx = currentMap.atX(ct.x);
					int mcy = currentMap.atY(ct.y);
					if(mcx > originX - radius - 1 && mcx < originX + radius + 1 && mcy > originY - radius -1 && mcy < originY + radius + 1) {
						if(spaces[mcx - originX + radius][mcy - originY + radius]) {
							carrots[i] = null;
							--carrotsLeft;
							carrotP[carrotsKilled] = new float[] {ct.x, ct.y};
							++carrotsKilled;
						}
					}
				}
			}
			
			//a maximum of 10 blocks will have gibs
			blockP = new float[10][2];
			
			//now we break the breakable blocks
			for(int i = 0; i < spaceSize; ++i) {
				for(int j = 0; j < spaceSize; ++j) {
					if(spaces[i][j]) {
						int mx = i + originX - radius;
						int my = j + originY - radius;
						if(currentMap.mapAccess(mx, my) == BLOCK_BREAKABLE) {
							currentMap.setBlock(mx, my, -1);
							blockP[blocksKilled] = new float[] {mx * 32, my * 32};
							++blocksKilled;
							if(blocksKilled > 9) {
								blocksKilled = 9;
							}
						}
					}
				}
			}
			
		} else {
			squishSound.play(false);
		}
		
		int carrotGibs = carrotsKilled * 12;
		int blockGibs = blocksKilled * 12;
		numGibs = 12 + carrotGibs + blockGibs;
		gibs = new Gib[numGibs];
		
		int totalCount = 0;
		for(int i = 0; i < 12; ++i) {
			createEntity(gibs[totalCount] = new Gib(totalCount, 0, rand(-512f, 512f), rand(-128f, -512f)), x - 8, y - 8);
			++totalCount;
		}
		for(int i = 0; i < carrotsKilled; ++i) {
			for(int j =0; j < 12; ++j) {
				createEntity(gibs[totalCount] = new Gib(totalCount, 1, rand(-512f, 512f), rand(-128f, -512f)), carrotP[i][0] + 8, carrotP[i][1] + 8);
				++totalCount;
			}
		}
		for(int i = 0; i < blocksKilled; ++i) {
			for(int j =0; j < 10; ++j) {
				createEntity(gibs[totalCount] = new Gib(totalCount, 2, rand(-512f, 512f), rand(-128f, -512f)), blockP[i][0] + 8, blockP[i][1] + 8);
				++totalCount;
			}
		}
		
		bomberTimer.reset();
		
		sparkSound.stop();
		
		tntTimer.going = false;
		tntTimer.dead = true;
		
		melons[mIndex] = null;
		playerMelon = null;
		
		//now for the selector stuff
		selectors[mIndex].mode = Selector.MODE_DEAD;
		
		--melonsLeft;
		melonDeathTimer.start();
			
	}
	
	public static float rand(float min, float max) {
		return (float)(min+Math.random()*(max-min));
	}
	
	public static double rand(double min, double max) {
		return min+Math.random()*(max-min);
	}
	
	public static int rand(int min, int max) {
		return (int)(min+Math.random()*(min-max+1));
	}
	
	public static void num2char(char[] place, int las, int num) {
		place[las] = (char)((num % 10) + 48);
		int tens = (num / 10);
		if(tens == 0) {
			place[las - 1] = 32;
		}else {
			place[las - 1] = (char)(tens + 48);
		}
	}
	
	public static void char2char(char[] place, char[] ins, int fir) {
		int l = ins.length;
		for(int i = 0; i < l; ++i) {
			place[i + fir] = ins[i];
		}
	}
	
	public void render() {
		
		eRender(skyBackground);
		
		currentMap.render(LAYER_MID);

		for(int i = 0; i < numMelons; ++i) {
			eRender_OS(melons[i]);
		}

		for(int i = 0; i < numCarrots; ++i) {
			eRender_S(carrots[i]);
		}
		
		for(int i = 0; i < numGibs; ++i) {
			eRender_OS(gibs[i]);
		}
		
		if(melonCamera) {
			for(int i = 0; i < numMelons; ++i) {
				eRender(selectors[i]);
			}
			
			MelonType type = melons[currentMelon].melonType;
			
			char2char(nameText, type.name, 6);
			melonFont.render(nameText, 8, 100, 1, false, selectFade);
			
			num2char(fuseText, 7, (int)type.fuse);
			melonFont.render(fuseText, 8, 132, 1, false, selectFade);
			
			num2char(radiusText, 9, (int)type.radius);
			melonFont.render(radiusText, 8, 164, 1, false, selectFade);
			
			melonFont.render(selectText, 256, 0, 4, true, selectFade);
			
		} else if(playerCamera) {
			for(int i = 0; i < numExplosions; ++i) {
				eRender_S(explosions[i]);
			}
			eRender(tntTimer);
		}
		
		eRender_S(carrotTransition);
		 
		if(lose) {
			melonFont.render(loseText, 256, 32, 3, true);
		} else if(win) {
			melonFont.render(winText, 256, 32, 3, true);
		} else {
			melonFont.render(remainingText, 340, 256, 1, false);
		}
	}
	
	@Override
	public void resizeUpdate() {
		
	}
	
}