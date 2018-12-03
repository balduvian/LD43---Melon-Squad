package game.scenes.game.entities;

import cnge.core.Base;
import cnge.core.Entity;
import cnge.core.Hitbox;
import cnge.graphics.Shader;
import cnge.graphics.Transform;
import cnge.graphics.texture.Texture;

import static game.scenes.game.GameAssets.*;

public class CarrotBullet extends Entity {

	public static final boolean RIGHT = true;
	public static final boolean LEFT = false;
	
	public static final int bulletSpeed = 800;
	
	public float velocityX;
	public float velocityY;
	
	public float angle;
	
	public Hitbox box;
	
	public CarrotBullet(float ang) {
		super();
		transform.setSize(16, 16);
		angle = ang;
		velocityX = (float) (Math.cos(angle) * bulletSpeed);
		velocityY = (float) (Math.sin(angle) * bulletSpeed);
		transform.rotation = angle;
		box = new Hitbox(4, 4, 8, 8);
	}
	
	public void update() {
		//nothing to see here folks
	}

	public int bulletUpdate() {
		transform.moveX((float)(velocityX * Base.time));
		transform.moveY((float)(velocityY * Base.time));
		
		if(currentMap.getBlockSet().get(currentMap.mapAccess(currentMap.atX(transform.x), currentMap.atY(transform.y))).solid) {
			return 1;
		}
		
		if(!onScreen) {
			return 1;
		}
		
		if(playerMelon != null) {
			Transform pt = playerMelon.getTransform();
			Hitbox pb = playerMelon.collisionBox;
			
			if(
				pt.x + pb.x < transform.x + box.x + box.width && 
				pt.x + pb.x + pb.width > transform.x + box.x && 
				pt.y + pb.y < transform.y + box.y + box.height && 
				pt.y + pb.y + pb.height > transform.y + box.y
			) {
				return 2;
			}
		}
		
		return 0;
	}
	
	@Override
	public void render() {
		carrotBulletTexture.bind();
		
		textureShader.enable();
		
		textureShader.setMvp(camera.getModelViewProjectionMatrix(camera.getModelMatrix(transform)));
		
		rect.render();
		
		Shader.disable();
		
		Texture.unbind();
	}

}