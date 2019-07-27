package framework.core;

import java.util.Random;

import framework.entity.EnemyType;
import object.collectable.Collectable_Ammo;
import object.collectable.Collectable_Flamethrower;
import object.collectable.Collectable_Health;
import object.collectable.Collectable_HelmetBattery;
import object.collectable.Collectable_LMG;
import object.collectable.Collectable_LaserShotgun;
import object.collectable.Collectable_Minigun;
import object.collectable.Collectable_Railgun;
import object.collectable.Collectable_Shotgun;

public class ItemSpawner {
	
	private enum Item{
		HP(0.1f), 
		ENERGY(0.6f), 
		AMMO(0.2f),
		FLAMETHROWER(0.08f), 
		LASERSHOTGUN(0.08f), 
		LMG(0.17f), 
		SHOTGUN(0.12f), 
		MINIGUN(0.05f), 
		RAILGUN(0.08f);
		
		private float possibility;
		
		Item(float possibility) {
			this.possibility = possibility;
		}
	}
	
	private Item currentItem;
	private Random rand;
	private Item[] items;
	private Handler handler;
	
	public ItemSpawner(Handler handler) {
		this.rand = new Random();
		this.handler = handler;
		
		items = Item.values();
	}
	
	public void spawnItem(int x, int y, EnemyType type) {
		int randomItem = rand.nextInt(items.length);
		currentItem = items[randomItem];
		
		float random = rand.nextFloat();
		// Item possibility * enemy possibilityFactor > random (0 - 1)
		if((currentItem.possibility * type.getPossibilityFactor()) > random) {
			System.out.println((currentItem.possibility * type.getPossibilityFactor()));
			switch (currentItem) {
			case HP:
				Collectable_Health hp = new Collectable_Health(x, y, 32, 32);
				handler.collectableList.add(hp);
				break;
			case ENERGY:
				Collectable_HelmetBattery eng = new Collectable_HelmetBattery(x, y, 32, 32);
				handler.collectableList.add(eng);
				break;
			case AMMO:
				Collectable_Ammo am = new Collectable_Ammo(x, y, 24, 24);
				handler.collectableList.add(am);
				break;
			case FLAMETHROWER:
				Collectable_Flamethrower fl = new Collectable_Flamethrower(x, y, 32, 16);
				handler.collectableList.add(fl);
				break;
			case LASERSHOTGUN:
				Collectable_LaserShotgun ls = new Collectable_LaserShotgun(x, y, 32, 16);
				handler.collectableList.add(ls);
				break;
			case LMG:
				Collectable_LMG lmg = new Collectable_LMG(x, y, 32, 16);
				handler.collectableList.add(lmg);
				break;
			case SHOTGUN:
				Collectable_Shotgun sho = new Collectable_Shotgun(x, y, 32, 16);
				handler.collectableList.add(sho);
				break;
			case MINIGUN:
				Collectable_Minigun min = new Collectable_Minigun(x, y, 32, 16);
				handler.collectableList.add(min);
				break;
			case RAILGUN:
				Collectable_Railgun rai = new Collectable_Railgun(x, y, 32, 16);
				handler.collectableList.add(rai);
				break;
			default:
				break;
			}
		}
	}
}
