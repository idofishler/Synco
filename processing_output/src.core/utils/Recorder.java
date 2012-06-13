package utils;

import processing.core.PApplet;
import processing.core.PImage;

public class Recorder {

	private static final String PUBLIC_URL = "http://dl.dropbox.com/u/2805933/";
	private static final int QR_SIZE = 200;
	private static final boolean DEBUG = true;


	private String SAVE_NAME ="/Users/idofishler/Dropbox/public/"; // << here is the name of the file
	private String gameId;

	private PApplet p;
	private int pn = 0;
	private boolean initiated = false; 

	public void init(PApplet p) {
		if (!initiated) {
			this.p = p;
		}

		gameId = String.valueOf(System.currentTimeMillis());

		initiated = true;

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

		if (DEBUG) {
			PApplet.open(PUBLIC_URL + "html/sliedshow.html?gameId=" + gameId + ",picNum=" + pn);
			PApplet.open(QR_URL);
		}
	}
}
