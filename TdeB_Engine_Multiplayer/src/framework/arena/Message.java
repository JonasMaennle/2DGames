package framework.arena;

import java.io.Serializable;

public class Message implements Serializable{
	
	private static final long serialVersionUID = 5338735019137457867L;
	private MessageType type;
	private Object deliveryObject;
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
}
