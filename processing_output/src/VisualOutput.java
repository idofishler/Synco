
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import processing.core.*;


public class VisualOutput extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int LEFT_PLAYER = 1;
	private static final int RIGHT_PLAYER = 2;

	List<CircleModel> left, right;

	public VisualOutput() {
		left = new ArrayList<CircleModel>();
		right = new ArrayList<CircleModel>();
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PApplet.main(new String[] { "--present", "VisualOutput" });
	}

	public void setup() {
		size(500, 350);
		noFill();
		strokeWeight(5);
		stroke(0, 0, 0, 128);
		ellipseMode(CENTER);
	}

	public void draw() {
		
		background(255);
		
		for (CircleModel circle : left) {
			circle.display();
			circle.grow();
		}
		for (CircleModel circle : right) {
			circle.display();
			circle.grow();
		}
	}

	@Override
	public void keyPressed() {
		if (key == 'r') {
			pulse(RIGHT_PLAYER);
		}
		if (key == 'l') {
			pulse(LEFT_PLAYER);
		}
	}


	// TOOD: fix java.util.ConcurrentModificationException
	private void removeInvisibleShapse() {
		for (CircleModel circle : left) {
			if (circle.centerX + circle.radius > this.width) {
				left.remove(circle);
			}
		}
		for (CircleModel circle : right) {
			if (circle.centerX - circle.radius < 0) {
				right.remove(circle);
			}
		}
		
	}


	void pulse(int player) {
		int leftPosX = this.width / 4;
		int rightPosX = this.width / 4 * 3;
		int leftPosY = this.height / 2;
		int rightPosY = this.height / 2;

		if (player == LEFT_PLAYER) {			
			left.add(new CircleModel(leftPosX, leftPosY, 0, this));
		} 
		else if (player == RIGHT_PLAYER) {
			right.add(new CircleModel(rightPosX, rightPosY, 0, this));
		}
	}
}

