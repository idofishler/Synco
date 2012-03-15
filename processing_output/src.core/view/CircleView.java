package view;
import model.*;
import processing.core.*;

public class CircleView extends AbstractView {
	
	//private PShape heart;
	private int m_alpha;

	public CircleView(PApplet p, IModel model) {
		super(p, model);
		m_alpha = 0;
		//heart = p.loadShape("C:/Users/Ido/workspace/Milab/processing_output/resourse/heart.svg");
	}

	/**
	 * 
	 */
	public void display() {
		float radius = ((CircleModel) m_model).radius;
		int centerX = ((CircleModel) m_model).centerX;
		int centerY = ((CircleModel) m_model).centerY;

		
		if (radius < 2 * p.width && m_alpha < 255) {
//			p.smooth();
//			p.shape(heart, centerX, centerY, radius, radius);
			p.ellipse(centerX, centerY, radius, radius);
		}
	}
}
