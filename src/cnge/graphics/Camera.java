package cnge.graphics;
import org.joml.Matrix4f;

public class Camera {
	
	private Transform transform;
	
	private Matrix4f projection, projectionView;
	
	private float defaultWidth;
	private float defaultHeight;
	
	public Camera(float w, float h) {
		transform = new Transform();
		projectionView = new Matrix4f();
		setDims(w, h);
	}
	
	public void giveDefaults(float w, float h) {
		defaultWidth = w;
		defaultHeight = h;
	}
	
	/**
	 * sets virtual space back to the dimensions of the game
	 */
	public void defaultDims() {
		transform.setSize(defaultWidth, defaultHeight);
		projection = new Matrix4f().setOrtho(0, defaultWidth, defaultHeight, 0, 1, -1);
		update();
	}
	
	/**
	 * sets the virtual space the camera renders to
	 * 
	 * @param w - virtual width
	 * @param h - virtual height
	 */
	public void setDims(float w, float h) {
		transform.setSize(w, h);
		projection = new Matrix4f().setOrtho(0, w, h, 0, 1, -1);
		update();
	}
	
	/**
	 * sets the projection view of the camera.
	 * 
	 * you need to do this after every transformation or else things won't render with the new camera transformation
	 */
	public void update() {
		projection.scale(transform.wScale, transform.hScale, 1, projectionView);
		projection.rotateZ(-transform.rotation, projectionView);
		projection.translate(-transform.x, -transform.y, 0, projectionView);
	}
	
	/**
	 * gets the model matrix from a certain transform
	 * 
	 * @param transform - the transform of the model
	 * 
	 * @return the model matrix in world coordiantes
	 */
	public Matrix4f getModelMatrix(Transform transform) {
		return new Matrix4f()
				.translate(transform.x + ((-transform.width * transform.wScale) / 2) + 2 * (transform.width / 2), transform.y + ((-transform.height * transform.hScale) / 2) + 2 * (transform.height / 2), 0)
				
				.rotateZ(transform.rotation)
				
				.translate(-(transform.width / 2), -(transform.height / 2), 0)
				
				.scale(transform.getWidth(), transform.getHeight(), 1);
	}
	
	/*
	public Matrix4f getModelMatrixRot(Transform transform) {
		return new Matrix4f()
				.translate(transform.x + transform.width / 2, transform.y + transform.height / 2, 0)
				
				.rotateZ(transform.rotation)
				
				.translate(-(transform.width / 2), -(transform.height / 2), 0)
				
				.scale(transform.getWidth(), transform.getHeight(), 1);
	}
	*/
	
	/**
	 * gets a model matrix based on manually inputted bounds.
	 * If you need something to be as exact as possible
	 * 
	 * @param left
	 * @param right
	 * @param up
	 * @param down
	 * 
	 * @return the model matrix in world coordiantes
	 */
	public Matrix4f getModelMatrix(float left, float right, float up, float down) {
		return new Matrix4f().translate(left, up, 0).scale(right - left, down - up, 1);
	}
	
	public Matrix4f getModelMatrixDims(float left, float up, float width, float height) {
		return new Matrix4f().translate(left, up, 0).scale(width , height, 1);
	}
	
	
	/**
	 * gets the mvp matrix from a certain model matrix
	 * 
	 * @param model - the matrix of the model
	 * 
	 * @return the mvp matrix in ndc
	 */
	public Matrix4f getModelViewProjectionMatrix(Matrix4f model) {
		return new Matrix4f(projectionView).mul(model);	
	}
	
	/**
	 * gets the view matrix from a certain model matrix for gui models.
	 * Basically like the camera is at position 0,0
	 * 
	 * @param model - the matrix of the model
	 * 
	 * @return the model projection matrix in gui ndc
	 */
	public Matrix4f getModelProjectionMatrix(Matrix4f model) {
		return new Matrix4f(projection).mul(model);
	}
	
	/**
	 * gets the model projection matrix width a ceratin width and height centered on the screen
	 * 
	 * @param w - the percent width that the model takes up of the screen
	 * @param h - the percent height that the model takes up of the screen
	 * 
	 * @return the model projection matrix in ndc coordinates
	 */
	public Matrix4f ndcFullMatrix(float w, float h) {
		return new Matrix4f().translate(-w, -h, 0).scale(2 * w, 2 * h, 1);
	}
	
	/**
	 * gets the model projection matrix filling up ndc coordinates
	 * 
	 * @return the model projection matrix in ndc coordinates
	 */
	public Matrix4f ndcFullMatrix() {
		return new Matrix4f().translate(-1, -1, 0).scale(2, 2, 1);
	}
	
	/**
	 * gets the camera's transform
	 * 
	 * @return the camera's transform
	 */
	public Transform getTransform() {
		return transform;
	}
}