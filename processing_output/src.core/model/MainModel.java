package model;

import controller.MainController;

/**
 * @author Ido
 *
 */
public class MainModel implements IModel {

	public static final int NO_OF_PLAYERS = 2;
	public static final int STAGE_WIDTH = 1200;
	public static final int STAGE_HEIGHT = 800;
	public static final float MAX_DISTANCE_FACTOR = 0.8f;

	private static final int RATE_THRESHOLD = 20;
	private static final double PULSE_THRESHOLD = 1 * 1000; // seconds
	
	private PersonModel[] players;
	private WaterModel waterModel;
	private SoundModel soundModel;
	private long startTime;


	/**
	 * 
	 */
	public MainModel() {
		players = new PersonModel[NO_OF_PLAYERS];
		for (int i = 0; i < NO_OF_PLAYERS; i++) {
			players[i] = new PersonModel(i);
		}
		waterModel = new WaterModel();
		soundModel = new SoundModel();
	}

	public void init(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the players
	 */
	public PersonModel[] getPlayers() {
		return players;
	}

	@Override
	public void update() {

		// Make the players closer if the are synced
		if (allPlayersReady()) {
			if (areSynced()) {
				sync();
			} else {
				unsync();
			}
		}

		for (int i = 0; i < NO_OF_PLAYERS; i++) {
			players[i].update();
		}

		// keep the water moving...
		waterModel.update();

		soundModel.update();
	}

	public boolean allPlayersReady() {
		for (int i = 0; i < NO_OF_PLAYERS; i++) {
			if (!players[i].isReady()) {
				return false;
			}
		}
		return true;
	}

	public void setPersonPos(int index, int x, int y) {
		switch (index) {
		case 0:
			getPlayers()[index].setCenterX(x - (x / 4));
			getPlayers()[index].setCenterY(y / 2);
			break;
		case 1:
			getPlayers()[index].setCenterX(x / 4);
			getPlayers()[index].setCenterY(y / 2);
			break;
		default:
			break;
		}
	}

	public void sync() {
		int rightPlayerXpos = players[0].getCenterX();
		int leftPlayerXpos = players[1].getCenterX();
		int distance = rightPlayerXpos - leftPlayerXpos;

		if (distance > 0) {
			players[0].setCenterX(--rightPlayerXpos);
			players[1].setCenterX(++leftPlayerXpos);
		}

		soundModel.playSongChannels(distance);
	}

	public void unsync() {

		int rightPlayerXpos = players[0].getCenterX();
		int leftPlayerXpos = players[1].getCenterX();
		int distance = rightPlayerXpos - leftPlayerXpos;

		if (distance < (int) (MAX_DISTANCE_FACTOR * STAGE_WIDTH)) {
			players[0].setCenterX(++rightPlayerXpos);
			players[1].setCenterX(--leftPlayerXpos);
		}

		soundModel.playSongChannels(distance);
	}

	public boolean areSynced() {
		int rightPlayerRate = players[0].getHeartRate();
		int leftPlayerRate = players[1].getHeartRate();
		int rateGap = Math.abs(rightPlayerRate - leftPlayerRate);

		double rightPlayerLastBit = players[0].getLastBeat();
		double leftPlayerLastBit = players[1].getLastBeat();
		double pulseGap = Math.abs(rightPlayerLastBit - leftPlayerLastBit);
		
		if (MainController.DEBUG) {
			System.out.println("RIGHT BPM=" + rightPlayerRate);
			System.out.println("LEFT BPM=" + leftPlayerRate);
			System.out.println("GAP=" + pulseGap);
		}

		boolean areAlive = players[0].isAlive() && players[1].isAlive();

		if (areAlive && pulseGap < PULSE_THRESHOLD && rateGap < RATE_THRESHOLD) {
			return true;
		}
		return false;
	}

	public void startPlayer(int identifier) {
		players[identifier].start(startTime);
	}

	public WaterModel getWaterModel() {
		return this.waterModel;
	}

	public SoundModel getSoundModel() {
		return this.soundModel;
	}
}
