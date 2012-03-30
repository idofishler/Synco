package view;

import model.FlowerModel;
import model.IModel;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public class FlowerView extends AbstractView {

	private static final int FLOWER_SIZE = 50;
	PImage flowerImg;
	private float counter;

	public FlowerView(PApplet p, IModel model) {
		super(p, model);
		//flowerImg = p.loadImage("resources/flower.png");
		getModel().cX = p.width / 2;
		getModel().cY = p.height / 2;
		counter = 0;
	}

	
	@Override
	public void display() {
		p.image(flowerImg, getModel().cX - FLOWER_SIZE/2, getModel().cY - FLOWER_SIZE/2,
				FLOWER_SIZE, FLOWER_SIZE);
//		rotate(); TODO fix this
	}
	
	private void rotate() {
		p.translate(p.width / 2, p.height / 2);
		p.rotate(PConstants.PI*2/360);
		p.translate(-flowerImg.width/2, flowerImg.height/2);
	}


	private FlowerModel getModel() {
		return (FlowerModel) m_model;
	}

}
