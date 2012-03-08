/**
 * 
 */
package controller;

import model.IModel;
import view.IView;

/**
 * @author Ido
 *
 */
public abstract class AbstractController implements IController {

	IModel m_model;
	IView m_view;
	
	
	/**
	 * @param mModel
	 * @param mView
	 */
	public AbstractController(IModel mModel, IView mView) {
		m_model = mModel;
		m_view = mView;
	}

	/* (non-Javadoc)
	 * @see controller.IController#doUI()
	 */
	@Override
	public void doUI() {
		m_model.update();
		m_view.display();
	}
}
