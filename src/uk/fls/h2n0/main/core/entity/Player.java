package uk.fls.h2n0.main.core.entity;

import fls.engine.main.util.Point;
import uk.fls.h2n0.main.core.entity.special.Bubble;

public class Player extends Entity{
	
	
	private int state;
	private int anim;
	private int animTime;
	private int grabTime;
	private int cGrabTime;
	private boolean grabing;
	private int idleTime;
	private int bubbleTime;
	
	private float tx,ty;
	
	private boolean canMove;
	
	public Player(int x,int y){
		this.pos = new Point(x,y);
	}
	
	public void init(){
		this.anim = 0;
		this.state = 0;
		this.animTime = 0;
		this.grabTime = 60;
		this.cGrabTime = 0;
		this.grabing = false;
		for(int i = 0; i < 9; i++)this.loadFrame("/images/player.png", i, 16, 3);
		this.canMove = true;
		this.tx = this.pos.getIX();
		this.ty = this.pos.getIY();
		this.idleTime = 0;
		resetBubbleTime();
	}

	@Override
	public void update() {
		boolean moving = !canMove;
		if(moving){
			float cx = this.pos.x / 16;
			float cy = this.pos.y / 16;
			
			float speed = (7/60f)/2f;
			
			if(Math.abs(cx - this.tx) > 0.2){
				if(cx < this.tx){
					cx += speed;
				}
				if(cx > this.tx){
					cx -= speed;
				}
				
				this.pos.x = cx * 16;
			}else{
				this.pos.x = tx * 16;
			}
			
			if(Math.abs(cy - this.ty) > 0.2){
				if(cy < this.ty){
					cy += speed;
				}
				if(cy > this.ty){
					cy -= speed;
				}
				
				this.pos.y = cy * 16;
			}else{
				this.pos.y = ty * 16;
			}
			
			if(this.pos.x == this.tx * 16 && this.pos.y == this.ty * 16){
				this.canMove = true;
			}
		}else{
			this.idleTime++;
			if(this.idleTime >= 2 * 60){//2 Seconds
				move(0,0);
			}
		}
		
		if(bubbleTime-- == 0){
			int amt = (int)Math.floor(Math.random() * 5);
			for(int i = 0; i < amt; i++){
				int xOff = (int)Math.floor(Math.random() * 8);
				int ex = !this.hasXFlag()?16 + xOff:-xOff;
				int ey = 10 + (int)Math.floor(Math.random() * 4);;
				this.world.addEntity(new Bubble(this.pos.getIX() + ex, this.pos.getIY() + ey));
			}
			resetBubbleTime();
		}
		updateFrame();
	}
	
	public void move(int x, int y){
		if(this.canMove){
			this.idleTime = 0;
			if(y == 0 && x == 0){
				this.state = 0;
				return;
			}
			
			if(y == 0){
				this.state = 0;
			}
			
			if(y < 0)this.state = 1;
			else if(y > 0)this.state = 2;
			
			if(x < 0)remXFlag();
			else if(x > 0)setXFlag();
			
			
			int cx = pos.getIX();
			int cy = pos.getIY();
			int nx = (cx / 16) + x;
			int ny = (cy / 16) + y;
			if(this.world.getTile(nx, ny).canPassThrough()){
				this.tx = nx;
				this.ty = ny;
				this.canMove = false;
			}
		}
	}
	
	public void grab(){
		this.idleTime = 0;
		this.grabing = true;
		this.anim = 2;
	}
	
	
	private void updateFrame(){
		if(!grabing){
			if(this.animTime-- == 0){
				this.anim ++;
				this.anim %= 2;
				this.animTime = 10;
			}
		}else{
			int sec = this.grabTime / 4;
			if(this.cGrabTime >= 0 && this.cGrabTime < sec){
				this.state = 0;
			}else if(this.cGrabTime >= sec && this.cGrabTime < sec * 2){
				this.state = 1;
			}else if(this.cGrabTime >= sec * 2 && this.cGrabTime < sec * 3){
				this.state = 2;
			}else if(this.cGrabTime >= sec * 3 && this.cGrabTime < sec * 4){
				this.state = 1;
			}else{
				this.state = 0;
				this.grabing = false;
				this.cGrabTime = 0;
			}
			this.cGrabTime++;
		}
		this.currentFrame = this.state + this.anim * 3;
	}
	
	public int getDepth(){
		int res = this.pos.getIY() / 16; 
		if(res < 0)res = 0;
		return res;
	}
	
	private void resetBubbleTime(){
		this.bubbleTime = (int)(10 + (Math.floor(Math.random() * 60) * 3));
	}
}
