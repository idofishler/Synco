package model;

public class WaterModel implements IModel {

	public int heightMap[][][]; // water surface (2 pages).
	int turbulenceMap[][]; // turbulence map
	public int line[]; // line optimizer;
	public int space;
	int radius, heightMax, density;
	public int page = 0;
	int width, height;

	// the height map is made of two "pages".
	// one to calculate the current state, and another to keep the previous state.
	public void initMap(int width, int height) {
		this.width = width;
		this.height = height;
		heightMap = new int[2][width][height];
		line = new int[height];
		for (int l = 0; l < height; l++) {
			line[l] = l * width;
		}
		density = 7;//5
		radius = 10;//20
		space = width * height - 1;

		// the turbulence map, is an array to make a smooth turbulence over the height map.
		turbulenceMap = new int[radius * 2][radius * 2]; // turbulence map.
		int r = radius * radius;
		int squarex, squarey;
		double dist;

		for (int x = -radius; x < radius; x++) {
			squarex = x * x;
			for (int y = -radius; y < radius; y++) {
				squarey = y * y;
				dist = Math.sqrt(squarex + squarey);
				if ((squarex) + (squarey) < r) {
					turbulenceMap[radius + x][radius + y] += (int) (900 * ((float) radius - dist));
				}
			}
		}
	}

	// it a kind of image filter, but instead of applying to an image,
	// we apply it to the height map, that encodes the height of the waves.
	private void waterFilter() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				int n = y - 1 < 0 ? 0 : y - 1;
				int s = y + 1 > (height) - 1 ? (height) - 1 : y + 1;
				int e = x + 1 > (width) - 1 ? (width) - 1 : x + 1;
				int w = x - 1 < 0 ? 0 : x - 1;

				int value = ((heightMap[page][w][n] + heightMap[page][x][n] + 
						heightMap[page][e][n] + heightMap[page][w][y] + 
						heightMap[page][e][y] + heightMap[page][w][s] + 
						heightMap[page][x][s] + heightMap[page][e][s]) >> 2) - 
						heightMap[page ^ 1][x][y];

				heightMap[page ^ 1][x][y] = value - (value >> density);
				//   water.set(x,y,value - (value >> density)); //<<-- kick here - do not update water here
			}
		}
	}

	public void makeTurbulence(int cx, int cy) {
		int r = radius * radius;
		int left = cx < radius ? -cx + 1 : -radius;
		int right = cx > (width - 1) - radius ? (width - 1) - cx : radius;
		int top = cy < radius ? -cy + 1 : -radius;
		int bottom = cy > (height - 1) - radius ? (height - 1) - cy : radius;


		for (int x= right;x>left;x--){
			int xsqr = x * x;

			for (int y=bottom;y>top;y--){
				if ((xsqr) + (y * y) < r)
					heightMap[page ^ 1][cx + x][cy + y] += turbulenceMap[radius + x][radius + y];
				//  water.set(x,y,turbulenceMap[radius + x][radius + y]); //<<<-- kick do not update water here
			}
		}
	}

	@Override
	public void update() {
		page ^= 1; // page switch
		waterFilter();
	}


}
