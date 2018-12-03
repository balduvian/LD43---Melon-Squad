package game.scenes.game.entities;

import cnge.core.Entity;
import cnge.core.animation.Anim1D;
import cnge.graphics.Shader;
import cnge.graphics.Transform;
import cnge.graphics.texture.Texture;

import static game.scenes.game.GameAssets.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class TntTimer extends Entity {

	public float timer;
	public boolean going;
	public boolean dead;
	public Anim1D sparkAnim;
	
	public Transform sparkT;
	
	public TntTimer() {
		super();
		transform.setSize(512, 64);
		sparkAnim = new Anim1D(
			new int[] {
				0,
				1
			},
			new double[] {
				0.5,
				0.5
			}
		);
		sparkT = new Transform(64, 64);
	}
	
	public void update() {
		if(going) {
			sparkAnim.update();
			sparkT.setTranslation(bomberTimer.getTimer() * 364, 0);
		}
	}

	@Override
	public void render() {
		if(dead) {
			tntTimerTexture.bind();
		
			tileShader.enable();
			tileShader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrix(transform)));
			clipShader.setUniforms(tntTimerTexture.getX(), tntTimerTexture.getY(), tntTimerTexture.getZ(0), tntTimerTexture.getW(1), 1, 1, 1, 1);
			
			rect.render();
			
		} else {
			tntTimerTexture.bind();
			
			clipShader.enable();
			
			clipShader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrix(transform)));
			clipShader.setUniforms(tntTimerTexture.getX(), tntTimerTexture.getY(), tntTimerTexture.getZ(0), tntTimerTexture.getW(0), 1, 1, 1, 1);
			clipShader.setModelMatrix(camera.getModelMatrix(transform));
			float clip = -sparkT.x - 32;
			if(clip < -386) {
				clip = -386;
			}
			clipShader.setPlane(1, 0, 0, clip);
			
			GL20.glEnable(GL20.GL_CLIP_PLANE0);
			
			rect.render();
			
			GL20.glDisable(GL20.GL_CLIP_PLANE0);
		}
		//render the spark
		if(going) {
			tileShader.enable();
			
			sparkTexture.bind();
			
			tileShader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrix(sparkT)));
			tileShader.setUniforms(sparkTexture.getX(), sparkTexture.getY(), sparkTexture.getZ(sparkAnim.getX()), sparkTexture.getW(), 1, 1, 1, 1);
			
			rect.render();
		}
		
		Shader.disable();
		
		Texture.unbind();
	}

}