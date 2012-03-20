package controller;

import model.IModel;
import model.MainModel;
import view.IView;
import view.MainView;

public class MainController extends AbstractController {

	public static final boolean DEBUG = true;

	public MainController(IModel model, IView view) {
		super(model, view);
		init();
	}

	@Override
	public void event(int identifier) {

		getModel().getPlayers()[identifier].pulse();

		// start the player with the first pulse
		getModel().startPlayer(identifier);

		// this long line is making the water move where the player center is at
		getModel().getWaterModel().
		makeTurbulence(getModel().getPlayers()[identifier].getCenterX(),
				getModel().getPlayers()[identifier].getCenterY());
	}

	public MainModel getModel() {
		return (MainModel) m_model;
	}

	public MainView getView() {
		return ((MainView) m_view);
	}

	public void init() {
		getView().initPlayers();
	}


}
