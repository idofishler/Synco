package controller;

import model.CircleModel;
import model.IModel;
import model.MainModel;
import view.IView;
import view.MainView;

public class MainController extends AbstractController {

	public MainController(IModel model, IView view) {
		super(model, view);
		((MainView) m_view).initPlayers();
	}

	@Override
	public void event(int identifier) {
		CircleModel circleModel = 
			((MainModel) m_model).getPlayers()[identifier].pulse();
		((MainView) m_view).getPlayers()[identifier].addViewableCircle(circleModel);
	}
	
	
}
