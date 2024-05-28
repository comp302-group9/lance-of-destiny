package domain.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import domain.models.BuildingModeModel;
import domain.models.RunningModeModel;
import ui.screens.BuildingModeView;
import ui.screens.MyGamesView;
import ui.screens.RunningModeView;

public class BuildingModeController {
    private BuildingModeModel model;
    private BuildingModeView view;

    public BuildingModeController(BuildingModeModel model, BuildingModeView view) {
        this.model = model;
        this.view = view;
        initController();
    }

    private void initController() {
        view.addPlayButtonListener(new PlayButtonListener());
        view.addPlaceButtonListener(new PlaceButtonListener());
        view.addSaveButtonListener(new SaveButtonListener());
        view.addMyGamesButtonListener(new MyGamesButtonListener());
    }

    private class PlayButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!model.validateBarriers()) {
                view.showErrorDialog();
                return;
            }

            RunningModeModel rmodel = new RunningModeModel(model.getUser(), view.getGrid()); // the model in model.getUser is a BuildingModoModel
            RunningModeView rview = new RunningModeView(rmodel);
            RunningModeController rcontroller = new RunningModeController(rmodel, rview, view.getGrid());

            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(view);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(rview);
            frame.revalidate();
            frame.repaint();

            Thread gameThread = new Thread(rcontroller);
            gameThread.start();
        }
    }

    private class PlaceButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.resetCurrentState();
            int[][] temp = model.createEmptyGrid();

            for (int i = 0; i < 4; i++) {
                String textFieldText = view.getBarrierElementText(i);
                if (BuildingModeView.isValidInteger(textFieldText)) {
                    int value = Integer.parseInt(textFieldText);
                    view.changeRandomValues(temp, value, i + 1);
                }
            }

            view.setGrid(temp);
            view.readGrid(temp);
            view.updateCurrentState();
        }
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.saveGridToDatabase("src\\domain\\txtData\\Test.txt", view.getGrid());
        }
    }

    private class MyGamesButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MyGamesView myGamesView = new MyGamesView();
            MyGamesController controller = new MyGamesController(myGamesView, model.getUser());

            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(view);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(myGamesView);
            frame.revalidate();
            frame.repaint();
        }
    }
}
