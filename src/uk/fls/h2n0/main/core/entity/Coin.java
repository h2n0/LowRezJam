package uk.fls.h2n0.main.core.entity;

import fls.engine.main.util.Point;

public class Coin extends Entity {
	
	
	public int animTime;
	public int[] displyPatrn;
	public int cf;
	public Coin(int x,int y){
		y += 4;
		x += (16 - 1 - 7)/2;
		this.pos = new Point(x,y);
	}
	
	public void init(){
		this.animTime = 0;
		for(int i = 0; i < 3; i++){
			this.loadFrame("/images/coin.png", i, 8, 3);
		}
		this.displyPatrn = new int[]{0,1,2,1};
		this.cf = 0;
	}
	
	@Override
	public void update(){
		if(this.animTime++ == 30){
			this.cf++;
			this.cf %= this.displyPatrn.length;
			this.currentFrame = this.displyPatrn[this.cf];
			this.animTime = 0;
		}
	}

}
