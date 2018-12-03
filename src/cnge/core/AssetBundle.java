package cnge.core;

import java.lang.reflect.Array;

import cnge.core.AssetBundle.SceneLoadAction;

abstract public class AssetBundle<S extends Scene<S>> {
	
	protected static Base base;
	private S scene;
	protected LoadAction[] loads;
	protected SceneLoadAction<S>[] sceneLoads;
	protected LoadScreen loadScreen;
	
	public AssetBundle(LoadScreen s, LoadAction[] l, SceneLoadAction<S>... c) {
		loadScreen = s;
		loads = l;
		sceneLoads = c;
	}
	
	public void giveScene(S s) {
		scene = s;
	}
	
	public void load() {
		
		base.setLoading(loadScreen, true);
		
		int num0 = loads.length;
		int num1 = sceneLoads.length;
		loadScreen.startLoad(num0 + num1);
		
		//do an initial render with nothing loaded so far
		loadScreen.render();
		
		//do all of our loads
		for(int i = 0; i < num0; ++i) {
			loads[i].load();
			//ok we loaded that now SHOW us
			loadScreen.setLoaded();
			
			base.update();
			base.render();
		}
		
		for(int i = 0; i < num1; ++i) {
			sceneLoads[i].load(scene);
			loadScreen.setLoaded();
			base.update();
			base.render();
		}
		
		base.setLoading(null, false);
	}
	
	public interface LoadAction {
		public void load();
	}
	
	public interface SceneLoadAction<S> {
		public void load(S s);
	}
	
	public static void giveBase(Base b) {
		base = b;
	}
	
}