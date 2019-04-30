package helper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

public class TileImageStorage {
	
	private LinkedList<Image> spriteList = new LinkedList<>();
	private int width, height;
	private String path;
	
	public TileImageStorage(int width, int height, int maxWidth, int maxHeight, String path){
		this.spriteList = new LinkedList<>();
		this.width = width;
		this.height = height;
		this.path = path;
		
		// fill list with images from path
		fillList(maxWidth, maxHeight, width, height, path);
	}
	
	public void addImage(Image image){
		spriteList.add(image);
	}
	
	public Image getImage(int index){
		return spriteList.get(index);
	}
	
	public int getTileCount(){
		return spriteList.size();
	}
	
	private void fillList(int maxWidth, int maxHeight, int width, int height, String path) {
		
		BufferedImage buf = null;
        try {
            buf = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        Texture tmpTex;
        Image tmpImage;
        
        for(int y = 0; y < maxHeight; y+=height)
        {
            for(int x = 0; x < maxWidth; x+=width)
            {
            	try {
    				tmpTex = BufferedImageUtil.getTexture("" + x*y, buf.getSubimage(x, y, width, height));
    				tmpImage = new Image(tmpTex);
    					
    				// add to list
    				spriteList.add(tmpImage);
    				tmpImage = null;
    			} catch (IOException e) {
    				e.printStackTrace();
    			}	
            }
        }
	}

	public LinkedList<Image> getSpriteList() {
		return spriteList;
	}

	public void setSpriteList(LinkedList<Image> spriteList) {
		this.spriteList = spriteList;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
