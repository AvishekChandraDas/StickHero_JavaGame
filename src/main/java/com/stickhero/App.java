package com.stickhero;

import com.stickhero.game.GameEngine;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Main application class for Stick Hero game
 */
public class App extends Application {
    
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final String GAME_TITLE = "Stick Hero";
    
    private GameEngine gameEngine;

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        
        // Initialize game engine
        gameEngine = new GameEngine(WINDOW_WIDTH, WINDOW_HEIGHT);
        root.getChildren().add(gameEngine.getGameCanvas());
        
        // Ensure canvas can receive keyboard input
        gameEngine.getGameCanvas().requestFocus();
        
        // Setup window
        primaryStage.setTitle(GAME_TITLE);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
        
        // Start game loop
        gameEngine.start();
        
        // Handle window close
        primaryStage.setOnCloseRequest(e -> {
            gameEngine.stop();
            System.exit(0);
        });
        
        // Handle window resize
        scene.widthProperty().addListener((obs, oldWidth, newWidth) -> 
            gameEngine.handleResize(newWidth.doubleValue(), scene.getHeight()));
        scene.heightProperty().addListener((obs, oldHeight, newHeight) -> 
            gameEngine.handleResize(scene.getWidth(), newHeight.doubleValue()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
