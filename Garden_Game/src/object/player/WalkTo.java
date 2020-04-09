package object.player;

import framework.core.pathfinding.Node;
import framework.core.pathfinding.PathfinderService;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import java.awt.*;
import java.awt.geom.Area;
import java.util.LinkedList;

import static framework.helper.Collection.TILE_SIZE;
import static framework.helper.Collection.getIsometricCoordinates;
import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.quickLoaderImage;

public class WalkTo extends PlayerTask{

    private int coordX, coordY;
    private float x, y, velX, velY, speed;
    private LinkedList<Node> path;
    private LinkedList<Node> visited;
    private float absx, absy;
    private PathfinderService pathfinderService;
    private Image image;
    private float collisionXCenter, collisionYCenter;
    private String movingDirection;

    public WalkTo(float coordX, float coordY, Player player){
        super(player);
        this.coordX = (int) coordX;
        this.coordY = (int) coordY;
        this.visited = new LinkedList<>();
        this.speed = 1;
        this.x = player.getX();
        this.y = player.getY();
        this.absx = x;
        this.absy = y;
        this.pathfinderService = new PathfinderService(player.getHandler().getGraph(), player.getHandler());
        this.path = pathfinderService.getPath(this, this.coordX, this.coordY);
        this.image = quickLoaderImage("tiles/path");
        this.movingDirection = "right_front";
    }

    @Override
    public void performTask() {
        x = player.getX();
        y = player.getY();
        collisionXCenter = x + player.getWidth() / 2;
        collisionYCenter = y + player.getHeight() - 6;
        //////////////////

        velX = 0;
        velY = 0;
        // System.out.println(path.size());
        if(path != null && path.size() > 0) {
            // System.out.println(path.get(path.size() - 1).getX());
            Vector2f point = getIsometricCoordinates(path.get(path.size() - 1).getX(), path.get(path.size() - 1).getY() );

            absx = point.x + (TILE_SIZE / 2);
            absy = point.y + (TILE_SIZE / 4);
            // System.out.println(path.size());
            //System.out.println(absx + "   " + x);
            if(absx > collisionXCenter)
                velX = 1;
            if(absx < collisionXCenter)
                velX = -1;
            if(absx == collisionXCenter || collisionXCenter >= absx - 4 && collisionXCenter <= absx + 4)
                velX = 0;

            if(absy > collisionYCenter)
                velY = 1;
            if(absy < collisionYCenter)
                velY = -1;
            if(absy == collisionYCenter || collisionYCenter >= absy - 4 && collisionYCenter <= absy + 4)
                velY = 0;
        }else{
            velX = 0;
            velY = 0;
        }
        // check player direction
        if(velY == 1 && (velX == 1 || velX == 0)){
            movingDirection = "right_front";
        }
        if(velY == 1 && velX == -1){
            movingDirection = "left_front";
        }
        if(velY == -1 && (velX == 1 || velX == 0)){
            movingDirection = "right_back";
        }
        if(velY == -1 && velX == -1){
            movingDirection = "left_back";
        }

        x += (velX * speed);
        y += ((velY * speed) / 2);

        // remove visited nodes
        if(path != null && path.size() > 0){
            Polygon p = new Polygon();
            Vector2f point = getIsometricCoordinates(path.get(path.size() - 1).getX(), path.get(path.size() - 1).getY());
            p.addPoint((int)point.x + (TILE_SIZE/2),(int)point.y + 8); // top center
            p.addPoint((int)point.x + (TILE_SIZE/2),(int)point.y + (TILE_SIZE/2)-8); // bottom center
            p.addPoint((int)point.x + 16,(int)point.y + (TILE_SIZE/4)); // left center
            p.addPoint((int)point.x + (TILE_SIZE) - 16, (int)point.y + (TILE_SIZE/4)); // right center

            Area r = new Area(p);
            Area x = new Area(player.getBounds());

            if(p.getBounds2D().intersects(player.getBounds().getBounds2D())){
                path.remove(path.size() - 1);
            }
            player.setShowIdle(false);
        }

        /////////////
        player.setX(x);
        player.setY(y);
        player.setMovingDirection(this.movingDirection);
    }

    @Override
    public void renderTask() {
        /*
        if(path != null)
            drawPath(path);
         */
    }

    private void drawPath(LinkedList<Node> tmp){
        for(int i = 0; i < tmp.size(); i++){
            Vector2f point = getIsometricCoordinates(tmp.get(i).getX(), tmp.get(i).getY());
            // System.out.println(tmp.get(i).getX() + "  :  " + tmp.get(i).getY());
            drawQuadImage(image, point.x, point.y, TILE_SIZE, TILE_SIZE/2);
        }
    }
}
