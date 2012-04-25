package utils;

import processing.core.PApplet;
import processing.video.MovieMaker;

public class Recorder {

	private String SAVE_NAME ="output/synco_"; // << here is the name of the file

	private PApplet p;
	private int pn = 0;
	MovieMaker mm;
	private boolean initiated = false; 
	private int frate = 15; // <<-- here is the frameRate 
	private boolean videoOn = false; // this is used to indicate whether the video is capturing
	
	public void saveVideo() {   
		if (!videoOn) {
			videoOn = true;
		}
		mm.addFrame();
	} 


	public void init(PApplet p) {
		if (!initiated) {
			this.p = p;
			
			mm = new MovieMaker(p, p.width, p.height, SAVE_NAME + "vdo_" + ".mov", frate,       
							MovieMaker.ANIMATION, MovieMaker.MEDIUM);

			initiated = true;
		}
	} 
	
	public void snap() {
		p.save(SAVE_NAME + "_snap_" + pn++ + ".png");
	}
	
	public void stop() {
		if (videoOn) {
			mm.finish();
			System.out.println("Video out put has been saved to '" + p.sketchPath + "/output'");
		}
		videoOn = false;
	}

}
