package domain.controllers;

import domain.models.BuildingModeModel;
import ui.screens.BuildingModeView;

public class BuildingModeController implements Runnable {
	private BuildingModeModel model;
	private BuildingModeView view;
	private boolean[] keys;
	private long lastUpdateTime; 

	public BuildingModeController(BuildingModeModel model, BuildingModeView view) {
		this.model = model;
        this.view = view;
	}

	@Override
	public void run() {
		while (true) {
			long currentTime = System.currentTimeMillis();
			model.update(currentTime, keys, BuildingModeView.WIDTH, BuildingModeView.HEIGHT);
			// view.repaint(); // Repaint the view
			try {
				Thread.sleep(10); // Pause for a short duration
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}