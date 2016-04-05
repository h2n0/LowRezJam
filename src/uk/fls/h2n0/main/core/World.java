package uk.fls.h2n0.main.core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import fls.engine.main.art.SplitImage;
import fls.engine.main.util.Camera;
import fls.engine.main.util.Point;
import uk.fls.h2n0.main.core.entity.Coin;
import uk.fls.h2n0.main.core.entity.Entity;
import uk.fls.h2n0.main.core.entity.Player;
import uk.fls.h2n0.main.core.tiles.Tile;
import uk.fls.h2n0.main.util.Renderer;

public class World {

	private List<Entity> entitys;
	public Renderer r;
	private Camera cam;
	public Player player;
	private byte[] tiles;
	public int[][] tileImages;
	
	private int w;
	private int h;
	
	
	public World(Renderer r, Camera cam){
		this.entitys = new ArrayList<Entity>();
		this.r = r;
		this.cam = cam;
		
		
		int xTiles = 4;
		this.tileImages = new int[xTiles * xTiles][16 * 16];
		BufferedImage[][] t = new SplitImage("/images/tiles.png").split(16, 16);
		for(int i = 0; i < this.tileImages.length; i++){
			this.tileImages[i] = r.imageToColor(t[i % xTiles][i / xTiles]);
		}
		loadWorld("level2");
	}
	
	public void render(){
		int cx = cam.pos.getIX();
		int cy = cam.pos.getIY();
		
		for (int x = 0; x <= this.w; x++) {
			for (int y = 0; y <= this.h; y++) {
				int px = (x * 16) - cx;
				int py = (y * 16) - cy;
				if(onScreen(px,py)){
					getTile(x, y).render(r, px, py, x, y, this);
				}
			}
		}
		
		
		
		
		for(int i = 0; i < this.entitys.size(); i++){
			Entity e = this.entitys.get(i);
			Point pos = e.getPos();
			if(!this.onScreen(pos))continue;
			int x = pos.getIX() - cx;
			int y = pos.getIY() - cy;
			int[] data = e.getFrameData();
			int w = e.getWidth();
			for(int xx = 0; xx < w; xx++){
				for(int yy = 0; yy < w; yy++){
					if(!e.hasXFlag()){
						this.r.setPixel(x+xx, y+yy, data[xx + yy * w]);
					}else{
						this.r.setPixel(x+(16-xx), y+yy, data[xx + yy * w]);
					}
				}
			}
		}
	}
	
	public void update(){
		for(int i = 0; i < this.entitys.size(); i++){
			Entity e = this.entitys.get(i);
			if(!this.onScreen(e.getPos()))continue;
			e.update();
			if(!e.isAlive()){
				this.entitys.remove(e);
			}
		}
		
		if(this.player != null){
			this.cam.center(this.player.getPos().getIX(), this.player.getPos().getIY(), 8);
		}
	}
	
	public Entity addEntity(Entity e){
		this.entitys.add(e);
		e.world = this;
		e.init();
		return e;
	}
	
	private boolean onScreen(int x, int y){
		if(x < -16 || y < -16 || x >= 64 || y >= 64)return false;
		else return true;
	}
	
	private boolean onScreen(Point p){
		return this.onScreen(p.getIX()/16,p.getIY()/16);
	}
	
	private void loadWorld(String name){
		BufferedImage levelImage = new SplitImage("/images/levels/"+name+".png").load();
		int ww = levelImage.getWidth();
		int wh = levelImage.getHeight();
		this.w = ww;
		this.h = wh;
		this.cam.ww = ww * 16;
		this.cam.wh = wh * 16;
		this.tiles = new byte[ww * wh];
		int[] levelData = new int[ww * wh];
		levelImage.getRGB(0, 0, ww, wh, levelData, 0, ww);
		for(int i = 0; i < ww * wh; i++){
			int x = i % ww;
			int y = i / ww;
			int col = this.r.hexToCol(levelData[i]);
			if(col == Renderer.BLACK){
				this.tiles[x + y * this.w] = Tile.rock.id;
			}else if(col == Renderer.BLUE){
				this.tiles[x + y * this.w] = Tile.water.id;
			}else if(col == Renderer.CYAN){
				this.tiles[x + y * this.w] = Tile.sky.id;
			}else if(col == Renderer.YELLOW){
				this.tiles[x + y * this.w] = Tile.water.id;
				this.addEntity(new Coin(x * 16,y * 16));
			}else if(col == Renderer.RED){
				if(this.player == null){
					this.player = (Player)this.addEntity(new Player(x * 16,y * 16));
				}
				this.tiles[x + y * this.w] = Tile.water.id;
			}
		}
	}
	
	private boolean isValid(int x, int y){
		if(x < 0 || y < 0 || x >= this.w || y >= this.h)return false;
		else return true;
	}
	
	public Tile getTile(int x, int y){
		if(!isValid(x,y))return Tile.voidTile;
		else return Tile.tiles[this.tiles[x + y * this.w]];
	}
	
	public BufferedImage getImage(){
		BufferedImage res = null;
		int[] pix = new int[this.w * this.h];
		for(int i = 0; i < pix.length; i++){
			int tx = i % this.w;
			int ty = i / this.w;
			Tile t = this.getTile(tx, ty);
			
			if(t == Tile.rock){
				pix[tx + ty * this.w] = 0xCCCCCC;
			}else if(t == Tile.water){
				pix[tx + ty * this.w] = 0x0000CC;
			}else{
				pix[tx + ty * this.w] = 0x000000;
			}
		}
		
		res = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		res.setRGB(0, 0, w, h, pix, 0, w);
		return res;
	}
}
