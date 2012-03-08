package model;

public class CircleModel implements IModel {

	private static final float DEFAULT_GROWTH_FACTOR = 1.5f;

	public int centerX;
	public int centerY;
	public float radius;
	
	public CircleModel(int centerX, int centerY, float raduis) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = raduis;
	}

	public void setCenter(int x, int y) {
		this.centerX = x;
		this.centerY = y;
	}

	public float getRaduis() {
		return radius;
	}

	public void setRaduis(float raduis) {
		this.radius = raduis;
	}

	/**
	 * @see model.IModel#update()
	 */
	@Override
	public void update() {
		this.radius += DEFAULT_GROWTH_FACTOR;
	}
}
