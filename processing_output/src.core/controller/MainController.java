package controller;



import java.util.Date;

import model.IModel;
import model.MainModel;
import model.SoundModel;
import view.IView;
import view.MainView;

public class MainController extends AbstractController {

	public static final boolean DEBUG = true;
	
	private long startTime = 0;


	public MainController(IModel model, IView view) {
		super(model, view);
		init();
	}

	@Override
	public void event(int identifier) {

		// TODO fix here save a pointer...
		getModel().getPlayers()[identifier].pulse();

		// start the player with the first pulse
		getModel().startPlayer(identifier);

		handleSound();

		// only make wave if "player is alive" pulse in the range
		if (getModel().getPlayers()[identifier].isAlive()) {
		
			// this long line is making the water move where the player center is at
			getModel().getWaterModel().
			makeTurbulence(getModel().getPlayers()[identifier].getCenterX(),
					getModel().getPlayers()[identifier].getCenterY());
			
		}
	}

	private void handleSound() {
		SoundModel i_SoundModel = getModel().getSoundModel();
		
		// make pulse sound anyway for each event
		i_SoundModel.playPulse();
		
	}

	public MainModel getModel() {
		return (MainModel) m_model;
	}

	public MainView getView() {
		return ((MainView) m_view);
	}

	public void init() {
		startTime = System.currentTimeMillis();
		getModel().init(startTime);
		if (DEBUG) {
			System.out.println("Iteraction started on: " + new Date(startTime));
		}
		getView().init();
	}


}
