package path;

import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

import Gamestate.StateManager;
import Gamestate.StateManager.GameState;
import data.Handler;
import object.enemy.Enemy;
import object.enemy.EnemyBasic;

public class PathfindingThread extends Thread{

	CopyOnWriteArrayList<Enemy> object;
	private Graph graph;
	private Handler handler;
	private long t1, t2, t3, t4;
	public static boolean running = true;
	
	public PathfindingThread(CopyOnWriteArrayList<Enemy> object, Graph graph, Handler handler) 
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
			// oder update path wenn 200ms seit letztem update vergangen
			if(t1 - t2 > 100)
			{
				object = handler.enemyList;
				for(int i = 0; i < object.size(); i++)
				{
					Enemy tempObject = object.get(i);
					
					EnemyBasic e = (EnemyBasic) tempObject;	
						int enemyX = e.getNextX();
						int enemyY = e.getNextY();

						if(graph.getNodeID(enemyX, enemyY) != -1 && graph.getNodeID((int)handler.player.getX()/64, (int)handler.player.getY()/64) != -1)
						{
							//e.setPath(graph.astar(graph.getNodeID((int)e.getX()/64, (int)e.getY()/64), graph.getNodeID((int)handler.player.getX()/ 64, (int)handler.player.getY()/ 64)));
							if(e != null){
								LinkedList<Node> tmp = graph.astar(graph.getNodeID(enemyX, enemyY), graph.getNodeID((int)handler.player.getX()/64 , (int)handler.player.getY()/64));
								if(tmp.size() < e.getEnemyNodesLeft() || t3 - t4 > 100)
								{
									e.setPath(tmp);
									t4 = t3;
								}							
							}
							t3 = System.currentTimeMillis();
						}	
				}
				t2 = t1;
			}
			t1 = System.currentTimeMillis();
		
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(StateManager.gameState == GameState.MAINMENU)
			running = false;
	}
}
