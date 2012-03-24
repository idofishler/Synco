package model;


public class FlowerModel implements IModel {

	private static final float MOVMET_FACTOR = 0.01f;
	public int cX, cY;
	
	/**
	 * @param cX
	 * @param cY
	 */
	public FlowerModel() {
		this.cX = 0;
		this.cY = 0;
	}


	
	public void moveFlower(PersonModel player) {
		int playerX = player.getCenterX();
		int playerY = player.getCenterY();
		
		int distance = calcDistance(playerX, playerY);
		//double angle = calcAngel(playerX, playerY);
		
		if (distance <= WaterModel.DEFAULT_RADUIS*5) {
			move(playerX, playerY);
			//move(distance, angle);
		}
	}
	
	
	
	private void move(int distance, double angle) {
		// TODO for later use
	}
	
	private void move(int pX, int pY) {
		int vX = pX - cX;
		int vY = pY - cY;
		cX -= (int) (vX * MOVMET_FACTOR);
		cY -= (int) (vY * MOVMET_FACTOR);
	}



	private double calcAngel(int playerX, int playerY) {
		int c = calcDistance(playerX, playerY);
		if (c == 0) {
			return 0;
		}
		int a = Math.abs(playerX - cX);
		return Math.acos(a / c);
	}



	private int calcDistance(int playerX, int playerY) {
		int x = Math.abs(playerX - cX);
		int y = Math.abs(playerY - cY);
		return (int) Math.sqrt(x*x + y*y);
	}



	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
