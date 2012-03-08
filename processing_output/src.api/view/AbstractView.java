/**
 * 
 */
package view;

import model.IModel;
import processing.core.PApplet;

/**
 * @author Ido
 *
 */
public abstract class AbstractView implements IView {

	PApplet p;
	IModel m_model;
	
	/**
	 * @param p
	 */
	public AbstractView(PApplet p, IModel model) {
		this.p = p;
		m_model = model;
	}
	
	
	/**
	 * @see view.IView#display()
	 */
	/* (non-Javadoc)
	 * @see view.IView#display()
	 */
	@Override
	public void display() {
		// TODO Auto-generated method stub

	}

}
