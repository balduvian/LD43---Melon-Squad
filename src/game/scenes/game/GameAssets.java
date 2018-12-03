package game.scenes.game;

import cnge.core.AssetBundle;
import cnge.core.BlockSet;
import cnge.core.Timer;
import cnge.graphics.Shape;
import cnge.graphics.Sound;
import cnge.graphics.TexShape;
import cnge.graphics.texture.Texture;
import cnge.graphics.texture.TexturePreset;
import cnge.graphics.texture.TileTexture;
import game.MelonBase;
import game.MelonBlock;
import game.scenes.game.entities.Carrot;
import game.scenes.game.entities.CarrotBullet;
import game.scenes.game.entities.CarrotTransition;
import game.scenes.game.entities.Explosion;
import game.scenes.game.entities.Gib;
import game.scenes.game.entities.Melon;
import game.scenes.game.entities.Selector;
import game.scenes.game.entities.SkyBackground;
import game.scenes.game.entities.TntTimer;
import game.scenes.game.levels.Level1;
import game.scenes.game.levels.Level2;
import game.scenes.game.levels.Level3;
import game.scenes.game.levels.Level4;
import game.scenes.game.levels.Level5;
import game.scenes.game.levels.Level6;
import game.scenes.game.levels.Level7;
import game.shaders.ClipShader;
import game.shaders.ColorShader;
import game.shaders.TextShader;
import game.shaders.TextureShader;
import game.shaders.TileShader;

public class GameAssets extends AssetBundle<GameScene> {
	
	public static final int LAYER_MID = 1;
	
	public static final int ENTITY_MELON_REGULAR = 0;
	public static final int ENTITY_MELON_MINI = 1;
	public static final int ENTITY_MELON_HUGE = 2;
	
	public static final int ENTITY_CARROT = 3;
	
	public static final int BLOCK_DIRT = 0;
	public static final int BLOCK_REDSTONE = 1;
	public static final int BLOCK_WOOD = 2;
	public static final int BLOCK_LADDER = 3;
	public static final int BLOCK_BREAKABLE = 4;
	
	/*
	 * block sets
	 */
	public static BlockSet<MelonBlock> melonBlocks;
	
	/*
	 * stuff in the scene
	 */
	public static MelonLevel currentLevel;
	public static MelonMap currentMap;
	
	public static SkyBackground skyBackground;
	
	public static Melon[] melons;
	public static Melon playerMelon;
	public static Carrot[] carrots;
	public static Selector[] selectors;
	public static Explosion[] explosions;
	public static CarrotTransition carrotTransition;
	public static Gib[] gibs;
	public static TntTimer tntTimer;
	public static CarrotBullet[] carrotBullets;
	
	/*
	 * map groups
	 */
	
	public static MelonLevel[] gameLevels;
	
	/*
	 * fonts
	 */
	public static MelonFont melonFont;
	
	/*
	 * texts
	 */
	public static char[] selectText;
	public static char[] loseText;
	public static char[] winText;
	public static char[] remainingText;
	
	public static char[] nameText;
	public static char[] fuseText;
	public static char[] radiusText;
	
	/*
	 * graphics
	 */
	public static TexShape rect;
	
	public static TileShader tileShader;
	public static ColorShader colShader;
	public static TextureShader textureShader;
	public static TextShader textShader;
	public static ClipShader clipShader;
	
	public static TileTexture tileSheet;
	public static Texture skyTexture;
	public static TileTexture carrotTexture;
	public static TileTexture melonTexture;
	public static TileTexture selectorTexture;
	public static TileTexture explosionTexture;
	public static Texture carrotCutout;
	public static TileTexture gibTexture;
	public static TileTexture tntTimerTexture;
	public static TileTexture sparkTexture;
	public static Texture carrotBulletTexture; 
	
	/*
	 * timers
	 */
	public static Timer melonDeathTimer;
	public static Timer loseLevelTimer;
	public static Timer bomberTimer;
	public static Timer winLevelTimer;
	
	/*
	 * sounds
	 */
	public static Sound sparkSound;
	public static Sound explosionSound; 
	public static Sound gameSong;
	public static Sound carrotGunSound;
	public static Sound squishSound;
	
	@SafeVarargs
	public GameAssets(SceneLoadAction<GameScene>... sceneLoads) {
		super(
			((MelonBase)base).loadScreen,
			new LoadAction[] {
				() -> {
					System.out.println("LOADING GAME");
					rect = Shape.RECT;		
				},
				() -> {
					tileShader = new TileShader();
					colShader = new ColorShader();
					textureShader = new TextureShader();
					textShader = new TextShader();
					clipShader = new ClipShader();
				},
				() -> {
					tileSheet = new TileTexture("res/textures/tileset.png", 3, 2, new TexturePreset().clampHorz(true).clampVert(true));
				},
				() -> {
					skyTexture = new Texture("res/textures/sky-bac.png", new TexturePreset().clampHorz(false).clampVert(true));
				},
				() -> {
					carrotTexture = new TileTexture("res/textures/carrot.png", 3, 2);
				},
				() -> {
					melonTexture = new TileTexture("res/textures/melon.png", 4, 2);
				},
				() -> {
					selectorTexture = new TileTexture("res/textures/selector.png", 3);
				},
				() -> {
					explosionTexture = new TileTexture("res/textures/explosion.png", 5, 2);
				},
				() -> {
					carrotCutout = new Texture("res/textures/carrot-cutout.png", new TexturePreset().clampHorz(true).clampVert(true));
				},
				() -> {
					gibTexture = new TileTexture("res/textures/gibs.png", 6, 2);
				},
				() -> {
					tntTimerTexture = new TileTexture("res/textures/tnt-timer.png", 1, 2);
				},
				() -> {
					sparkTexture = new TileTexture("res/textures/spark.png", 2);
				},
				() -> {
					carrotBulletTexture = new Texture("res/textures/carrot-projectile.png");
				},
				() -> {
					explosionSound = new Sound("res/sounds/explosion.wav");
					sparkSound = new Sound("res/sounds/spark.wav");
					carrotGunSound = new Sound("res/sounds/carrot-gun.wav");
					gameSong = new Sound("res/sounds/song0.wav");
					squishSound = new Sound("res/sounds/squish.wav");
				},
				() -> {
					melonBlocks = new BlockSet<MelonBlock>(
						//entities
						new int[] {
							255,
							200,
							150,
							100
						},
						//default
						new MelonBlock(-1, -1, false, null, -1, -1),	
						//actual
						new MelonBlock(0, LAYER_MID, true, tileSheet, 0, 1), //DIRT
						new MelonBlock(50, LAYER_MID, true, tileSheet, 1, 1),  //REDSTONE
						new MelonBlock(100, LAYER_MID, true, tileSheet, 1, 0),  //WOOD
						new MelonBlock(150, LAYER_MID, false, tileSheet, 2, 1),   //LADDER
						new MelonBlock(200, LAYER_MID, true, tileSheet, 2, 0)   //CRACKED STONE
					);
				},
				() -> {
					melonFont = new MelonFont();
				},
				() -> {
					gameLevels = new MelonLevel[] {
						new Level1(),
						new Level2(),
						new Level3(),
						new Level4(),
						new Level5(),
						new Level6(),
						new Level7(),
					};
				},
				() -> {
					selectText = "Select Melon".toCharArray();
					loseText = "The Carrots Remain...".toCharArray();
					remainingText = "Carrots Remaining: __".toCharArray();
					winText = "All Carrots Destroyed!".toCharArray();
					
					//11 extra
					nameText   = "Name: ___________".toCharArray();
					fuseText   = "Fuse: __S".toCharArray();
					radiusText = "Radius: __".toCharArray();
				}
			},
			sceneLoads
		);
	}
	
}