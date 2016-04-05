package uk.fls.h2n0.main.core.tiles;

import uk.fls.h2n0.main.core.World;
import uk.fls.h2n0.main.util.Renderer;

public class VoidTile extends Tile {

	public VoidTile(int id) {
		super(id);
		this.canPass = false;
	}

	@Override
	public void render(Renderer r, int dx, int dy, int x, int y, World w) {
		
	}

}
