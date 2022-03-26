package application.controller;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

class LoopTask extends Thread {

	public void run() {
		MediaPlayer mp = MainViewController.mp;
		Duration loopEnd = MainViewController.loopEnd;
		Duration loopStart = MainViewController.loopStart;
		boolean loopContinue = MainViewController.loopClosed;
    	while ( loopContinue ) {
    		if ( mp.getCurrentTime().greaterThan(loopEnd) ) {
    			mp.stop();
    			mp.setStartTime(loopStart);
    			mp.play();
    		}
    		loopContinue = MainViewController.loopClosed;
    	}
		
	}
	
}