package uk.fls.h2n0.main.core.tiles;

import uk.fls.h2n0.main.core.World;
import uk.fls.h2n0.main.util.Renderer;

public class WaterTile extends Tile {

	public WaterTile(int id) {
		super(id);
	}

	@Override
	public void render(Renderer r, int dx, int dy, int x, int y, World w) {
		for(int xx = 0; xx < 16; xx++){
			for(int yy = 0; yy < 16; yy++){
				r.setPixel(dx + xx, dy + yy, Renderer.BLUE);
			}
		}
	}

}
