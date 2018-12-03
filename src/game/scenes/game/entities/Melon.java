package game.scenes.game.entities;

import cnge.core.Base;
import cnge.core.Entity;
import cnge.core.Hitbox;
import cnge.core.animation.Anim2D;
import cnge.core.level.Map;
import cnge.graphics.Shader;
import cnge.graphics.Transform;
import cnge.graphics.texture.Texture;
import game.MelonBlock;
import game.scenes.game.GameScene;
import game.scenes.game.MelonLevel;
import game.scenes.game.MelonMap;

import static game.scenes.game.GameAssets.*;

public class Melon extends Entity {

	//TODO SOM P MNODE
	
		public static final float ladderY = 100;
		public static final float ladderA = 2024;
		public static final float ladderStopA = 2024;
		public static final float ladderMaxX = 125;
		
		public static final float maxX = 192;
		public static final float maxY = 2000;
		public static final float walkA = 1024;
		public static final float stopA = 1024;
		public static final float airA = 512;
		public static final float stopAirA = 128;
		public static final float jumpV = 270;
		public static final double jumpTime = 0.125;
		public static final boolean RIGHT = true;
		public static final boolean LEFT = false;
		
		public static final MoveMode MODE_LADDER = new MoveMode(ladderMaxX, ladderA, ladderStopA);
		public static final MoveMode    MODE_AIR = new MoveMode(maxX, airA, stopAirA);
		public static final MoveMode MODE_GROUND = new MoveMode(maxX, walkA, stopA);
		
		public Hitbox collisionBox;
		
		public Anim2D idleAnim;
		public Anim2D runAnim;
		public Anim2D climbAnim;
		
		public int frameX;
		public int frameY;
		
		public float velocityX;
		public float velocityY;
		
		public float dx;
		public float dy;
		
		public float accelerationX;
		public float accelerationY;
		
		public boolean air;
		
		public boolean facing;
		
		public boolean jumpLock;
		
		public double jumpTimer;
		
		public boolean controllable;
		
		public boolean ladderMode;
		
		public MoveMode moveMode;
		public MelonType melonType;
		
		public int ind;
		
		public static final int TYPE_REGULAR = 0;
		public static final int TYPE_MINI = 1;
		public static final int TYPE_HUGE = 2;
		
		public static final MelonType[] setTypes = {
			new MelonType("Normal Dan ".toCharArray(), 9, 2, 1, 1, 1),//regular
			new MelonType("Sneaky Pete".toCharArray(), 13, 1, 1, 0.5f, 1),//mini
			new MelonType("Big Bertha ".toCharArray(), 5, 3, 0.5f, 1, 0.5f),//huge
		};
		
		public Melon(int i, int t) {
			super();
			ind = i;
			melonType = setTypes[t];
			frameX = 0;
			frameY = 0;
			velocityX = 0;
			velocityY = 0;
			air = false;
			accelerationY = 32;
			accelerationX = 0;
			facing = RIGHT;
			transform.setSize(32, 32);
			jumpLock = false;
			controllable = false;
			collisionBox = new Hitbox(2, 7, 28, 25);
			moveMode = MODE_GROUND;
			runAnim = new Anim2D(
				new int[][] {
					{0, 1},
					{0, 0},
					{1, 1},
					{0, 0},
				},
				new double[] {
					0.1,
					0.1,
					0.1,
					0.1,
				}
			);
			climbAnim = new Anim2D(
				new int[][] {
					{3, 0},
					{3, 1},
				},
				new double[] {
					0.1,
					0.1,	
				}
			);
			idleAnim = new Anim2D(
				new int[][] {
					{0, 0},
					{1, 0},
					{2, 0},
					{1, 0},
				},
				new double[] {
					0.2,
					0.2,
					0.2,
					0.2,
				}
			);
					
		}
		
		public static class MelonType {
			public char[] name;
			public double fuse;
			public int radius;
			public float red, green, blue;
			
			public MelonType(char[] n, double f, int ra, float r, float g, float b) {
				name = n;
				fuse = f;
				radius = ra;
				red = r;
				green = g;
				blue = b;
			}
		}
		
		public static class MoveMode {
			public float max;
			public float acceleration;
			public float stop;
			
			public MoveMode(float m, float a, float s) {
				max = m;
				acceleration = a;
				stop = s;
			}
		}
		
		//used by melons awaiting selection
		public void fakeUpdate() {
			idleAnim.update();
			frameX = idleAnim.getX();
			frameY = idleAnim.getY();
		}
		
		public void update() {
			GameScene gs = ((GameScene)scene);
			MelonMap map = currentMap;
			Transform t = getTransform();
			Hitbox b = collisionBox;
			
			accelerationY = GameScene.GRAVITY;
			
			float tempA = 0;
			boolean active = false;
			boolean suggestLadder = false;
			
			//to snap to if neccesary
			float ladderX = 0;
			
			if(controllable) {
				if(gs.pressUp) {
					suggestLadder = true;
					if(ladderMode) {
						velocityY = -ladderY;
					}
				} else if (!gs.pressDown && ladderMode){
					velocityY = 0;
				}
				
				if(gs.pressDown) {
					if(ladderMode) {
						velocityY = ladderY;
					}
				} else if (!gs.pressUp && ladderMode){
					velocityY = 0;
				}
				
				if(gs.pressLeft) {
					facing = LEFT;
					active = true;
					tempA -= moveMode.acceleration;
				}
				
				if(gs.pressRight) {
					facing = RIGHT;
					active = true;
					tempA += moveMode.acceleration;
				}
				
				if(jumpTimer > 0) {
					jumpTimer -= Base.time;
					velocityY = -jumpV;
					if(jumpTimer < 0 || !gs.pressJump) {
						jumpTimer = 0;
					}
				}else {
					if(jumpLock) {
						jumpLock = gs.pressJump;
					}else {
						if(gs.pressJump) {
							if(!air || ladderMode) {
								ladderMode = false;
								velocityY = -jumpV;
								jumpTimer = jumpTime;
								jumpLock = true;
							}
						}
					}
				}
			}
			if(!active) {
				if(velocityX > 0) {
					tempA -= moveMode.stop;
					velocityX += (float)(tempA * Base.time);
					if(velocityX < 0) {
						velocityX = 0;
					}
				}else if(velocityX < 0) {
					tempA += moveMode.stop;
					velocityX += (float)(tempA * Base.time);
					if(velocityX > 0) {
						velocityX = 0;
					}
				}else {
					tempA = 0;
				}
			}else {
				velocityX += (float)(tempA * Base.time);
			}
			
			if(!ladderMode) {
				velocityY += accelerationY * Base.time;
			}
			
			//slowdown part for overspeeding
			if(active) {
				if(velocityX > moveMode.max) {
					velocityX -= moveMode.stop;
					if(velocityX < moveMode.max) {
						velocityX = moveMode.max;
					}
				}else if(velocityX < -moveMode.max){
					velocityX += moveMode.stop;
					if(velocityX > -moveMode.max) {
						velocityX = -moveMode.max;
					}
				}
			}
			
			dx = (float)(velocityX * Base.time);
			dy = (float)(velocityY * Base.time);
			
			float baseLeft = t.x + b.x;
			float baseRight = t.x + b.x + b.width;
			float baseUp = t.y + b.y;
			float baseDown = t.y + b.y + b.height;
			
			float left = baseLeft + dx;
			float right = baseRight + dx;
			float up = baseUp + dy;
			float down = baseDown + dy;
			
			int l = 0;
			int r = 0;
			if(left > 0) {
				l = (int)(Math.min(baseLeft, left) / map.getScale() );
			}else {
				l = (int)(Math.min(baseLeft, left) / map.getScale() - 1);
			}
			if(right > 0) {
				r = (int)(Math.max(baseRight, right) / map.getScale() );
			}else {
				r = (int)(Math.max(baseRight, right) / map.getScale() + 1);
			}
			
			int u = (int)(Math.min(baseUp, up) / map.getScale() );
			int d = (int)(Math.max(baseDown, down) / map.getScale() ) + 1;
			
			boolean hasHitGround = false;
			
			float downDist = 100;
			
			boolean onLadder = false;
			
			boolean ladderHead = false;
			
			for(int i = l; i <= r; ++i) {
				for(int j = u; j <= d; ++j) {
					int bId = map.mapAccess(i, j);
					if(bId != Map.OUTSIDE_MAP ) {
						MelonBlock sb = (MelonBlock)map.getBlockSet().get(bId);
						
						float upSide = map.getY(j);
						float leftSide = map.getX(i);
						float downSide = map.getY(j + 1);
						float rightSide = map.getX(i + 1);
						
						//if the player, not factoring in movement, is inside the vertical span of the block
						boolean withinVertical = (baseUp < downSide && baseDown > upSide);
						//same for horizontal
						boolean withinHorizontal = (baseLeft < rightSide && baseRight > leftSide);
						
						if(sb.id == BLOCK_LADDER) {
							
							//if the player, not factoring in movement, is inside the vertical span of the block
							withinVertical = (baseUp < downSide && baseDown > upSide);
							//same for horizontal
							withinHorizontal = (baseLeft < (rightSide - 4) && baseRight > (leftSide + 4));
							
							if(withinVertical && withinHorizontal) {
								onLadder = true;
								ladderX = leftSide;
								if(!ladderMode && suggestLadder) {
									velocityY = 0;
									ladderMode = true;
								}
							}
						} else if(sb.solid) {
							do {
								
								//the current wall value for this block, telling whether it is collidable on any given face
								int wallValue = map.access(map.values, i, j);
								
								if(MelonLevel.isUpWall(wallValue)) {
									if(withinHorizontal) {
										float tempDown = upSide - baseDown;
										if(tempDown < downDist && !(tempDown < 0)) {
											downDist = tempDown;
										}
										if(dy > 0 && (down > upSide && down < downSide)) {
											velocityY = 0;
											dy = upSide - baseDown;
											hasHitGround = true;
											break;
										}
									}
								}
								
								if(MelonLevel.isLeftWall(wallValue) && dx > 0 && withinVertical && (right > leftSide && right < rightSide)) {
									velocityX = 0;
									dx = leftSide - baseRight;
									break;
								}
								
								if(MelonLevel.isDownWall(wallValue) && dy < 0 && withinHorizontal && (up < downSide && up > upSide)) {
									velocityY = 0;
									dy = downSide - baseUp;
									if(ladderMode) {
										ladderHead = true;
									}
									break;
								}
								
								if(MelonLevel.isRightWall(wallValue) && dx < 0 && withinVertical && (left < rightSide && left > leftSide)) {
									velocityX = 0;
									dx = rightSide - baseLeft;
									break;
								}
								
							} while(false);
						}
						
					}
				}
			}

			//check to see if we are booted off ladder
			if(ladderMode) {
				ladderMode = onLadder;
			}
			
			//if we're still in ladder mode
			if(ladderMode) {
				moveMode = MODE_LADDER;
				if(ladderHead) {
					//if(dx == 0) {
						if(Math.abs(ladderX - t.x) < 16) {
							dx += (float)(ladderMaxX * Base.time * Math.signum(ladderX - t.x));
						}
					//}
				}
				if(dy != 0 || dx != 0) {
					climbAnim.update();
				}
				frameX = climbAnim.getX();
				frameY = climbAnim.getY();
			} else {
				air = !hasHitGround;
				
				if(air) {
					moveMode = MODE_AIR;
					frameX = 0;
					frameY = 1;
				}else {
					moveMode = MODE_GROUND;
					if(active) {
						runAnim.update();
						frameX = runAnim.getX();
						frameY = runAnim.getY();
					}else {
						idleAnim.update();
						frameX = idleAnim.getX();
						frameY = idleAnim.getY();
					}
				}
			}
			
			t.move(dx, dy);
			
			//TODO FIX??
			if(t.y > gs.deathBarrier) {
				gs.killSuggest(false);
			}
		}
		
		public void render() {
			melonTexture.bind();
			
			tileShader.enable();
			
			tileShader.setUniforms(melonTexture.getX(), melonTexture.getY(), melonTexture.getZ(frameX), melonTexture.getW(frameY), melonType.red, melonType.green, melonType.blue, 1);
			
			Transform renderT = new Transform(transform);
			if(!facing) {
				renderT.width = -renderT.width;
				renderT.x -= renderT.width;
			}
			tileShader.setMvp(camera.getModelViewProjectionMatrix(camera.getModelMatrix(renderT)));
			
			rect.render();
			
			Shader.disable();
			
			Texture.unbind();
		}

}