package controller;

import java.util.ArrayList;

import model.IModel;
import model.MP3Player;
import model.MainModel;
import model.MyAdvancePlayer;
import view.IView;
import view.MainView;

public class MainController extends AbstractController {

	public static final boolean DEBUG = true;

	private static final int NUM_OF_CHANNELS = 1;
	private static final String HEART_BIT_SOUND_PATH = "/Users/idofishler/workspace/Milab/processing_output/resource/heartbeat-05.mp3";
	private static final String CHANNEL_0_PATH = "/Users/idofishler/workspace/Milab/processing_output/resource/01 Gatekeeper.mp3";

	private static final long TIME_FOR_SONG = 5 * 1000;
	
	private MyAdvancePlayer heartBitSound;
	private ArrayList<MyAdvancePlayer> channelPlayers;

	private long startTime = 0;
	private boolean songIsRunning = false;

	public MainController(IModel model, IView view) {
		super(model, view);
		init();
	}

	@Override
	public void event(int identifier) {

		// TODO fix here save a pointer...
		getModel().getPlayers()[identifier].pulse();
		
		// Play hear bit sound for each event
		heartBitSound.play(0, 20);
		
		// make the flower move
		//getModel().getFlowerModel().moveFlower(getModel().getPlayers()[identifier]);

		// start the player with the first pulse
		getModel().startPlayer(identifier);

		// this long line is making the water move where the player center is at
		getModel().getWaterModel().
		makeTurbulence(getModel().getPlayers()[identifier].getCenterX(),
				getModel().getPlayers()[identifier].getCenterY());
		
		if (startTime  == 0 && getModel().allPlayersReady()) {
			startTime = System.currentTimeMillis();
			System.out.println(startTime);
		}
		if (startTime != 0 && !songIsRunning  && System.currentTimeMillis() - startTime >= TIME_FOR_SONG) {
			songIsRunning = true;
			playSong();
		}
	}
	private void playSong() {
		for (MyAdvancePlayer player : channelPlayers) {
			player.play();
		}
		
	}
	

	public MainModel getModel() {
		return (MainModel) m_model;
	}

	public MainView getView() {
		return ((MainView) m_view);
	}

	public void init() {
		getView().init();
		heartBitSound = new MyAdvancePlayer(HEART_BIT_SOUND_PATH);
		channelPlayers = new ArrayList<MyAdvancePlayer>();
		
		channelPlayers.add(new MyAdvancePlayer(CHANNEL_0_PATH));
	}


}
