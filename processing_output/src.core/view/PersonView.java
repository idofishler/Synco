package view;

import java.util.ArrayList;
import processing.core.*;
import model.CircleModel;
import model.PersonModel;

public class PersonView extends AbstractView {

	private static final boolean DEBUG = true;
	ArrayList<CircleView> m_circles;
	private int centerX;
	private int centerY;
	
	/**
	 * @param personModel
	 */
	public PersonView(PApplet p, PersonModel personModel) {
		super(p, personModel);
		m_circles = new ArrayList<CircleView>();
	}

	public void display() {
		
		for (CircleView circle : m_circles) {
			circle.display();
		}
		
		if (DEBUG) {
			showRates();
		}
	}
	
	public void addViewableCircle(CircleModel circleModel) {
		m_circles.add(new CircleView(p, circleModel));
	}
	
	/**
	 * @return the centerX
	 */
	public int getCenterX() {
		return centerX;
	}

	/**
	 * @param centerX the centerX to set
	 */
	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	/**
	 * @return the centerY
	 */
	public int getCenterY() {
		return centerY;
	}

	/**
	 * @param centerY the centerY to set
	 */
	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}
	
	public PersonModel getModel() {
		return (PersonModel) m_model;
	}
	
	private void showRates() {
		// Display the rate as text
		int rate = getModel().getHeartRate();
		int position = getModel().getGamePos();
		
		p.fill(0, 0, 0);
		switch (position) {
		case 0:
			p.text(rate, p.width - 20, 10);
			break;
		case 1:
			p.text(rate, 10, 10);
			break;
		default:
			break;
		}
		p.noFill();
	}

	
	// TOOD: fix java.util.ConcurrentModificationException
//	private void removeInvisibleShapse() {
//		for (CircleModel circle : left) {
//			if (circle.centerX + circle.radius > this.width) {
//				left.remove(circle);
//			}
//		}
//		for (CircleModel circle : right) {
//			if (circle.centerX - circle.radius < 0) {
//				right.remove(circle);
//			}
//		}
//		
//	}
}
