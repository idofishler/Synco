package controller;

import model.CircleModel;
import model.IModel;
import model.MainModel;
import view.IView;
import view.MainView;

public class MainController extends AbstractController {

	public MainController(IModel model, IView view) {
		super(model, view);
		getView().initPlayers();
	}

	@Override
	public void event(int identifier) {
		CircleModel circleModel = 
			getModel().getPlayers()[identifier].pulse();
		
		// start the player with the first pulse
		getModel().startPlayer(identifier);
		
		getView().getPlayers()[identifier].addViewableCircle(circleModel);
	}
	
	public MainModel getModel() {
		return (MainModel) m_model;
	}
	
	public MainView getView() {
		return ((MainView) m_view);
	}
	
	public void init() {
		// TODO call this from main class
	}
	
	
}
