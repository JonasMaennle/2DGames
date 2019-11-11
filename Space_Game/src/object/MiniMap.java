package object;

import org.newdawn.slick.Image;

import framework.core.Handler;
import object.enemy.Enemy_Basic;

import static framework.helper.Graphics.*;

public class MiniMap {
	
	private Image image, playerImage, minimap_background;
	private Handler handler;
	private int x, y, width, height;
	private float scaling;
	
	private int realCenterX, realCenterY, maxRange, mapCenterX, mapCenterY;

	public MiniMap(int x, int y, int width, int height, float scaling, Handler handler) {
		this.handler = handler;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.mapCenterX = x + width/2;
		this.mapCenterY = y + height/2;
		this.image = quickLoaderImage("hud/minimap");
		this.playerImage = quickLoaderImage("hud/minimap_player");
		this.minimap_background = quickLoaderImage("hud/minimap_background");
		this.scaling = scaling;
		
		maxRange = (int) (scaling * width);
	}
	
	public void update() {
		realCenterX = (int) handler.getPlayer().getX();
		realCenterY = (int) handler.getPlayer().getY();
	}
	
	public void draw() {
		// background
		drawQuadImageStatic(minimap_background, x, y, width, height);
		
		// draw enemy
		for(Enemy_Basic enemy : handler.getEnemyList()) {
			//System.out.println(calcTotalDistanceFromTarget(realCenterX, realCenterY, enemy.getX(), enemy.getY()));
			if(calcTotalDistanceFromTarget(realCenterX, realCenterY, enemy.getX(), enemy.getY()) <= maxRange) {
				float xDistanceFromTarget = (enemy.getX() - realCenterX) / scaling;
				float yDistanceFromTarget = (enemy.getY() - realCenterY) / scaling;
				
				drawSpotOnMap((int)(mapCenterX + xDistanceFromTarget), (int)(mapCenterY + yDistanceFromTarget), enemy.getImage(), enemy.getAngle());
			}
		}
		// player
		drawQuadImageStatic(playerImage, x + width/2 - 4, y + height/2 - 4, 8, 8);
		// map border
		drawQuadImageStatic(image, x, y, width, height);
	}
	
	private void drawSpotOnMap(int x, int y, Image image, float angle) {
		if(x > this.x + 2 && x < this.x + width - 2) {
			if(y > this.y + 2 && y < this.y + height - 2) {
				drawStaticImageRotCenter(image, x - ((0.4f / scaling) * 100), y - ((0.4f / scaling) * 100), (0.8f / scaling) * 100, (0.8f / scaling) * 100, angle);
			}
		}
	}
	
	private float calcTotalDistanceFromTarget(float centerX, float centerY, float enemyX, float enemyY) {
		float xDistanceFromTarget = Math.abs(enemyX - centerX);
		float yDistanceFromTarget = Math.abs(enemyY - centerY);
		return xDistanceFromTarget + yDistanceFromTarget;
	}
}
