package uk.fls.h2n0.main.util;

import java.awt.image.BufferedImage;

import fls.engine.main.art.SplitImage;

public class Renderer {

	private BufferedImage img;
	private int w,h;
	private int[] pixles;
	private int[] pallet;
	private boolean[] dirtyFlags;
	private final String letters = "abcdefghijklmnopqrstuvwxyz"+"1234567890 ";
	private BufferedImage[][] font;
	

	
	public static int BLACK = 0;
	public static int BLUE = 1;
	public static int GREEN = 2;
	public static int CYAN = 3;
	public static int RED = 4;
	public static int PINK = 5;
	public static int YELLOW = 6;
	public static int WHITE = 7;
	
	private final boolean optz = true;
	
	public Renderer(BufferedImage image){
		this.img = image;
		this.h = this.w = image.getWidth();
		this.pixles = new int[this.w * this.h];
		setupColor();
		this.dirtyFlags = new boolean[this.h];
		this.font = new SplitImage("/images/font2.png").split(4, 6);
	}
	
	private void setupColor(){
		this.pallet = new int[8];
		for(int i = 0; i < this.pallet.length; i++){
			int r = (i & 4) > 0 ? 255 : 0;
			int g = (i & 2) > 0 ? 255 : 0;
			int b = (i & 1) > 0 ? 255 : 0;
			int rgb = makeRGB(r, g, b);
			this.pallet[i] = rgb;
		}
	}
	
	public void setPixel(int x,int y,int shade){
		if(!isValid(x, y))return;
		if(shade < 0 || shade > 7)return;
		this.pixles[x + y * this.w] = this.pallet[shade];
		this.dirtyFlags[y] = true;
	}
	
	public void render(){
		if(optz){
			for(int y = 0; y < this.h; y++){
				if(this.dirtyFlags[y]){
					int[] row = getPixles(y);
					this.img.setRGB(0, y, w, 1, row, 0, w);
				}
			}
		}else{
			this.img.setRGB(0, 0, w, h, pixles, 0, w);
		}
		clearDirtyFlags();
	}
	
	private void clearDirtyFlags(){
		for(int i = 0; i < this.h; i++){
			this.dirtyFlags[i] = false;
		}
	}
	
	private int[] getPixles(int y){
		int[] res = new int[64];
		int c = 0;
		for(int i = 0; i < 64; i++){
			res[c] = this.pixles[i + y * this.w];
			c++;
		}
		return res;
	}
	
	public BufferedImage getImage(){
		return this.img;
	}
	
	private int makeRGB(int r,int g,int b){
		return (r << 16) | (g << 8) | b;
	}
	
	private boolean isValid(int x,int y){
		if(x < 0 || y < 0 || x >= this.w || y >= this.h)return false;
		else return true;
	}
	
	public void refresh(){
		for(int i = 0; i < this.dirtyFlags.length; i++){
			this.dirtyFlags[i] = true;
		}
	}
	
	public void fill(int shade){
		for(int i = 0; i < this.w * this.h; i++){
			int x = i % this.w;
			int y = i / this.w;
			setPixel(x, y, shade);
		}
	}
	
	private void renderSection(int x, int y, int col, int[] data){//Used for the font only
		for(int i = 0; i < data.length; i++){
			int d = data[i];
			int xx = i % 4;
			int yy = i / 4;
			if(d != -65316){
				setPixel(x + xx, y + yy, col);
			}
		}
	}
	
	public int hexToCol(int hex){
		for(int i = 0; i < this.pallet.length; i++){
			if(hexToRGB(hex) == this.pallet[i]){
				return i;
			}
		}
		
		if(hexToRGB(hex) ==  0x808080){
			return BLUE;
		}
		
		return -1;
	}
	
	private int hexToRGB(int hex){
		int r = (hex >> 16) & 0xFF;
		int g = (hex >> 8) & 0xFF;
		int b = (hex) & 0xFF;
		int rgb =  makeRGB(r,g,b);
		return rgb;
	}
	
	public void renderText(String msg,int x,int y,int col){
		msg = msg.toLowerCase();
		for(int i = 0; i < msg.length(); i++){
			String letter = msg.substring(i,i+1);
			if(letter.equals(" "))continue;
			int pos = this.letters.indexOf(letter.charAt(0));
			if(pos == -1)continue;
			int tx = pos % 26;
			int ty = pos / 26;
			int[] data = new int[4*6];
			this.font[tx][ty].getRGB(0, 0, 4, 6, data, 0, 4);
			renderSection(x + i * 5, y, col, data);
		}
	}
	
	public void renderLeftText(String msg,int x,int y,int col){
		msg = msg.toLowerCase();
		int off = msg.length() * 5;
		x -= off;
		for(int i = 0; i < msg.length(); i++){
			String letter = msg.substring(i,i+1);
			int pos = this.letters.indexOf(letter.charAt(0));
			if(pos == -1)continue;
			int tx = pos % 26;
			int ty = pos / 26;
			int[] data = new int[4*6];
			this.font[tx][ty].getRGB(0, 0, 4, 6, data, 0, 4);
			renderSection(x + i * 5, y, col, data);
		}
	}
	
	public int[] imageToColor(BufferedImage img){
		int w = img.getWidth();
		int h = img.getHeight();
		int[] res = new int[w * h];
		int[] cData = new int[w * h];
		img.getRGB(0, 0, w, h, cData, 0, w);
		for(int i = 0; i < cData.length; i++){
			res[i] = hexToCol(cData[i]);
		}
		return res;
	}
	
	public void renderText(String msg, int x, int y){
		this.renderText(msg, x, y, 7);
	}
	
	public void renderLeftText(String msg,int x,int y){
		this.renderLeftText(msg, x, y, 7);
	}
}
