package game.scenes.game;

import static game.scenes.game.GameAssets.*;

import cnge.core.level.Map;
import cnge.graphics.Shader;
import cnge.graphics.Transform;
import cnge.graphics.texture.Texture;
import cnge.graphics.texture.TileTexture;
import game.MelonBlock;

public class MelonMap extends Map<MelonBlock> {
	
	public int[][] values;
	
	public MelonMap(int[][] v) {
		super(mAccess, 32);
		values = v;
	}
	
	public static Access mAccess = new Access() {
		public int access(int[][] a, int x, int y) {
			return edgeAccess(a, x, y);
		}
	};
	
	public static Access vAccess = new Access() {
		public int access(int[][] a, int x, int y) {
			int w = a.length;
			int h = a[0].length;
			if(x < 0) {
				x = 0;
			}else if(x > w - 1) {
				x = w - 1;
			}
			if(y < 0) {
				return Map.OUTSIDE_MAP;
			} else if (y > h - 1) {
				return Map.OUTSIDE_MAP;
			}
			return a[x][y];
		}
	};
	
	public void blockRender(int l, int x, int y, float left, float right, float up, float down) {
		int bId = mapAccess(x, y);
		if(bId != Map.OUTSIDE_MAP) {
			MelonBlock tb = blockSet.get(bId);
			if(tb != null && tb.layer == l) {
				TileTexture tex = tb.texture;
				
				tex.bind();
				
				tileShader.enable();
				
				tileShader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrix(left, right, up, down)));
				
				
				int value = vAccess.access(values, x, y);
				
				if(value != Map.OUTSIDE_MAP && tb.id == BLOCK_DIRT && MelonLevel.isUpWall(value)) {
					tileShader.setUniforms(tex.getX(), tex.getY(), tex.getZ(0), tex.getW(0), 1, 1, 1, 1);
				} else {
					tileShader.setUniforms(tex.getX(), tex.getY(), tex.getZ(tb.texX), tex.getW(tb.texY), 1, 1, 1, 1);
				}
				
				rect.render();
				
				Shader.disable();
				
				Texture.unbind();
			}
		}
	}
	
	public void mapRender(Transform t, Texture tx) {
		tx.bind();
		
		tileShader.enable();
		
		tileShader.setMvp(camera.getModelViewProjectionMatrix(camera.getModelMatrix(t)));
		tileShader.setUniforms(1, -1, 0, 0, 1, 1, 1, 1);
		
		rect.render();
		
		Shader.disable();
		
		Texture.unbind();
	}
	
	public void update() {
		
	}

}