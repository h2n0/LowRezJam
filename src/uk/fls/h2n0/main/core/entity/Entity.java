package uk.fls.h2n0.main.core.entity;

import java.awt.image.BufferedImage;

import fls.engine.main.art.SplitImage;
import fls.engine.main.util.Point;
import uk.fls.h2n0.main.core.World;

public abstract class Entity {

	
	public World world;
	protected int[][] frames;
	protected int currentFrame;
	protected Point pos;
	protected byte data;
	
	protected int w;
	
	protected boolean alive = true;
	
	public Entity(int x, int y){
		this.pos = new Point(x,y);
		this.currentFrame = 0;
		this.frames = new int[9][16 * 16];
		this.data = 0;
	}
	
	public Entity(){
		this(0,0);
	}
	
	public abstract void update();
	
	public void init(){
		
	}
	
	public Point getPos(){
		return this.pos;
	}
	
	public int[] getFrameData(){
		return this.frames[this.currentFrame];
	}
	
	protected void loadFrame(String place, int frame, int s,int w){
		if(frame > 9)throw new RuntimeException("Frame is above " + (w*w));
		
		this.w = s;
		BufferedImage img = new SplitImage(place).split(s,s)[frame % w][frame/w];
		this.frames[frame] = this.world.r.imageToColor(img);
	}
	
	protected void setPos(int x,int y){
		this.pos = new Point(x,y);
	}
	
	protected void setXFlag(){
		this.data |= 0x2;
	}
	
	protected void remXFlag(){
		if((this.data & 0x2) > 0){
			this.data ^= 0x2;
		}
	}
	
	public boolean hasXFlag(){
		return (this.data & 0x2) > 0;
	}
	
	public boolean isAlive(){
		return this.alive;
	}
	
	public int getWidth(){
		return this.w;
	}
	
	protected void die(){
		this.alive = false;
	}
}
