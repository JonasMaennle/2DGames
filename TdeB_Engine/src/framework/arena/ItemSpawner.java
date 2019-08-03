package framework.arena;

import java.util.Random;

import framework.core.Handler;
import framework.entity.EnemyType;
import framework.helper.Collection;
import object.collectable.Collectable_Ammo;
import object.collectable.Collectable_Flamethrower;
import object.collectable.Collectable_Health;
import object.collectable.Collectable_HelmetBattery;
import object.collectable.Collectable_Icethrower;
import object.collectable.Collectable_LMG;
import object.collectable.Collectable_LaserShotgun;
import object.collectable.Collectable_Minigun;
import object.collectable.Collectable_Railgun;
import object.collectable.Collectable_Shield;
import object.collectable.Collectable_Shotgun;
import object.collectable.Collectable_SpeedForWeapon;
import object.collectable.Collectable_SpeedUp;

public class ItemSpawner {
	
	private enum Item{
		HP(0.2f), 
		ENERGY(0.6f), 
		AMMO(0.2f),
		FLAMETHROWER(0.1f), 
		LASERSHOTGUN(0.08f), 
		LMG(0.14f), 
		SHOTGUN(0.16f), 
		MINIGUN(0.07f), 
		RAILGUN(0.1f),
		SPEED(0.15f),
		ICETHROWER(0.1f),
		WEAPONSPEED(0.1f),
		SHIELD(0.15f);
		
		private float possibility;
		
		Item(float possibility) {
			this.possibility = possibility;
		}
	}
	
	private Item currentItem;
	private Random rand;
	private Item[] items;
	private Handler handler;
	
	private boolean lvl9Drop, lvl10Drop;
	
	public ItemSpawner(Handler handler) {
		this.rand = new Random();
		this.handler = handler;
		
		this.lvl9Drop = false;
		this.lvl10Drop = false;
		
		items = Item.values();
	}
	
	public void spawnItem(int x, int y, EnemyType type) {
		int randomItem = rand.nextInt(items.length);
		currentItem = items[randomItem];
		
		float random = rand.nextFloat();
		// Item possibility * enemy possibilityFactor > random (0 - 1)
		if((currentItem.possibility * type.getPossibilityFactor()) > random) {

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
			case SPEED:
				Collectable_SpeedUp speed = new Collectable_SpeedUp(x, y, 32, 32);
				handler.collectableList.add(speed);
				break;
			case ICETHROWER:
				Collectable_Icethrower ice = new Collectable_Icethrower(x, y, 32, 16);
				handler.collectableList.add(ice);
				break;
			case WEAPONSPEED:
				if(Collection.ARENA_CURRENT_WAVE >= 20) {
					Collectable_SpeedForWeapon speed4W = new Collectable_SpeedForWeapon(x, y, 24, 24);
					handler.collectableList.add(speed4W);
				}
				break;
			case SHIELD:
				if(Collection.ARENA_CURRENT_WAVE >= 15) {
					Collectable_Shield shield = new Collectable_Shield(x, y, 32, 32);
					handler.collectableList.add(shield);
				}
				break;	
			default:
				break;
			}
		}else {
			specialAirdrop(x, y);
		}
	}
	
	private void specialAirdrop(int x, int y) {
		// Level 9 drop - Shotgun
		if((Collection.ARENA_CURRENT_WAVE - 1) == 9 && !lvl9Drop){
			lvl9Drop = true;
			Collectable_Shotgun sho = new Collectable_Shotgun(x, y, 32, 16);
			handler.collectableList.add(sho);
		}
		// Level 10 Airdrop - engery stone
		if((Collection.ARENA_CURRENT_WAVE - 1) == 10 && !lvl10Drop){
			lvl10Drop = true;
			Collectable_HelmetBattery eng = new Collectable_HelmetBattery(x, y, 32, 32);
			handler.collectableList.add(eng);
		}
	}
}
