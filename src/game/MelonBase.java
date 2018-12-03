//gatcuddy <emmettglaser@gmail.com>

package game;

import cnge.core.Base;
import cnge.core.BasePreset;

import cnge.graphics.Window;

public class MelonBase extends Base {
	
	public MelonLoadScreen loadScreen;
	public WinLoadScreen winLoadScreen;
	
	public MelonBase(Window win, BasePreset set) {
		super(win, set);
		loadScreen = new MelonLoadScreen();
		winLoadScreen = new WinLoadScreen();
		loadLoadScreen(loadScreen);
	}
	
}