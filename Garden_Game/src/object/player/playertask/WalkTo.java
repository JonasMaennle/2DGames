package object.player.playertask;

import framework.core.pathfinding.Node;
import framework.core.pathfinding.PathfinderService;
import object.TestShot;
import object.player.Player;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import java.awt.*;
import java.util.LinkedList;
import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;

public class WalkTo extends PlayerTask {

    private float x, y, velX, velY, speed;
    private LinkedList<Node> path;
    private float absx, absy;
    private PathfinderService pathfinderService;
    private Image image;
    private float collisionXCenter, collisionYCenter;
    private String movingDirection;
    private TestShot testShot;
    private boolean doComplexPathfinding, doSimplePathfinding;
    private float xCoord, yCoord;

    public WalkTo(float xCoord, float yCoord, Player player){
        super(player);
        this.speed = 1;
        this.x = player.getX();
        this.y = player.getY();
        this.absx = x;
        this.absy = y;
        this.image = quickLoaderImage("tiles/path");
        this.movingDirection = "right_front";
        this.doComplexPathfinding = false;
        this.doSimplePathfinding = false;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.pathfinderService = new PathfinderService(player.getHandler().getGraph(), player.getHandler());

        this.testShot = new TestShot(this, x+player.getWidth()/2, y + player.getHeight(), xCoord + 32, yCoord, 5, 5, 10);
        calculateDirection(x+player.getWidth()/2, y + player.getHeight(), (int)xCoord + 32, (int)yCoord);
    }

    @Override
    public void performTask() {
        x = player.getX();
        y = player.getY();
        //////////////////
        if(testShot != null) testShot.update();

        // normal path finding
        if(doSimplePathfinding){
            x += (velX * speed);
            y += (velY * speed);
        }
        if(doComplexPathfinding){
            doAStarPathfinding();
        }

        /////////////
        player.setShowIdle(false);
        player.setX(x);
        player.setY(y);
        checkPlayerDirection();
        player.setMovingDirection(this.movingDirection);
        // close task
        Rectangle target = new Rectangle((int)xCoord + 28, (int)yCoord - 2, 4, 4);
        if(player.getBounds().getBounds2D().intersects(target)){
            taskDone = true;
        }
    }

    private void doAStarPathfinding(){
        collisionXCenter = x + player.getWidth() / 2;
        collisionYCenter = y + player.getHeight() - 6;
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
            if(absx == collisionXCenter || collisionXCenter >= absx && collisionXCenter <= absx)
                velX = 0;

            if(absy > collisionYCenter)
                velY = 1;
            if(absy < collisionYCenter)
                velY = -1;
            if(absy == collisionYCenter || collisionYCenter >= absy && collisionYCenter <= absy)
                velY = 0;
        }else{
            velX = 0;
            velY = 0;
        }

        x += (velX * speed);
        y += ((velY * speed) / 2);

        // remove visited nodes

        if(path != null && path.size() > 0){
            Polygon p = new Polygon();
            Vector2f point = getIsometricCoordinates(path.get(path.size() - 1).getX(), path.get(path.size() - 1).getY());
            p.addPoint((int)point.x + (TILE_SIZE/2),(int)point.y + 12); // top center
            p.addPoint((int)point.x + (TILE_SIZE/2),(int)point.y + (TILE_SIZE/2)-12); // bottom center
            p.addPoint((int)point.x + 30,(int)point.y + (TILE_SIZE/4)); // left center
            p.addPoint((int)point.x + (TILE_SIZE) - 30, (int)point.y + (TILE_SIZE/4)); // right center

            if(p.getBounds2D().intersects(player.getBounds().getBounds2D())){
                path.remove(path.size() - 1);
            }
        }

        if(path.size() == 0){
            doComplexPathfinding = false;
            doSimplePathfinding = true;
            calculateDirection(x+player.getWidth()/2, y + player.getHeight(), (int)xCoord+32, (int)yCoord);
        }
    }

    private void checkPlayerDirection(){
        if(velY >= 0 && velY <= 1 && (velX <= 1 && velX >= 0)){
            movingDirection = "right_front";
        }
        if(velY >= 0 && velY <= 1 && (velX >= -1 && velX < 0)){
            movingDirection = "left_front";
        }
        if(velY < 0 && velY >= -1 && (velX <= 1 && velX >= 0)){
            movingDirection = "right_back";
        }
        if(velY < 0 &&  velY >= -1 && (velX >= -1 && velX < 0)){
            movingDirection = "left_back";
        }
    }

    private void calculateDirection(float playerX, float playerY, int destX, int destY) {
        float totalAllowedMovement = 1.0f;
        float xDistanceFromTarget = Math.abs(destX - playerX);
        float yDistanceFromTarget = Math.abs(destY - playerY);
        float totalDistanceFromTarget = xDistanceFromTarget + yDistanceFromTarget;
        float xPercentOfMovement = xDistanceFromTarget / totalDistanceFromTarget;

        velX = xPercentOfMovement;
        velY = totalAllowedMovement - xPercentOfMovement;

        if(destY < playerY)
            velY *= -1;

        if(destX < playerX)
            velX *= -1;
    }

    @Override
    public void renderTask() {
        if(path != null){
            //drawPath(path);
        }
        //if(testShot != null) testShot.draw();
    }

    private void drawPath(LinkedList<Node> tmp){
        for(int i = 0; i < tmp.size(); i++){
            Vector2f point = getIsometricCoordinates(tmp.get(i).getX(), tmp.get(i).getY());
            // System.out.println(tmp.get(i).getX() + "  :  " + tmp.get(i).getY());
            drawQuadImage(image, point.x, point.y, TILE_SIZE, TILE_SIZE/2);
        }
    }

    public void setTestShot(TestShot testShot) { this.testShot = testShot; }

    public void setDoComplexPathfinding(boolean doComplexPathfinding) {
        this.doComplexPathfinding = doComplexPathfinding;
        // create a path
        this.path = pathfinderService.getPath((int)x + player.getWidth()/2, (int)y + player.getHeight() - 6, (int)xCoord, (int)yCoord);
    }

    public void setDoSimplePathfinding(boolean doSimplePathfinding) {
        this.doSimplePathfinding = doSimplePathfinding;
    }


}