package uk.fls.h2n0.main.core.entity.special;

import fls.engine.main.util.Point;
import uk.fls.h2n0.main.core.tiles.Tile;

public class Bubble extends Particle {

	
	
	private float rising;
	private int life;
	private int maxLife;
	public Bubble(int x,int y){
		this.pos = new Point(x,y);
		this.rising = -0.2f;
		this.life = 0;
		this.maxLife = (360 + (int)Math.floor(Math.random() * 180))/4;
	}
	
	public void init(){
		for(int i = 0; i < 5; i++){
			this.loadFrame("/images/bubbles.png", i, 4, 5);
		}
		this.currentFrame = 0;
	}
	
	public void update(){
		
		if(this.world.getTile(this.pos.getIX()/16, this.pos.getIY()/16) != Tile.water){
			die();
		}
		
		int sec = this.maxLife / 5;
		if(life >= 0 && life < sec){
			this.currentFrame = 0;
		}else if(life >= sec && life < sec * 2){
			this.currentFrame = 1;
		}else if(life >= sec * 3 && life < sec * 4){
			this.currentFrame = 2;
		}else if(life >= sec * 4 && life < sec * 5){
			this.currentFrame = 3;
		}else if(life >= sec * 5 && life < sec * 6){
			this.currentFrame = 4;
		}
		
		this.life++;
		
		int ny = (int)(this.pos.y + this.rising)/16;
		if(this.world.getTile(this.pos.getIX()/16, ny).canPassThrough())this.pos.y += this.rising;
		
		if(this.life > this.maxLife){
			this.die();
		}
	}
}
