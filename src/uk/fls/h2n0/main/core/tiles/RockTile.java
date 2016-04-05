package uk.fls.h2n0.main.core.tiles;

import uk.fls.h2n0.main.core.World;
import uk.fls.h2n0.main.util.Renderer;

public class RockTile extends Tile {

	public RockTile(int id) {
		super(id);
		this.canPass = false;
	}

	@Override
	public void render(Renderer rend, int dx, int dy, int x, int y, World w) {
		int cx = 1;
		int cy = 1;
		
		boolean u = w.getTile(x, y-1) == Tile.rock || w.getTile(x, y-1) == Tile.voidTile;
		boolean d = w.getTile(x, y+1) == Tile.rock || w.getTile(x, y+1) == Tile.voidTile;
		boolean l = w.getTile(x-1, y) == Tile.rock || w.getTile(x-1, y) == Tile.voidTile;
		boolean r = w.getTile(x+1, y) == Tile.rock || w.getTile(x+1, y) == Tile.voidTile;
		
		if(!u)cy--;
		if(!d)cy++;
		if(!l)cx--;
		if(!r)cx++;
		
		if(!u && !d && !l && !r){
			cy = 0;
			cx = 3;
		}
		
		for(int xx = 0; xx < 16; xx++){
			for(int yy = 0; yy < 16; yy++){
				rend.setPixel(dx + xx, dy + yy, w.tileImages[cx + cy * 4][xx + yy * 16]);
			}
		}
	}

}
