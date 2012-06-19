package model;

public class WaterModel implements IModel {

	private static final int DEFAULT_DENSITY = 6;
	public static final int DEFAULT_RADUIS = 25;
	
	public int heightMap[][][]; // water surface (2 pages).
	public int line[]; // line optimizer;
	public int space;
	public int page = 0;

	private int turbulenceMap[][]; // turbulence map
	private int radius, heightMax, density;
	private int m_width, m_height;

	public void initMap(int width, int height) {
		this.m_width = width;
		this.m_height = height;

		// the height map is made of two "pages".
		// one to calculate the current state, and another to keep the previous state.
		heightMap = new int[2][width][height];
		line = new int[height];
		for (int l = 0; l < height; l++) {
			line[l] = l * width;
		}
		density = DEFAULT_DENSITY;
		radius = DEFAULT_RADUIS;
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
					turbulenceMap[radius + x][radius + y] +=
						(int) (900 * ((float) radius - dist));
				}
			}
		}
	}

	// it a kind of image filter, but instead of applying to an image,
	// we apply it to the height map, that encodes the height of the waves.
	private void waterFilter() {
		for (int x = 0; x < m_width; x++) {
			for (int y = 0; y < m_height; y++) {
				int n = y - 1 < 0 ? 0 : y - 1;
				int s = y + 1 > (m_height) - 1 ? (m_height) - 1 : y + 1;
				int e = x + 1 > (m_width) - 1 ? (m_width) - 1 : x + 1;
				int w = x - 1 < 0 ? 0 : x - 1;

				int value = ((heightMap[page][w][n] + heightMap[page][x][n] +
						heightMap[page][e][n] + heightMap[page][w][y] +
						heightMap[page][e][y] + heightMap[page][w][s] +
						heightMap[page][x][s] + heightMap[page][e][s]) >> 2) -
						heightMap[page ^ 1][x][y];

				heightMap[page ^ 1][x][y] = value - (value >> density);
			}
		}
	}

	public void makeTurbulence(int cx, int cy) {
		int r = radius * radius;
		int left = cx < radius ? -cx + 1 : -radius;
		int right = cx > (m_width - 1) - radius ? (m_width - 1) - cx : radius;
		int top = cy < radius ? -cy + 1 : -radius;
		int bottom = cy > (m_height - 1) - radius ? (m_height - 1) - cy : radius;

		for (int x = left; x < right; x++) {
			int xsqr = x * x;
			for (int y = top; y < bottom; y++) {
				if ((xsqr) + (y * y) < r)
					heightMap[page ^ 1][cx + x][cy + y] +=
						turbulenceMap[radius + x][radius + y];
			}
		}
	}

	@Override
	public void update() {
		page ^= 1; // page switch
		waterFilter();
	}


}
