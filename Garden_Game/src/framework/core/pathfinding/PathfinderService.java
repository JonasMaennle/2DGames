package framework.core.pathfinding;

import java.util.LinkedList;
import framework.core.Handler;
import framework.helper.Leveler;
import object.player.Player;
import object.player.PlayerTask;
import object.player.WalkTo;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import static framework.helper.Collection.*;
import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.quickLoaderImage;

public class PathfinderService {

	private Graph graph;
	private Handler handler;
	
	public PathfinderService(Graph graph, Handler handler) {
		this.graph = graph;
		this.handler = handler;
	}

	public LinkedList<Node> getPath(WalkTo walkTo, int targetX, int targetY) {
		LinkedList<Node> tmp = new LinkedList<>();
		try {
			int playerX = (int)(walkTo.getPlayer().getX());
			int playerY = (int)(walkTo.getPlayer().getY());

			Vector2f point = convertObjectCoordinatesToIsometricGrid(playerX, playerY);
			Vector2f mousePoint = convertMouseCoordsToIsometricGrid(targetX, targetY);

			// System.out.println("Player Tile: " + point.x + " y: " + point.y);
			// System.out.println("Target Tile: " + targetX + " y: " + targetY);

			if(graph.getNodeID((int)point.x, (int)point.y) == -1){
				Vector2f altPoint = findEmergencyTile(playerX, playerY);
				point = altPoint;
				// System.out.println("Alternative Tile for Player found!");
			}

			if(graph.getNodeID((int)point.x, (int)point.y) != -1 && graph.getNodeID((int)mousePoint.x, (int)mousePoint.y) != -1) {
				// add original mouse target position
				// tmp.add(new Node(targetX, targetY));
				tmp.addAll(graph.astar(graph.getNodeID((int)point.x, (int)point.y), graph.getNodeID((int)mousePoint.x, (int)mousePoint.y)));

			}else{
				// System.out.println("!!! no path found !!!");
				// System.out.println("Player Tile: " + point.x + " y: " + point.y);
				// System.out.println("Target Tile: " + targetX + " y: " + targetY);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return tmp;
	}

	private Vector2f findEmergencyTile(int playerX, int playerY){
		Vector2f point = convertObjectCoordinatesToIsometricGrid(playerX, playerY);
		if(graph.getNodeID((int)point.x, (int)point.y-1) != -1){ // top
			return new Vector2f((int)point.x, (int)point.y-1);
		}
		if(graph.getNodeID((int)point.x, (int)point.y+1) != -1){ // bottom
			return new Vector2f((int)point.x, (int)point.y+1);
		}
		if(graph.getNodeID((int)point.x-1, (int)point.y) != -1){ // left
			return new Vector2f((int)point.x-1, (int)point.y);
		}
		if(graph.getNodeID((int)point.x+1, (int)point.y) != -1){ // right
			return new Vector2f((int)point.x+1, (int)point.y);
		}
		System.out.println("No alternative Tile found!");
		return null;
	}
}
