package model;

import java.util.ArrayList;

public class PersonModel implements IModel {

	private static final int BEATS_TO_STORE = 5;
	private static final long START_DELAY = 12 * 1000;

	private String name;
	private int heartRate;
	private float HRV;
	private float pulseStrangth;
	private int gamePos;

	private ArrayList<Long> timeBetweenBeats;
	long lastBeat;

	private int centerX;
	private int centerY;

	private boolean ready;

	/**
	 * @param name
	 */
	public PersonModel(int pos) {
		this.name = "player" + pos;
		gamePos = pos;
		this.ready = false;

		timeBetweenBeats = new ArrayList<Long>();
		lastBeat = 0;
	}

	@Override
	public void update() {
		// nothing to do
	}




	public void pulse() {
		// beat is for calculating the heart rate
		beat();
	}

	public void beat() {
		timeBetweenBeats.add(System.currentTimeMillis() - lastBeat);
		lastBeat = System.currentTimeMillis();
	
		if (timeBetweenBeats.size() > BEATS_TO_STORE) 
			timeBetweenBeats.remove(0);
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

		double total = 0;

		for (double t : timeBetweenBeats) {
			total += t;
		}

		double average = total / timeBetweenBeats.size();
		heartRate = (int) (60 / (average / 1000));

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

	/**
	 * @return the lastBeat
	 */
	public double getLastBeat() {
		return lastBeat;
	}

	public void start(long startTime) {
		if (!ready && System.currentTimeMillis() - startTime > START_DELAY) {
			this.ready = true;			
		}
	}

	/**
	 * @return the ready
	 */
	public boolean isReady() {
		return ready;
	}
}
