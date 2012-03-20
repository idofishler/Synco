package view;

import model.IModel;
import model.WaterModel;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public class WaterView extends AbstractView {

	private static final int DEFAUT_GAIN = 5;
	private PImage water;
	PImage ov;

	public WaterView(PApplet p, IModel model) {
		super(p, model);
		((WaterModel) m_model).initMap(p.width, p.height);
		initWater();
	}

	private void initWater() {
		float zoff = 0;

		water = p.createImage(p.width,p.height,PConstants.ARGB);
		water.loadPixels();
		ov = p.createImage(10,10,PConstants.ARGB);

		for (int y = p.height-1; y > 0; y--) {
			for (int x = p.width-1; x > 0; x--) {
				zoff += 0.0001f;
				float bright = ((p.noise(x*0.01f, y*0.01f, zoff)) * 255);
				//   water.pixels[x + y * width] = 0xFF000000 | ((int) bright);
				water.pixels[x + p.width*y]=p.color(p.height-x,y,p.width-x);
				// water.pixels[x+width*y]=color(random(40)+random(40)+random(40)+random(40));
				// water.pixels[x+width*y]=color(random(400),random(400),random(400),random(40));
				//   water.pixels[x + width*y]=color(bright,bright,bright,bright );
			}
		}
		water.updatePixels();

		water.filter(PConstants.BLUR, 4.5f);
		water.filter(PConstants.INVERT);
	}

	private void updateWater() {

		float rr,gg,bb,dis;
		float[] cc = new float[3];
		int randomX= (int) p.random(p.width);
		int randomY=(int) p.random(p.height);
		int gain = DEFAUT_GAIN;

		// TODO might need to fix here
		for(int i=0;i<3;i++){
			cc[i]=p.random(40)+p.random(40)+p.random(40)+p.random(40)+p.random(40);
		}

		for (int y = 0; y < p.height - 1; y++) {
			for (int x = 0; x < p.width - 1; x++) {

				int pos = y * p.width + x;
				int col = water.pixels[pos];
				rr = col >> 16 & 0xff;
			gg = col >> 8 & 0xff;
		bb = col  & 0xff;
		dis = PApplet.dist(randomX, randomY,x,y)/6;

		rr += cc[0]/dis-gain;
		gg += cc[1]/dis-gain;
		bb += cc[2]/dis-gain;

		water.pixels[pos] = p.color(rr,gg,bb,120);

		WaterModel i_model = getModel();

		// using the heightmap to distort underlying image
		int deltax = i_model.heightMap[i_model.page][x][y] - i_model.heightMap[i_model.page][(x) + 1][y];
		int deltay = i_model.heightMap[i_model.page][x][y] - i_model.heightMap[i_model.page][x][(y) + 1];

		int offsetx = (deltax >> 3) + x;
		int offsety = (deltay >> 3) + y;

		offsetx = offsetx > p.width ? p.width - 1 : offsetx < 0 ? 0 : offsetx;
		offsety = offsety > p.height ? p.height - 1 : offsety < 0 ? 0 : offsety;

		int offset = (offsety * p.width) + offsetx;
		offset = offset < 0 ? 0 : offset > i_model.space ? i_model.space : offset;
		// Getting the water pixel with distortion and...
		// apply some fake lightning, in true color.
		int pixel = water.pixels[offset];
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		int light = (deltax + deltay) >> 6;
		red += light;
		green += light;
		blue += light;
		red = red > 255 ? 255 : red < 0 ? 0 : red;
		green = green > 255 ? 255 : green < 0 ? 0 : green;
		blue = blue > 255 ? 255 : blue < 0 ? 0 : blue;
		// updating our image source.
		water.pixels[i_model.line[y] + x] = 0xff000000 | (red << 16) | (green << 8) | blue;
			}
		}
		water.updatePixels();
	}

	private WaterModel getModel() {
		return (WaterModel) m_model;
	}


	@Override
	public void display() {
		updateWater();
		blendOv();
	}

	private void blendOv() {
		ov.copy(p.g,0,0,p.width,p.height,0,0,10,10);
		ov.mask(ov);
		p.blend(ov,0,0,9,9,0,0,p.width,p.height,PConstants.OVERLAY);
	}
}
