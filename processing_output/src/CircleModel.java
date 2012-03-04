import processing.core.*;


public class CircleModel {

	private static final float DEFAULT_GROWTH_FACTOR = 1.5f;

	int centerX, centerY;
	float radius;
	PApplet p;

	public CircleModel(int centerX, int centerY, float raduis, PApplet p) {
		this.p = p;
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

	void grow() {
		this.radius += DEFAULT_GROWTH_FACTOR;
	}

	void display() {
		if (radius < 2 * p.width) {
			p.ellipse(centerX, centerY, radius, radius);
		}
	}
}
