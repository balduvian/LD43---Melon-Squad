package cnge.graphics.texture;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

public class Texture {
	
	private int id;
	
	private int width;
	private int height;
	
	/**
	 * the ultimate texture contructor, with full customizablility
	 * 
	 * @param tp - the texture preset
	 * 
	 * @return a new texture
	 */
	public Texture(String path, TexturePreset tp) {
		init(path, tp.clampHorz, tp.clampVert, tp.nearest);
	}
	
	public Texture(String path) {
		init(path, TexturePreset.defaultClampHorz, TexturePreset.defaultClampVert, TexturePreset.defaultNearest);
	}
	
	protected void init(String p, boolean ch, boolean cv, boolean n) {
		BufferedImage b = null;
		try {
			b = ImageIO.read(new File(p));
		}catch(IOException ex) {
			ex.printStackTrace();
			System.err.println("TEXTURE NOT FOUND, resolving to placeholder");
			try {
				b = ImageIO.read(new File("res/cnge/missing.png"));
			} catch (IOException ex2) {
				ex2.printStackTrace();
				System.err.println("SOMETHING WENT TERRIBLY, TERRIBLY WRONG");
			}
		}
		width = b.getWidth();
		height = b.getHeight();
		int[] pixels = b.getRGB(0, 0, width, height, null, 0, width);
		ByteBuffer buffer = BufferUtils.createByteBuffer(width*height*4);
		for(int i = 0; i < height; ++i) {
			for(int j = 0; j < width; ++j) {
				int pixel = pixels[i*width+j];
				buffer.put((byte)((pixel >> 16) & 0xff));
				buffer.put((byte)((pixel >>  8) & 0xff));
				buffer.put((byte)((pixel      ) & 0xff));
				buffer.put((byte)((pixel >> 24) & 0xff));
			}
		}
		buffer.flip();
		id = glGenTextures();
		bind();
		if(ch) {
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		}
		if(cv) {
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		}
		if(n) {
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		}else {
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		}
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		unbind();
	}

	/**
	 * create a texture from a byte buffer
	 * 
	 * @param bb - the fin byte buffer
	 */
	public Texture(int w, int h, ByteBuffer bb) {
		width = w;
		height = h;
		bb.flip();
		id = glGenTextures();
		bind();
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, bb);
		unbind();
	}
	
	/**
	 * the dummest of dummy textures. Unusable but fast
	 */
	public Texture() {
        id = glGenTextures();
    }
	
	/**
	 * a texture for frameBuffers
	 * 
	 * @param w - texture width
	 * @param h - texture height
	 * @param tp - texture parameters
	 */
	public Texture(int w, int h, TexturePreset tp) {
        width = w;
        height = h;
        id = glGenTextures();
        bind();
        if(tp.clampHorz) {
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		}
		if(tp.clampVert) {
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		}
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        if(tp.nearest) {
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		}else {
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		}
        unbind();
    }
	
	/**
	 * creates a blank texture with default texture presets
	 * 
	 * @param w
	 * @param h
	 */
	public Texture(int w, int h) {
        width = w;
        height = h;
        id = glGenTextures();
        bind();
        if(TexturePreset.defaultClampHorz) {
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		}
		if(TexturePreset.defaultClampVert) {
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		}
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        if(TexturePreset.defaultNearest) {
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		}else {
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		}
        unbind();
    }
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public static void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void destroy() {
		glDeleteTextures(id);
	}
	
	public int getId() {
		return id;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
