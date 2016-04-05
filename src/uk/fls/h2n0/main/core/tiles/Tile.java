package uk.fls.h2n0.main.core.tiles;

import uk.fls.h2n0.main.core.World;
import uk.fls.h2n0.main.core.entity.Entity;
import uk.fls.h2n0.main.util.Renderer;

public abstract class Tile {

	
	public static Tile[] tiles = new Tile[9];
	public static Tile voidTile =  new VoidTile(0);
	public static Tile rock = new RockTile(1);
	public static Tile water = new WaterTile(2);
	public static Tile sky = new SkyTile(3);
	
	
	public final byte id;
	protected boolean canPass;
	
	public Tile(int id){
		if(tiles[id] != null)throw new RuntimeException("Duplicate Ids: " + id);
		this.id = (byte)id;
		tiles[id] = this;
		this.canPass = true;
	}
	
	public void Interact(Entity e){
		
	}
	
	public boolean canPassThrough(){
		return this.canPass;
	}
	
	public abstract void render(Renderer r, int dx, int dy, int x, int y,World w);
}
