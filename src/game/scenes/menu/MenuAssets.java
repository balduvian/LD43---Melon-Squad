package game.scenes.menu;

import cnge.core.AssetBundle;
import cnge.graphics.Sound;
import cnge.graphics.TexShape;
import cnge.graphics.shapes.RectShape;
import cnge.graphics.texture.Texture;
import cnge.graphics.texture.TexturePreset;
import cnge.graphics.texture.TileTexture;
import game.MelonBase;
import game.scenes.menu.entities.MenuSelector;
import game.scenes.menu.entities.HowToCard;
import game.scenes.menu.entities.MenuBackground;
import game.scenes.menu.entities.Title;
import game.shaders.ColorShader;
import game.shaders.TextShader;
import game.shaders.TextureShader;
import game.shaders.TileShader;

public class MenuAssets extends AssetBundle<MenuScene> {

	/*
	 * textures
	 */
	public static Texture skyTexture;
	public static Texture titleTexture;
	public static TileTexture menuSelectorTexture;
	
	public static Texture howToCardTexture;
	
	public static Texture[] howToCards;
	
	/*
	 * shaders
	 */
	public static TileShader tileShader;
	public static ColorShader colShader;
	public static TextureShader textureShader;
	public static TextShader textShader;
	
	/*
	 * shape
	 */
	public static TexShape rect;
	
	/*
	 * fonts
	 */
	public static MenuFont melonFont;
	
	/*
	 * texts
	 */
	public static char[] playText;
	public static char[] tutorialText;
	
	public static char[] spaceText;
	public static char[] spaceTextHowTo;
	
	/*
	 * entities
	 */
	public static MenuBackground background;
	public static MenuSelector rightSelect; 
	public static MenuSelector leftSelect; 
	public static Title title; 
	public static HowToCard howToCard;
	
	/*
	 * sounds
	 */
	public static Sound menuSong;
	
	@SafeVarargs
	public MenuAssets(SceneLoadAction<MenuScene>... sceneLoads) {
		super(
			((MelonBase)base).loadScreen, 
			new LoadAction[] {
				() -> {
					playText = "Play".toCharArray();
					
					tutorialText = "Tutorial".toCharArray();
					
					spaceText = "Press Space to Select".toCharArray();
					
					spaceTextHowTo = "Press Space to Continue".toCharArray();
				},
				() -> {
					skyTexture = new Texture("res/textures/sky-bac.png", new TexturePreset().clampHorz(false).clampVert(true));
				},
				() -> {
					titleTexture = new Texture("res/textures/title.png");
				},
				() -> {
					menuSelectorTexture = new TileTexture("res/textures/menu-selector.png", 2);
				},
				() -> {
					melonFont = new MenuFont();
				},
				() -> {
					rect = new RectShape();
				},
				() -> {
					howToCardTexture = new Texture("res/textures/how-to-card.png");
				},
				() -> {
					howToCards = new Texture[] {
						new Texture("res/textures/how-to/screen0.png"),
						new Texture("res/textures/how-to/screen1.png"),
						new Texture("res/textures/how-to/screen2.png"),
						new Texture("res/textures/how-to/screen3.png"),
						new Texture("res/textures/how-to/screen4.png"),
					};
				},
				() -> {
					menuSong = new Sound("res/sounds/song1.wav");
				},
				() -> {
					tileShader = new TileShader();
					colShader = new ColorShader();
					textureShader = new TextureShader();
					textShader = new TextShader();
				}
			},
			sceneLoads
		);
	}
	
	
}