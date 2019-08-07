package framework.multiplayer;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

import framework.core.Handler;
import framework.entity.MessageType;
import framework.path.Graph;
import object.Spawner;

public class Message implements Serializable{
	
	private static final long serialVersionUID = 5338735019137457867L;
	private MessageType type;
	private Object deliveryObject;
	private Handler handler;
	private Graph graph;
	private CopyOnWriteArrayList<Spawner> spawnPoints;
	private String deliveryObjectClassName;
	private long timeCreated;
	
	public Message(MessageType type) {
		this.type = type;
		this.timeCreated = System.currentTimeMillis();
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public Object getDeliveryObject() {
		return deliveryObject;
	}

	public void setDeliveryObject(Object deliveryObject) {
		this.deliveryObject = deliveryObject;
	}

	public String getDeliveryObjectClassName() {
		return deliveryObjectClassName;
	}

	public void setDeliveryObjectClassName(String deliveryObjectClassName) {
		this.deliveryObjectClassName = deliveryObjectClassName;
	}

	public long getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(long timeCreated) {
		this.timeCreated = timeCreated;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public CopyOnWriteArrayList<Spawner> getSpawnPoints() {
		return spawnPoints;
	}

	public void setSpawnPoints(CopyOnWriteArrayList<Spawner> spawnPoints) {
		this.spawnPoints = spawnPoints;
	}
}
