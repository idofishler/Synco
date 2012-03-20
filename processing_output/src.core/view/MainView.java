package view;

import model.MainModel;
import model.PersonModel;
import processing.core.*;

public class MainView extends AbstractView {

	PersonView[] players;
	WaterView waterView;

	/**
	 * @param p
	 */
	public MainView(PApplet p, MainModel mainModel) {
		super(p, mainModel);

	}

	public void initPlayers() {

		// Set the player position (in the model)
		getModel().setPersonPos(0, p.width - (p.width / 4), p.height / 2);
		getModel().setPersonPos(1, p.width / 4, p.height / 2);

		// init the PersonView[] from these PersonModels
		PersonModel[] playerModels = getModel().getPlayers();
		this.players = new PersonView[MainModel.NO_OF_PLAYERS];

		for (int i = 0; i < MainModel.NO_OF_PLAYERS; i++) {
			players[i] = new PersonView(p, playerModels[i]);
		}

		// Start the water view
		waterView = new WaterView(p, getModel().getWaterModel());

	}

	public void display() {
		for (int i = 0; i < MainModel.NO_OF_PLAYERS; i++) {
			players[i].display();
		}
		waterView.display();
	}

	/**
	 * @return the players
	 */
	public PersonView[] getPlayers() {
		return players;
	}

	public MainModel getModel() {
		return (MainModel) m_model;
	}
}
