package view;
import model.*;
import processing.core.*;

public class CircleView extends AbstractView {
	
	private PShape heart;

	public CircleView(PApplet p, IModel model) {
		super(p, model);
		heart = p.loadShape("C:/Users/Ido/workspace/Milab/processing_output/resourse/heart.svg");
	}

	/**
	 * 
	 */
	public void display() {
		float radius = ((CircleModel) m_model).radius;
		int centerX = ((CircleModel) m_model).centerX;
		int centerY = ((CircleModel) m_model).centerY;

		if (radius < 2 * p.width) {
//			p.smooth();
//			p.shape(heart, centerX, centerY, radius, radius);
			
			p.ellipse(centerX, centerY, radius, radius);
		}
	}
}
