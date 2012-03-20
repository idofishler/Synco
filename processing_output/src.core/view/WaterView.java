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
		init();
	}

	public void init() {
		// first init the model's map
		((WaterModel) m_model).initMap(p.width, p.height);
		// then init the water view itself
		initWater();
	}

	private void initWater() {

		float zoff = 0;

		water = new PImage(p.width,p.height);
		water.loadPixels();
		for (int y = 0; y < p.height; y++) {
			for (int x = 0; x < p.width; x++) {
				zoff += 0.0001f;
				float bright = ((p.noise(x*0.01f, y*0.01f, zoff)) * 255);
				water.pixels[x + y * p.width] = 0xFF000000 | ((int) bright);
			}
		}
		water.updatePixels();
		water.filter(PConstants.BLUR, 4.5f);
	}

	private void updateWater() {

		WaterModel i_model = getModel();
		p.loadPixels();
		p.arraycopy(water.pixels, p.pixels); // not really needed...
		for (int y = 0; y < p.height - 1; y++) {
			for (int x = 0; x < p.width - 1; x++) {
				// using the heightmap to distort underlying image
				int deltax = i_model.heightMap[i_model.page][x][y] -
					i_model.heightMap[i_model.page][(x) + 1][y];
				int deltay = i_model.heightMap[i_model.page][x][y] -
					i_model.heightMap[i_model.page][x][(y) + 1];

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
				p.pixels[i_model.line[y] + x] = 0xff000000 | (red << 16) | (green << 8) | blue;
			}
		}
		p.updatePixels();
	}

	private WaterModel getModel() {
		return (WaterModel) m_model;
	}


	@Override
	public void display() {
		updateWater();
		//blendOv();
	}

	private void blendOv() {
		ov.copy(p.g,0,0,p.width,p.height,0,0,10,10);
		ov.mask(ov);
		p.blend(ov,0,0,9,9,0,0,p.width,p.height,PConstants.OVERLAY);
	}
}
