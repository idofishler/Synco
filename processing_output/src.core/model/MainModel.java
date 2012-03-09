package model;



/**
 * @author Ido
 *
 */
public class MainModel implements IModel {

	public static final int NO_OF_PLAYERS = 2;
	private static final int RATE_THRESHOLD = 7;
	private static final double PULSE_THRESHOLD = 1 * 1000; // seconds
	private PersonModel[] players;

	/**
	 * @return the players
	 */
	public PersonModel[] getPlayers() {
		return players;
	}

	/**
	 * 
	 */
	public MainModel() {
		players = new PersonModel[NO_OF_PLAYERS];
		for (int i = 0; i < NO_OF_PLAYERS; i++) {
			players[i] = new PersonModel(i);
		}
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
	}

	private boolean allPlayersReady() {
		for (int i = 0; i < NO_OF_PLAYERS; i++) {
			if (!players[i].isReady()) {
				return false;
			}
		}
		return true;
	}

	public void setPersonPos(int index, int x, int y) {
		getPlayers()[index].setCenterX(x);
		getPlayers()[index].setCenterY(y);
	}

	public void sync() {
		int rightPlayerXpos = players[0].getCenterX();
		int leftPlayerXpos = players[1].getCenterX();
		int distance = rightPlayerXpos - leftPlayerXpos;

		if (distance > 0) {
			players[0].setCenterX(--rightPlayerXpos);
			players[1].setCenterX(++leftPlayerXpos);
		}
	}

	public void unsync() {

		int rightPlayerXpos = players[0].getCenterX();
		int leftPlayerXpos = players[1].getCenterX();
		int distance = rightPlayerXpos - leftPlayerXpos;

		if (distance < 700) { // TODO fix 700
			players[0].setCenterX(++rightPlayerXpos);
			players[1].setCenterX(--leftPlayerXpos);
		}
	}

	public boolean areSynced() {
		int rightPlayerRate = players[0].getHeartRate();
		int leftPlayerRate = players[1].getHeartRate();
		int rateGap = Math.abs(rightPlayerRate - leftPlayerRate);

		double rightPlayerLastBit = players[0].getLastBeat();
		double leftPlayerLastBit = players[1].getLastBeat();
		double pulseGap = Math.abs(rightPlayerLastBit - leftPlayerLastBit);

		if (pulseGap < PULSE_THRESHOLD && rateGap < RATE_THRESHOLD) {
			return true;
		}
		return false;
	}

	public void startPlayer(int identifier) {
		players[identifier].start();
	}
}
