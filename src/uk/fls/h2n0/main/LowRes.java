package uk.fls.h2n0.main;

import java.awt.image.BufferedImage;

import fls.engine.main.Init;
import fls.engine.main.input.Input;
import uk.fls.h2n0.main.screens.GameScreen;

@SuppressWarnings("serial")
public class LowRes extends Init{
	
	
	public final static int w = 64;
	public final static int scale = 10;
	public LowRes(){
		super("Lowres test",w * scale,w * scale);
		showFPS();
		useCustomBufferedImage(w, w, BufferedImage.TYPE_INT_RGB);
		setInput(new Input(this, Input.KEYS));
		setDestieredAmtOfTicks(60);
		skipInit();
		setScreen(new GameScreen());
		start();
	}
	
	public static void main(String[] args){
		new LowRes();
	}

}
