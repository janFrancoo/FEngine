package main;

import render_engine.DisplayManager;

public class GameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();

        while (!DisplayManager.windowShouldClose()) {
            DisplayManager.updateDisplay();
        }

        DisplayManager.closeDisplay();
    }

}
