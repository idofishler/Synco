package utils;

import java.util.Date;

import processing.core.PApplet;
import processing.core.PImage;
import processing.video.MovieMaker;

public class Recorder {

	private static final String PUBLIC_URL = "http://dl.dropbox.com/u/2805933/";
	private static final int QR_SIZE = 200;


	private String SAVE_NAME ="/Users/idofishler/Dropbox/public/"; // << here is the name of the file
	private String gameId;

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
			
			gameId = String.valueOf(System.currentTimeMillis());
			
			initiated = true;
		}
	} 
	
	public String snap() {
		String snapName = "Synco_snap_" + pn++ + ".png";
		p.save(SAVE_NAME + gameId + '/' + snapName);
		return gameId + '/' + snapName;
	}
	
	private String getQRURL(String name) {
		String dropBoxUrl = PUBLIC_URL + name;
		int size = QR_SIZE; //The size of the QR (in pixels)
		String correctionLevel = "M"; //Error Correction level
		int margin = 0;
		String background = "FFFFFF"; //Background color
		String codeText = "UTF-8"; //Text Encoding
		String transparency ="a,s,000000";
		String basicURL = 
				"http://chart.apis.google.com/chart?chf=" + transparency +
				"|bg,s," + background + "&chs=" + size + "x" + size + "&chld=" +
						correctionLevel + "|" + margin + "&cht=qr&chl=";
		String fullURL = basicURL + dropBoxUrl + "&choe=" + codeText; //FullURLof the QR
		return fullURL;
	}
	
	public void showQR() {
		String slidShowURL = "html/sliedshow.html?gameId=" + gameId + ",picNum=" + pn;
		String QR_URL = getQRURL(slidShowURL);
		PImage onLine = p.loadImage(QR_URL, "png");
		p.image(onLine, p.width/2 - QR_SIZE/2, p.height/2 - QR_SIZE/2);
		p.noLoop();
		
		// DEBUG only!!!
//		PApplet.open(PUBLIC_URL + "html/sliedshow.html?gameId=" + gameId + ",picNum=" + pn);
//		PApplet.open(QR_URL);
	}


	public void stop() {
		if (videoOn) {
			mm.finish();
			System.out.println("Video out put has been saved to '" + p.sketchPath + "/output'");
		}
		videoOn = false;
	}

}
