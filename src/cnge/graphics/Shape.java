package cnge.graphics;

import cnge.graphics.shapes.ArrowShape;
import cnge.graphics.shapes.RectShape;

abstract public class Shape {
	
	protected VBO vbo;
	
	public static RectShape RECT;
	public static ArrowShape ARROW;
	
	public static void giveCamera(Camera c) {
		RECT = new RectShape();
		ARROW = new ArrowShape();
	}
	
	public Shape(int numAttribs, float vertices[], int[] indices, int drawMode) {
		vbo = new VBO(numAttribs, vertices, indices, drawMode);
	}
	
	public void render() {
		vbo.render();
	}
	
}