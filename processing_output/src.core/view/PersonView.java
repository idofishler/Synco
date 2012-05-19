package view;

import model.PersonModel;
import processing.core.PApplet;
import controller.MainController;

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
		
		p.fill(255);
		
		if (getCenterX() > p.width / 2) {
			p.text(String.valueOf(rate), p.width - 35, 10, 20, 20);
		} else {
			p.text(String.valueOf(rate), 25, 10, 20, 20);			
		}
		p.noFill();
	}
}
