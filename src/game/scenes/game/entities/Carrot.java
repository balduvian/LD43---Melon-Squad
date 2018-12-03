package game.scenes.game.entities;

import cnge.core.Entity;
import cnge.core.Timer;
import cnge.core.animation.Anim2D;
import cnge.graphics.Shader;
import cnge.graphics.Transform;
import cnge.graphics.texture.Texture;

import static game.scenes.game.GameAssets.*;
import game.scenes.game.GameScene;

public class Carrot extends Entity {

	public Anim2D idleAnim;
	public Anim2D vigilAnim;
	
	//one at a time
	public CarrotBullet bullet;
	
	public Timer shootTimer;
	public Timer loadTimer;
	public Timer recoilTimer;
	
	public boolean facing = true;
	
	public int frameX;
	public int frameY;
	
	public float angle;
	public boolean lineOfSight;
	
	public boolean recoil;

	public boolean loaded;
	public boolean loading;
	public boolean shooting;
	
	public Carrot() {
		super();
		transform.setSize(32, 32);
		shootTimer = new Timer(
			0.2, 
			() -> {
				shooting = false;
				loaded = false;
				bullet = new CarrotBullet(angle);
				recoil = true;
				recoilTimer.start();
				bullet.getTransform().setTranslation(transform.x + 8, transform.y + 8);
				carrotGunSound.play(false);
			}
		);
		loadTimer = new Timer(
			1.1, 
			() -> {
				shootTimer.reset();
				loaded = true;
				loading = false;
			}
		);
		recoilTimer = new Timer(
			0.3,
			() -> {
				recoil = false;
			}
		);
		
		vigilAnim = new Anim2D(
			new int[][] {
				{0, 0},
				{2, 0},
			},
			new double[] {
				0.4,
				0.4,
			}
		);
		idleAnim = new Anim2D(
				new int[][] {
					{1, 0},
					{2, 1},
				},
				new double[] {
					0.4,
					0.4,
				}
			);
		
	}
	
	public void fakeUpdate() {
		idleAnim.update();
		frameX = idleAnim.getX();
		frameY = idleAnim.getY();
	}
	
	public void update() {
		
		recoilTimer.update();
		
		//CARROT LINE OF SIGHT
		if(playerMelon != null) {
			if(onScreen) {
				Transform pt = playerMelon.getTransform();
				
				float px = pt.x + 16;
				float py = pt.y + 16;
				
				float cx = transform.x + 16;
				float cy = transform.y + 16;
				
				float distanceX = (px - cx);
				float distanceY = (py - cy);
				
				angle = (float)(Math.atan2(distanceY, distanceX));
				
				float distance = (float)Math.sqrt( Math.pow(distanceX, 2) + Math.pow(distanceY, 2) );
				
				int intDistance = (int)(distance / 16);
				
				lineOfSight = true;
				
				for(int i = 0; i < intDistance; ++i) {
					float along = ((float)i / intDistance);
					
					float currentX = (px - cx) * along + cx;
					float currentY = (py - cy) * along + cy;
					
					if(currentMap.getBlockSet().get(currentMap.mapAccess(currentMap.atX(currentX), currentMap.atY(currentY))).solid) {
						lineOfSight = false;
					}
				}
				
				facing = (playerMelon.getTransform().x > transform.x);
			}
		} else {
			lineOfSight = false;
		}
		
		if(bullet == null) {
			
			if(!loading && !loaded) {
				loading = true;
				loadTimer.start();
			}
			loadTimer.update();
			
			if(lineOfSight && loaded && !shooting) {
				shootTimer.start();
				shooting = true;
			}
			shootTimer.update();
			
		} else {
			bullet.onScreenUpdate();
			int updateFlag = bullet.bulletUpdate();
			switch(updateFlag) {
				case 1:
					bullet = null;
					loadTimer.reset();
					break;
				case 2:
					bullet = null;
					loadTimer.reset();
					((GameScene)scene).killSuggest(false);
					break;
			}
		}
		
		if(recoil) {
			frameX = 1;
			frameY = 1;
		} else {
			vigilAnim.update();
			frameX = vigilAnim.getX();
			frameY = vigilAnim.getY();
		}

	}

	@Override
	public void render() {
		carrotTexture.bind();
		
		tileShader.enable();
		
		Transform renderT = new Transform(transform);
		if(!facing) {
			renderT.width = -renderT.width;
			renderT.x -= renderT.width;
		}
		tileShader.setMvp(camera.getModelViewProjectionMatrix(camera.getModelMatrix(renderT)));
		
		tileShader.setUniforms(carrotTexture.getX(), carrotTexture.getY(), carrotTexture.getZ(frameX), carrotTexture.getW(frameY), 1, 1, 1, 1);
		
		rect.render();
		
		Shader.disable();
		
		Texture.unbind();
		
		if(bullet != null) {
			bullet.render();
		}
	}

}