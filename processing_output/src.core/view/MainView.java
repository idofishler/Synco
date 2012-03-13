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
		
		// Set the player position (in the model)
		getModel().setPersonPos(0, p.width - (p.width / 4), p.height / 2);
		getModel().setPersonPos(1, p.width / 4, p.height / 2);
		
		// init the PersonView[] from these PersonModels
		PersonModel[] playerModels = getModel().getPlayers();
		this.players = new PersonView[MainModel.NO_OF_PLAYERS];

		for (int i = 0; i < MainModel.NO_OF_PLAYERS; i++) {
			players[i] = new PersonView(p, playerModels[i]);
		}
	}
	
	public void display() {
		
		p.background(255);
		
		for (int i = 0; i < MainModel.NO_OF_PLAYERS; i++) {
			switch (i) {
			case 0:
				p.stroke(255, 0, 0); // red
				break;
			case 1:
				p.stroke(0, 0, 255); // blue
				break;

			default:
				p.stroke(0, 0, 0); // black
			}
			players[i].display();
		}	
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
