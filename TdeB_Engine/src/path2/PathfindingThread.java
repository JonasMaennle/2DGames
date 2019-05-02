package path2;

import static framework.helper.Collection.*;

import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

import framework.core.Handler;
import framework.core.StateManager;
import framework.core.StateManager.GameState;
import object.enemy.Enemy_Basic;

public class PathfindingThread extends Thread{

	CopyOnWriteArrayList<Enemy_Basic> object;
	private Graph graph;
	private Handler handler;
	private long t1, t2, t3, t4;
	public static boolean running = true;
	
	public PathfindingThread(CopyOnWriteArrayList<Enemy_Basic> object, Graph graph, Handler handler) 
	{
		this.object = object;
		this.graph = graph;
		t1 = System.currentTimeMillis();
		t2 = System.currentTimeMillis();
		this.handler = handler;
	}
	
	@Override
	public void run()
	{
		while(running)
		{		
			object = handler.enemyList;
			for(int i = 0; i < object.size(); i++)
			{
				Enemy_Basic tempObject = object.get(i);

				Enemy_Basic e = (Enemy_Basic) tempObject;	
				int enemyX = e.getNextX();
				int enemyY = e.getNextY();

				if(graph.getNodeID(enemyX, enemyY) != -1 && graph.getNodeID((int)handler.getCurrentEntity().getX()/TILE_SIZE, (int)handler.getCurrentEntity().getY()/TILE_SIZE) != -1)
				{
					t1 = System.currentTimeMillis();
					if(t1 - t2 > 50)
					{
						e.setPathLock(true);
						if(e != null){
							LinkedList<Node> tmp = graph.astar(graph.getNodeID(enemyX, enemyY), graph.getNodeID((int)handler.getCurrentEntity().getX()/TILE_SIZE , (int)handler.getCurrentEntity().getY()/TILE_SIZE));
							t3 = System.currentTimeMillis();
							if(tmp.size() < e.getEnemyNodesLeft() || t3 - t4 > 10)
							{
								e.setPath(tmp);
								t4 = t3;
							}							
						}
						e.setPathLock(false);
						t2 = t1;
					}		
				}		
			}
		}
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			
		if(StateManager.gameState == GameState.MAINMENU)
			running = false;
	}
}
