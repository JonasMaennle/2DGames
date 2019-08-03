package com.jonas.neon.framework;

import java.awt.image.BufferedImage;

import com.jonas.neon.window.BufferedImageLoader;

public class Texture {

	SpriteSheet bs, ps, ws, hud_l, hud_r, es, at, exps;
	
	private BufferedImage block_sheet = null;
	private BufferedImage player_sheet = null;
	private BufferedImage weapon_sheet = null;
	private BufferedImage HUD_left = null;
	private BufferedImage HUD_right = null;
	private BufferedImage enemy_sheet = null;
	private BufferedImage at_st_sheet = null;
	private BufferedImage expo_sheet = null;
	
	public BufferedImage[] block = new BufferedImage[8];
	public BufferedImage[] player = new BufferedImage[18];
	public BufferedImage[] weapon = new BufferedImage[6]; 
	public BufferedImage[] hud_left = new BufferedImage[1];
	public BufferedImage[] hud_right = new BufferedImage[1];
	public BufferedImage[] enemy = new BufferedImage[11];
	public BufferedImage[] lava = new BufferedImage[8];
	public BufferedImage[] flag = new BufferedImage[1];
	public BufferedImage[] at_st = new BufferedImage[32];
	public BufferedImage[] explo = new BufferedImage[22];
	
	public BufferedImage[] collectable = new BufferedImage[2];
	
	public Texture()
	{
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			block_sheet = loader.loadImage("/textures_sheet.png");
			player_sheet = loader.loadImage("/player_sheet.png");
			weapon_sheet = loader.loadImage("/weapon_sheet.png");
			HUD_left = loader.loadImage("/hud_left.png");
			HUD_right = loader.loadImage("/hud_right.png");
			enemy_sheet = loader.loadImage("/enemy_sheet.png");
			at_st_sheet = loader.loadImage("/at_st_sprite.png");
			expo_sheet = loader.loadImage("/explosion_sheet.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		bs = new SpriteSheet(block_sheet);
		ps = new SpriteSheet(player_sheet);
		ws = new SpriteSheet(weapon_sheet);
		es = new SpriteSheet(enemy_sheet);
		hud_l = new SpriteSheet(HUD_left);
		hud_r = new SpriteSheet(HUD_right);
		at = new SpriteSheet(at_st_sheet);
		exps = new SpriteSheet(expo_sheet);
		
		getTextures();
	}
	
	private void getTextures()
	{
		// Spalte / Zeile / Größe
		block[0] = bs.grabImage(1, 1, 32, 32); // dirt block
		block[1] = bs.grabImage(2, 1, 32, 32); // grass block

		block[4] = bs.grabImage(5, 1, 32, 32); // grass left
		block[5] = bs.grabImage(6, 1, 32, 32); // grass right
		block[6] = bs.grabImage(7, 1, 32, 32); // grass single
		
		block[7] = bs.grabImage(4, 1, 32, 32); // stone block
		
		player[0] = ps.grabImage(1, 1, 48, 96);
		player[1] = ps.grabImage(2, 1, 48, 96);
		player[2] = ps.grabImage(3, 1, 48, 96);
		player[3] = ps.grabImage(4, 1, 48, 96);	
		player[4] = ps.grabImage(5, 1, 48, 96);
		player[5] = ps.grabImage(6, 1, 48, 96);
		player[6] = ps.grabImage(7, 1, 48, 96);
		player[7] = ps.grabImage(8, 1, 48, 96);
		
		player[8] = ps.grabImage(1, 2, 48, 96);
		player[9] = ps.grabImage(2, 2, 48, 96);
		player[10] = ps.grabImage(3, 2, 48, 96);
		player[11] = ps.grabImage(4, 2, 48, 96);
		player[12] = ps.grabImage(5, 2, 48, 96);
		player[13] = ps.grabImage(6, 2, 48, 96);
		player[14] = ps.grabImage(7, 2, 48, 96);
		player[15] = ps.grabImage(8, 2, 48, 96);
		
		player[16] = ps.grabImage(1, 3, 48, 96);
		player[17] = ps.grabImage(2, 3, 48, 96);
		
		weapon[0] = ws.grabImage(1, 1, 48, 32);
		weapon[1] = ws.grabImage(2, 1, 48, 32);
		// laser
		weapon[2] = ws.grabImage(1, 3, 38, 32);
		
		weapon[3] = ws.grabImage(1, 2, 48, 32); // at_st weapon right
		weapon[4] = ws.grabImage(2, 2, 48, 32); // at_st weapon left
		
		// big laser
		weapon[5] = ws.grabImage(2, 3, 38, 32);
		
		// enemy left
		enemy[0] = es.grabImage(1, 1, 48, 80);
		enemy[1] = es.grabImage(2, 1, 48, 80);
		enemy[2] = es.grabImage(3, 1, 48, 80);
		enemy[3] = es.grabImage(4, 1, 48, 80);
		enemy[4] = es.grabImage(5, 1, 48, 80);
		enemy[5] = es.grabImage(6, 1, 48, 80); // grave stone
		// enemy right
		enemy[6] = es.grabImage(1, 2, 48, 80);
		enemy[7] = es.grabImage(2, 2, 48, 80);
		enemy[8] = es.grabImage(3, 2, 48, 80);
		enemy[9] = es.grabImage(4, 2, 48, 80);
		enemy[10] = es.grabImage(5, 2, 48, 80);
		
		// lasercrystal
		collectable[0] = bs.grabImage(1, 3, 32, 32);
		// health
		collectable[1] = bs.grabImage(2, 3, 32, 32);
		
		// load overlay / HUD
		hud_left[0] = hud_l.grabImage(1, 1, 200, 100);
		hud_right[0] = hud_r.grabImage(1, 1, 450, 100);
		
		// lava top
		lava[0] = bs.grabImage(1, 4, 32, 32);
		lava[1] = bs.grabImage(2, 4, 32, 32);
		lava[2] = bs.grabImage(3, 4, 32, 32);
		lava[3] = bs.grabImage(4, 4, 32, 32);
		
		// lava block
		lava[4] = bs.grabImage(1, 5, 32, 32);
		lava[5] = bs.grabImage(2, 5, 32, 32);
		lava[6] = bs.grabImage(3, 5, 32, 32);
		lava[7] = bs.grabImage(4, 5, 32, 32);
		
		// flag
		flag[0] = bs.grabImage(3, 1, 32, 96);
		
		// AT_ST_Walker
		
		// idle right
		at_st[0] = at.grabImage(1, 4, 150, 250);
		at_st[1] = at.grabImage(2, 4, 150, 250);
		at_st[2] = at.grabImage(3, 4, 150, 250);
		at_st[3] = at.grabImage(4, 4, 150, 250);
		
		// idle left
		at_st[4] = at.grabImage(1, 3, 150, 250);
		at_st[5] = at.grabImage(2, 3, 150, 250);
		at_st[6] = at.grabImage(3, 3, 150, 250);
		at_st[7] = at.grabImage(4, 3, 150, 250);
		
		// move left
		at_st[8] = at.grabImage(1, 1, 150, 250);
		at_st[9] = at.grabImage(2, 1, 150, 250);
		at_st[10] = at.grabImage(3, 1, 150, 250);
		at_st[11] = at.grabImage(4, 1, 150, 250);
		at_st[12] = at.grabImage(5, 1, 150, 250);
		at_st[13] = at.grabImage(6, 1, 150, 250);
		at_st[14] = at.grabImage(7, 1, 150, 250);
		at_st[15] = at.grabImage(8, 1, 150, 250);
		at_st[16] = at.grabImage(9, 1, 150, 250);
		at_st[17] = at.grabImage(10, 1, 150, 250);
		at_st[18] = at.grabImage(11, 1, 150, 250);
		at_st[19] = at.grabImage(12, 1, 150, 250);
		
		// move right
		at_st[20] = at.grabImage(1, 2, 150, 250);
		at_st[21] = at.grabImage(2, 2, 150, 250);
		at_st[22] = at.grabImage(3, 2, 150, 250);
		at_st[23] = at.grabImage(4, 2, 150, 250);
		at_st[24] = at.grabImage(5, 2, 150, 250);
		at_st[25] = at.grabImage(6, 2, 150, 250);
		at_st[26] = at.grabImage(7, 2, 150, 250);
		at_st[27] = at.grabImage(8, 2, 150, 250);
		at_st[28] = at.grabImage(9, 2, 150, 250);
		at_st[29] = at.grabImage(10, 2, 150, 250);
		at_st[30] = at.grabImage(11, 2, 150, 250);
		at_st[31] = at.grabImage(12, 2, 150, 250);
		
		// explosion
		explo[0] = exps.grabImage(1, 1, 64, 64);
		explo[1] = exps.grabImage(2, 1, 64, 64);
		explo[2] = exps.grabImage(3, 1, 64, 64);
		explo[3] = exps.grabImage(4, 1, 64, 64);
		explo[4] = exps.grabImage(5, 1, 64, 64);
		explo[5] = exps.grabImage(6, 1, 64, 64);
		explo[6] = exps.grabImage(7, 1, 64, 64);
		explo[7] = exps.grabImage(8, 1, 64, 64);
		
		explo[8] = exps.grabImage(1, 2, 64, 64);
		explo[9] = exps.grabImage(2, 2, 64, 64);
		explo[10] = exps.grabImage(3, 2, 64, 64);
		explo[11] = exps.grabImage(4, 2, 64, 64);
		explo[12] = exps.grabImage(5, 2, 64, 64);
		explo[13] = exps.grabImage(6, 2, 64, 64);
		explo[14] = exps.grabImage(7, 2, 64, 64);
		explo[15] = exps.grabImage(8, 2, 64, 64);
		
		explo[16] = exps.grabImage(1, 3, 64, 64);
		explo[17] = exps.grabImage(2, 3, 64, 64);
		explo[18] = exps.grabImage(3, 3, 64, 64);
		explo[19] = exps.grabImage(4, 3, 64, 64);
		explo[20] = exps.grabImage(5, 3, 64, 64);
		explo[21] = exps.grabImage(6, 3, 64, 64);
	}
}
