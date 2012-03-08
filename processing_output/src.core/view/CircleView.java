package view;
import model.*;
import processing.core.*;

public class CircleView extends AbstractView {

	public CircleView(PApplet p, IModel model) {
		super(p, model);
	}

	/**
	 * 
	 */
	public void display() {
		float radius = ((CircleModel) m_model).radius;
		int centerX = ((CircleModel) m_model).centerX;
		int centerY = ((CircleModel) m_model).centerY;
		
		if (radius < 2 * p.width) {
			p.ellipse(centerX, centerY, radius, radius);
		}
	}
}
