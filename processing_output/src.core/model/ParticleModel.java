package model;

public class ParticleModel implements IModel {

	float xpos,ypos,del;

	public ParticleModel (float x,float y,float d) {
		xpos=x;
		ypos=y;
		del = d;
	}


	@Override
	public void update() {
		xpos *= del;
	    ypos *= del;
	}

}
