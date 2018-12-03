package cnge.graphics;

/**
 * a shape that supports textures
 */
abstract public class TexShape extends Shape{

	/**
	 * makes the texture shape
	 * 
	 * @param vertices - list of vertices
	 * @param indices - the order in which the vertices are connected
	 * @param texCoords - where on the texture to render for each vertex
	 * @param drawMode - points, lines, or triangles
	 */
	public TexShape(float[] vertices, int[] indices, float[] texCoords, int drawMode) {
		super(2, vertices, indices, drawMode);
		vbo.addAttrib(texCoords, 2);
	}

}
