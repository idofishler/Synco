/**
 * 
 */
package utils;

/**
 * @author idofishler
 *
 */

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javax.sound.sampled.FloatControl;

import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.Player;


public class MP3Player {
	private static final int FADE_TIME = 1000 * 1000 * 2; // in microseconds
	private static final boolean DEBUG = false;
	private String filename;
	private Player player;

	// constructor that takes the name of an MP3 file
	public MP3Player(String filename) {
		this.filename = filename;
	}

	public void close() { if (player != null) player.close(); }

	// play the MP3 file to the sound card
	public void play() {
		try {
			FileInputStream fis     = new FileInputStream(filename);
			BufferedInputStream bis = new BufferedInputStream(fis);
			player = new Player(bis);
		}
		catch (Exception e) {
			System.out.println("Problem playing file " + filename);
			System.out.println(e);
		}

		// run in new thread to play in background
		new Thread() {
			public void run() {
				try { player.play(); }
				catch (Exception e) { System.out.println(e); }
			}
		}.start();

		if (DEBUG) {
			System.out.println("Play: " + filename);
		}

	}

	public boolean isRunning() {
		return !player.isComplete() || player.getPosition() == 0;
	}

	public void stop() {
		player.close();

		if (DEBUG) {
			System.out.println("Stop: " + filename);
		}
	}

	public void setVolume(float vol) {
		AudioDevice audio = player.getAudioDevice();
		if (audio instanceof JavaSoundAudioDevice)
		{
			JavaSoundAudioDevice jsAudio = (JavaSoundAudioDevice) audio;
			jsAudio.setLineGain(vol);
		}
	}

	public void fadeIn() {
		AudioDevice audio = player.getAudioDevice();
		if (audio instanceof JavaSoundAudioDevice)
		{
			JavaSoundAudioDevice jsAudio = (JavaSoundAudioDevice) audio;
			FloatControl volControl = jsAudio.getVolControl();
			int updatePeriod = volControl.getUpdatePeriod();
//			if (updatePeriod != -1) {
				jsAudio.fadeIn(FADE_TIME);
//			} else {
//				float precision = volControl.getPrecision();
//				while (volControl.getValue() <= (volControl.getMaximum() * 0.5)) {
//					setVolume(volControl.getValue() + precision);
//				}
//			}

			if (DEBUG) {
				System.out.println("Fade in: " + filename);
			}
		}

	}

	public void fadeOut() {
		AudioDevice audio = player.getAudioDevice();
		if (audio instanceof JavaSoundAudioDevice)
		{
			JavaSoundAudioDevice jsAudio = (JavaSoundAudioDevice) audio;
			
			jsAudio.fadeOut(FADE_TIME);

			if (DEBUG) {
				System.out.println("Fade out: " + filename);
			}
		}
	}

	public void mute() {
		AudioDevice audio = player.getAudioDevice();
		if (audio instanceof JavaSoundAudioDevice)
		{
			JavaSoundAudioDevice jsAudio = (JavaSoundAudioDevice) audio;

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			jsAudio.mute();

			if (DEBUG) {
				System.out.println("Mute: " + filename);
			}
		}
	}
}
