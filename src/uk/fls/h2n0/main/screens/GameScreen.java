package uk.fls.h2n0.main.screens;

import java.awt.Graphics;

import fls.engine.main.art.Art;
import fls.engine.main.screen.Screen;
import fls.engine.main.util.Camera;
import uk.fls.h2n0.main.core.World;
import uk.fls.h2n0.main.core.entity.Player;
import uk.fls.h2n0.main.util.Renderer;

public class GameScreen extends Screen {

	private Renderer rend;
	private Camera cam;
	private World world;
	private Player player;
	private int col;
	
	public void postInit(){
		this.rend = new Renderer(this.game.image);
		this.cam = new Camera(0,0);
		this.world = new World(this.rend,this.cam);
		this.cam.w = 64;
		this.cam.h = 64;
		this.col = 0;
	}
	
	
	@Override
	public void render(Graphics g) {
		//this.rend.fill(Renderer.BLUE);
		this.world.render();
		//this.rend.renderLeftText(this.player.getDepth()+"m", 64, 58);
		this.rend.renderText("having fun", 0, 0);
		finalizeRender(g);
	}

	@Override
	public void update() {
		this.world.update();
		
		if(this.input.isKeyHeld(this.input.w)){
			this.world.player.move(0, -1);
		}
		else if(this.input.isKeyHeld(this.input.s)){
			this.world.player.move(0, 1);
		}
		else if(this.input.isKeyHeld(this.input.a)){
			this.world.player.move(-1, 0);
		}
		else if(this.input.isKeyHeld(this.input.d)){
			this.world.player.move(1, 0);
		}
		
		if(this.input.isKeyPressed(this.input.space)){
			//this.world.player.grab();
			//Art.showImage(this.world.getImage());
		}
		
		//this.cam.center(this.player.getPos().getIX(), this.player.getPos().getIY(),8);
	}
	
	private void finalizeRender(Graphics g){
		this.rend.render();
		g.drawImage(this.rend.getImage(),0,0,null);
	}

}
