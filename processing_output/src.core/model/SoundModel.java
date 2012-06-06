package model;

import java.util.ArrayList;

import utils.MP3Player;
import utils.MyAdvancePlayer;

public class SoundModel implements IModel {

	private static final int INIT_NUM_OF_CHANNELS = 0;

	private static final String HEART_BIT_SOUND_PATH = "resource/HeartBit_strong.mp3";
	private static final String HEART_BIT_SOUND_PATH_L = "resource/HeartBit_L.mp3";
	private static final String HEART_BIT_SOUND_PATH_R = "resource/HeartBit_R.mp3";

	private static final String[] CHNNEL_PATHS_0 = { 
		"resource/channel0.mp3",
		"resource/channel1.mp3",
		"resource/channel2.mp3",
		"resource/channel3.mp3",
		"resource/channel4.mp3",	
	};
	private static final String[] CHNNEL_PATHS_1 = { 
		"resource/channel0_1.mp3",
		"resource/channel1_1.mp3",
		"resource/channel2_1.mp3",
		"resource/channel3_1.mp3",
		"resource/channel4_1.mp3",	
	};
	
	private static final String[] CHNNEL_PATHS = CHNNEL_PATHS_1;
	private static final String GATE_KEEPER_SONG_PATH = "resource/01 Gatekeeper.mp3";

	private int numOfChannels;
	private int prevNumOfChannels;

	//private MP3Player heartBitSound;
	private MP3Player heartBitSound_L;
	private MP3Player heartBitSound_R;
	private ArrayList<MP3Player> channelPlayers;



	public SoundModel() {
		//heartBitSound = new MP3Player(HEART_BIT_SOUND_PATH);
		heartBitSound_L = new MP3Player(HEART_BIT_SOUND_PATH_L);
		heartBitSound_R = new MP3Player(HEART_BIT_SOUND_PATH_R);
		channelPlayers = new ArrayList<MP3Player>();
		init();
	}

	public void init() {
		stop();
		channelPlayers.clear();
		numOfChannels = INIT_NUM_OF_CHANNELS;
		prevNumOfChannels = numOfChannels;
	}

	@Override
	public void update() {
		if (prevNumOfChannels < numOfChannels) {
			MP3Player channel = new MP3Player(CHNNEL_PATHS[prevNumOfChannels]);
			channel.play();
			channelPlayers.add(channel);
		} else if (prevNumOfChannels > numOfChannels) {
			MP3Player channel = channelPlayers.remove(numOfChannels);
			channel.stop();
		}
		prevNumOfChannels = numOfChannels;
	}

	// Play hear bit sound for each event
	public void playPulse(int identifier) {
		if (identifier == 0) {
			heartBitSound_R.play();
		} else if (identifier == 1) {
			heartBitSound_L.play();
		}
		//heartBitSound.play();
	}

	public void playSongChannels(int distance) {
		int maxDistance = (int) (MainModel.STAGE_WIDTH * MainModel.MAX_DISTANCE_FACTOR);
		float closeness =  (float) distance / (float) maxDistance;
		numOfChannels = (int) ((1 - closeness) * CHNNEL_PATHS.length);
	}
	
	public void stop() {
		for (MP3Player channel : channelPlayers) {
			if (channel.isRunning()) {
				channel.stop();
			}
		}
	}
}
