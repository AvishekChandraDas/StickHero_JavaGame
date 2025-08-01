package com.stickhero.ui;

import com.stickhero.game.GameEngine;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Handles all UI rendering for the Stick Hero game
 */
public class GameUI {
    
    private Font scoreFont;
    private Font titleFont;
    private Font instructionFont;
    
    public GameUI() {
        scoreFont = Font.font("Arial", FontWeight.BOLD, 24);
        titleFont = Font.font("Arial", FontWeight.BOLD, 48);
        instructionFont = Font.font("Arial", FontWeight.NORMAL, 16);
    }
    
    public void render(GraphicsContext gc, int score, GameEngine.GameState gameState) {
        renderScore(gc, score);
        renderGameStateInfo(gc, gameState);
    }
    
    // Removed duplicate method - using the one with score parameter below
    
    private void renderScore(GraphicsContext gc, int score) {
        gc.save();
        
        // Reset any transforms to draw UI in screen space
        gc.setTransform(1, 0, 0, 1, 0, 0);
        
        gc.setFont(scoreFont);
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        
        String scoreText = "Score: " + score;
        
        // Draw text outline
        gc.strokeText(scoreText, 20, 40);
        // Draw text fill
        gc.fillText(scoreText, 20, 40);
        
        gc.restore();
    }
    
    private void renderGameStateInfo(GraphicsContext gc, GameEngine.GameState gameState) {
        gc.save();
        
        // Reset any transforms to draw UI in screen space
        gc.setTransform(1, 0, 0, 1, 0, 0);
        
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();
        
        switch (gameState) {
            case READY:
                renderInstructions(gc, canvasWidth, canvasHeight);
                break;
                
            case GROWING_STICK:
                renderStickGrowthHint(gc, canvasWidth, canvasHeight);
                break;
                
            case ROTATING_STICK:
                renderRotationInfo(gc, canvasWidth, canvasHeight);
                break;
                
            case HERO_WALKING:
                renderWalkingInfo(gc, canvasWidth, canvasHeight);
                break;
                
            case GAME_OVER:
                renderGameOver(gc, canvasWidth, canvasHeight);
                break;
        }
        
        gc.restore();
    }
    
    private void renderInstructions(GraphicsContext gc, double canvasWidth, double canvasHeight) {
        gc.setFont(instructionFont);
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        
        String instruction = "Hold mouse button to grow stick, release to drop it";
        double textWidth = getTextWidth(instruction, instructionFont);
        double x = (canvasWidth - textWidth) / 2;
        double y = canvasHeight - 50;
        
        // Draw outline
        gc.strokeText(instruction, x, y);
        // Draw fill
        gc.fillText(instruction, x, y);
    }
    
    private void renderStickGrowthHint(GraphicsContext gc, double canvasWidth, double canvasHeight) {
        gc.setFont(instructionFont);
        gc.setFill(Color.YELLOW);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        
        String hint = "Growing stick... Release mouse when ready!";
        double textWidth = getTextWidth(hint, instructionFont);
        double x = (canvasWidth - textWidth) / 2;
        double y = 80;
        
        // Draw outline
        gc.strokeText(hint, x, y);
        // Draw fill
        gc.fillText(hint, x, y);
    }
    
    private void renderRotationInfo(GraphicsContext gc, double canvasWidth, double canvasHeight) {
        gc.setFont(instructionFont);
        gc.setFill(Color.LIGHTGREEN);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        
        String info = "Stick rotating...";
        double textWidth = getTextWidth(info, instructionFont);
        double x = (canvasWidth - textWidth) / 2;
        double y = 80;
        
        // Draw outline
        gc.strokeText(info, x, y);
        // Draw fill
        gc.fillText(info, x, y);
    }
    
    private void renderWalkingInfo(GraphicsContext gc, double canvasWidth, double canvasHeight) {
        gc.setFont(instructionFont);
        gc.setFill(Color.LIGHTBLUE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        
        String info = "Hero is walking...";
        double textWidth = getTextWidth(info, instructionFont);
        double x = (canvasWidth - textWidth) / 2;
        double y = 80;
        
        // Draw outline
        gc.strokeText(info, x, y);
        // Draw fill
        gc.fillText(info, x, y);
    }
    
    private void renderGameOver(GraphicsContext gc, double canvasWidth, double canvasHeight) {
        // Game Over title
        gc.setFont(titleFont);
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        
        String gameOverText = "GAME OVER";
        double titleWidth = getTextWidth(gameOverText, titleFont);
        double titleX = (canvasWidth - titleWidth) / 2;
        double titleY = canvasHeight / 2 - 50;
        
        // Draw outline
        gc.strokeText(gameOverText, titleX, titleY);
        // Draw fill
        gc.fillText(gameOverText, titleX, titleY);
        
        // Restart instruction
        gc.setFont(instructionFont);
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        
        String restartText = "Press R to restart or close window to quit";
        double restartWidth = getTextWidth(restartText, instructionFont);
        double restartX = (canvasWidth - restartWidth) / 2;
        double restartY = canvasHeight / 2 + 20;
        
        // Draw outline
        gc.strokeText(restartText, restartX, restartY);
        // Draw fill
        gc.fillText(restartText, restartX, restartY);
    }
    
    /**
     * Helper method to calculate text width (approximation)
     */
    private double getTextWidth(String text, Font font) {
        Text tempText = new Text(text);
        tempText.setFont(font);
        return tempText.getBoundsInLocal().getWidth();
    }
    
    /**
     * Render FPS counter for debugging
     */
    public void renderFPS(GraphicsContext gc, double fps) {
        gc.save();
        
        // Reset any transforms
        gc.setTransform(1, 0, 0, 1, 0, 0);
        
        gc.setFont(Font.font("Arial", 12));
        gc.setFill(Color.YELLOW);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        
        String fpsText = String.format("FPS: %.1f", fps);
        double x = gc.getCanvas().getWidth() - 80;
        double y = 20;
        
        // Draw outline
        gc.strokeText(fpsText, x, y);
        // Draw fill
        gc.fillText(fpsText, x, y);
        
        gc.restore();
    }
    
    /**
     * Render a progress bar
     */
    public void renderProgressBar(GraphicsContext gc, double x, double y, double width, double height, 
                                double progress, Color fillColor, Color borderColor) {
        gc.save();
        
        // Draw background
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(x, y, width, height);
        
        // Draw progress
        gc.setFill(fillColor);
        gc.fillRect(x, y, width * progress, height);
        
        // Draw border
        gc.setStroke(borderColor);
        gc.setLineWidth(2);
        gc.strokeRect(x, y, width, height);
        
        gc.restore();
    }
}
