package view;

import java.util.ArrayList;

import controller.MainController;
import processing.core.*;
import model.CircleModel;
import model.PersonModel;

public class PersonView extends AbstractView {

	ArrayList<CircleView> m_circles;
	private int centerX;
	private int centerY;
	private PImage heartImage;
	private int pulseIndicatorRate = 24;
	
	/**
	 * @param personModel
	 */
	public PersonView(PApplet p, PersonModel personModel) {
		super(p, personModel);
		m_circles = new ArrayList<CircleView>();
		setCenterX(personModel.getCenterX());
		setCenterY(personModel.getCenterY());
		heartImage = p.loadImage("/resourse/heart.jpg");
	}

	public void display() {
		
		drawHeart();

		for (CircleView circle : m_circles) {
			circle.display();
		}
		
		if (MainController.DEBUG) {
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
		
		p.fill(0, 0, 0);
		
		if (getCenterX() > p.width / 2) {
			p.text(rate, p.width - 35, 10);
		} else {
			p.text(rate, 25, 10);			
		}
		p.noFill();
	}
	
	public void waitForPlayer() {
		
	}
	
	private void drawHeart() {
		if (!getModel().isReady()) {
			p.image(heartImage, centerX-75, centerY-75, 150, 150);			
		} else if (getModel().isReady()) {
			if (getCenterX() > p.width / 2) {
				p.image(heartImage, p.width - 20, 0, 20, 20);
				if (--pulseIndicatorRate > 0 && pulseIndicatorRate < 10) {
					p.image(heartImage, p.width - 23, 0, 23, 23);
				}
			} else {
				p.image(heartImage, 0, 0, 20, 20);
				if (--pulseIndicatorRate > 0 && pulseIndicatorRate < 10) {
					p.image(heartImage, 0, 0, 23, 23);
				}
			}
			if (pulseIndicatorRate == 0) pulseIndicatorRate = 34;
		}
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
