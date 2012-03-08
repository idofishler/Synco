package view;

import model.MainModel;
import model.PersonModel;
import processing.core.*;

public class MainView extends AbstractView {

	PersonView[] players;
	PersonView left;
	PersonView right;
	/**
	 * @param p
	 */
	public MainView(PApplet p, MainModel mainModel) {
		super(p, mainModel);
		
	}
	
	public void initPlayers() {
		PersonModel[] playerModels = ((MainModel) m_model).getPlayers();
		this.players = new PersonView[MainModel.NO_OF_PLAYERS];
		
		playerModels[0].setCenterX(p.width - (p.width / 4));
		playerModels[1].setCenterX(p.width / 4);
		
		for (int i = 0; i < MainModel.NO_OF_PLAYERS; i++) {
			playerModels[i].setCenterY(p.height / 2);
			players[i] = new PersonView(p, playerModels[i]);
		}
	}
	
	public void display() {
		for (int i = 0; i < MainModel.NO_OF_PLAYERS; i++) {
			players[i].display();
		}
	}

	/**
	 * @return the players
	 */
	public PersonView[] getPlayers() {
		return players;
	}
}
