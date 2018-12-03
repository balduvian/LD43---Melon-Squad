package cnge.graphics.shapes;

import org.lwjgl.opengl.GL11;

import cnge.graphics.TexShape;

public class RectShape extends TexShape{

	public RectShape() {
		super(
				new float[] {
		           1, 0, 0,
		           1, 1, 0,
		           0, 1, 0,
		           0, 0, 0
				}, new int[] {
					0, 1, 3,
					1, 2, 3
				}, new float[] {
			        1, 0,
			        1, 1,
			        0, 1,
			        0, 0
				},
				GL11.GL_TRIANGLES
		);
	}
	
}
