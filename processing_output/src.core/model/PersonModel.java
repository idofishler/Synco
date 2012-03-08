package model;

import java.util.ArrayList;

public class PersonModel implements IModel {

	private String name;
	private int heartRate;
	private float HRV;
	private float pulseStrangth;
	private int gamePos;

	private ArrayList<CircleModel> circles;
	private int centerX;
	private int centerY; 

	/**
	 * @param name
	 */
	public PersonModel(int pos) {
		this.name = "player" + pos;
		gamePos = pos;
		this.circles = new ArrayList<CircleModel>();
	}

	public CircleModel pulse() {
		CircleModel circleModel = new CircleModel(centerX, centerY, 0);
		circles.add(circleModel);
		return circleModel;
	}

	/**
	 * @return the circles
	 */
	public ArrayList<CircleModel> getCircles() {
		return circles;
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

	/**
	 * @return the gamePos
	 */
	public int getGamePos() {
		return gamePos;
	}

	/**
	 * @param gamePos the gamePos to set
	 */
	public void setGamePos(int gamePos) {
		this.gamePos = gamePos;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the heartRate
	 */
	public int getHeartRate() {
		return heartRate;
	}

	/**
	 * @param heartRate the heartRate to set
	 */
	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}

	/**
	 * @return the hRV
	 */
	public float getHRV() {
		return HRV;
	}

	/**
	 * @param hRV the hRV to set
	 */
	public void setHRV(float hRV) {
		HRV = hRV;
	}

	/**
	 * @return the pulseStrangth
	 */
	public float getPulseStrangth() {
		return pulseStrangth;
	}

	/**
	 * @param pulseStrangth the pulseStrangth to set
	 */
	public void setPulseStrangth(float pulseStrangth) {
		this.pulseStrangth = pulseStrangth;
	}

	@Override
	public void update() {
		for (CircleModel circle : circles) {
			circle.update();
		}
	}
}
