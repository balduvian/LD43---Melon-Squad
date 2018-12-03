package game.scenes.win;

import cnge.core.AssetBundle;
import cnge.core.Timer;
import cnge.graphics.Sound;
import game.MelonBase;
import game.scenes.game.MelonFont;

public class WinAssets extends AssetBundle<WinScene> {

	public static MelonFont melonFont;
	public static Timer winTimer;
	public static Sound winMusic;
	
	public static char[][] winText;
	
	@SafeVarargs
	public WinAssets(SceneLoadAction<WinScene>... sceneLoads) {
		super(
			((MelonBase)base).winLoadScreen, 
			new LoadAction[] {
				() -> {
					melonFont = new MelonFont();
					winMusic = new Sound("res/sounds/song2.wav");
					winText = new char[][] {
						"Congratulations Brave Melons".toCharArray(),
						"All the Carrots are now gone from this land".toCharArray(),
						"May you rest in peace".toCharArray()
					};
				}
			},
			sceneLoads
		);
	}

}
