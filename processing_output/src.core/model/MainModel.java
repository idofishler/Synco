package model;

import view.PersonView;


/**
 * @author Ido
 *
 */
public class MainModel implements IModel {
	
	public static final int NO_OF_PLAYERS = 2;
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
		for (int i = 0; i < NO_OF_PLAYERS; i++) {
			players[i].update();
		}
	}
}
