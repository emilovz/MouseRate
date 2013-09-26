package cz.pscheidl.mouse.hardware;

import java.awt.MouseInfo;
import java.awt.Point;

import cz.pscheidl.mouse.util.AvgPointerStorage;
import cz.pscheidl.mouse.util.PointerStorage;

public class MouseWatcher implements Runnable {

	private static MouseWatcher instance = null;

	private Thread mouseWatcherThread;
	private boolean isWatching;
	private PointerStorage storage;

	private MouseWatcher() {
		isWatching = false;
		storage = new AvgPointerStorage();
	}

	@Override
	public synchronized void run() {


		Point oldMouseLocation = new Point(0, 0);

		long lastUpdateTime = System.nanoTime();

		while (isWatching) {

			Point currentMouseLocation = MouseInfo.getPointerInfo().getLocation();

			if (!oldMouseLocation.equals(currentMouseLocation)) {

				long currentTime = System.nanoTime();
				storage.mouseRefreshed(currentTime - lastUpdateTime);
				lastUpdateTime = currentTime;
				oldMouseLocation = currentMouseLocation;
			}
		}
	}

	public void startWatching() {

		if (isWatching)
			return;

		isWatching = true;
		mouseWatcherThread = new Thread(this);
		mouseWatcherThread.start();

	}
	
	public boolean isWatching() {
		return isWatching;
	}

	public static MouseWatcher getInstance() {

		if (instance == null) {
			instance = new MouseWatcher();
		}

		return instance;
	}

	public PointerStorage getStorage() {
		return storage;
	}

}
