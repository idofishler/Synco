package view;

import java.util.ArrayList;

import controller.MainController;
import processing.core.*;
import model.CircleModel;
import model.PersonModel;

public class PersonView extends AbstractView {

	private int centerX;
	private int centerY;
	
	/**
	 * @param personModel
	 */
	public PersonView(PApplet p, PersonModel personModel) {
		super(p, personModel);
		setCenterX(personModel.getCenterX());
		setCenterY(personModel.getCenterY());
	}

	public void display() {
		
		if (MainController.DEBUG) {
			showRates();
		}
		
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
}
