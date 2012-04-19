package model;

import java.util.ArrayList;

import utils.MP3Player;
import utils.MyAdvancePlayer;

public class SoundModel implements IModel {

	private static final String HEART_BIT_SOUND_PATH = "/Users/idofishler/workspace/Milab/processing_output/resource/HeartBit.mp3";

	private static final int INIT_NUM_OF_CHANNELS = 0;
	private static final String[] CHNNEL_PATHS = 
		{ "/Users/idofishler/workspace/Milab/processing_output/resource/01 Gatekeeper.mp3",
		
	};
	
	private static final int MAX_NUM_OF_CHANNELS = 1;

	private int numOfChannels;
	private int prevNumOfChannels;

	private MP3Player heartBitSound;
	private ArrayList<MP3Player> channelPlayers;



	public SoundModel() {
		heartBitSound = new MP3Player(HEART_BIT_SOUND_PATH);
		channelPlayers = new ArrayList<MP3Player>();
		setNumOfChannels(INIT_NUM_OF_CHANNELS);
		prevNumOfChannels = INIT_NUM_OF_CHANNELS;
	}

	@Override
	public void update() {
		if (numOfChannels > prevNumOfChannels) {
			MP3Player newChannel = new MP3Player(CHNNEL_PATHS[numOfChannels-1]);
			channelPlayers.add(newChannel);
			// TODO make sure I play from the same position of other 
			newChannel.play();
		} 
		else if (numOfChannels < prevNumOfChannels) {
			MP3Player removedChannel = channelPlayers.remove(numOfChannels);
			removedChannel.stop();
		}
		// any way make sure these are equal
		prevNumOfChannels = numOfChannels;
		//loopIfNeeded();

	}

	private void loopIfNeeded() {
		for (MP3Player channel : channelPlayers) {
			if (!channel.isRunning()) {
				channel.play();
			}
		}
	}

	// Play hear bit sound for each event
	public void playPulse() {
		heartBitSound.play();

	}

	private void setNumOfChannels(int numOfChannels) {
		if (numOfChannels >=0 && numOfChannels <= MAX_NUM_OF_CHANNELS) {
			this.numOfChannels = numOfChannels;
		}
	}

	public void sync() {
		if (numOfChannels >=0 && numOfChannels < MAX_NUM_OF_CHANNELS) {
			numOfChannels++;
		}
	}
	
	public void unsync() {
		if (numOfChannels >0 && numOfChannels <= MAX_NUM_OF_CHANNELS) {
			numOfChannels--;
		}
	}

}
