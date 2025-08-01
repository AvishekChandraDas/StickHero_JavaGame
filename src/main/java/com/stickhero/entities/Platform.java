package com.stickhero.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;

/**
 * Represents a platform in the Stick Hero game
 */
public class Platform {
    
    private double x, y, width, height;
    private Color topColor;
    private Color sideColor;

    public Platform(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.topColor = Color.LIGHTGRAY;
        this.sideColor = Color.GRAY;
    }

    public void render(GraphicsContext gc) {
        // Draw platform shadow
        gc.setFill(Color.rgb(0, 0, 0, 0.3));
        gc.fillRect(x + 3, y + 3, width, height);
        
        // Draw platform main body with gradient
        LinearGradient gradient = new LinearGradient(0, y, 0, y + height, false, 
            CycleMethod.NO_CYCLE, 
            new Stop(0, topColor), 
            new Stop(1, sideColor));
        gc.setFill(gradient);
        gc.fillRect(x, y, width, height);
        
        // Draw platform border
        gc.setStroke(Color.DARKGRAY);
        gc.setLineWidth(2);
        gc.strokeRect(x, y, width, height);
        
        // Add some texture details
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(1);
        // Top highlight
        gc.strokeLine(x + 1, y + 1, x + width - 1, y + 1);
        // Side highlight
        gc.strokeLine(x + 1, y + 1, x + 1, y + height - 1);
        
        // Add crack details for realism
        gc.setStroke(Color.rgb(100, 100, 100, 0.7));
        gc.setLineWidth(0.5);
        double crackY = y + height * 0.3;
        gc.strokeLine(x + width * 0.2, crackY, x + width * 0.8, crackY);
        gc.strokeLine(x + width * 0.6, crackY, x + width * 0.9, y + height * 0.7);
    }

    // Getters and setters
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    
    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }
    
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }
    
    public Color getTopColor() { return topColor; }
    public void setTopColor(Color topColor) { this.topColor = topColor; }
    
    public Color getSideColor() { return sideColor; }
    public void setSideColor(Color sideColor) { this.sideColor = sideColor; }
    
    /**
     * Check if a point is within this platform
     */
    public boolean contains(double pointX, double pointY) {
        return pointX >= x && pointX <= x + width && 
               pointY >= y && pointY <= y + height;
    }
    
    /**
     * Get the top surface Y coordinate
     */
    public double getTopY() {
        return y;
    }
    
    /**
     * Get the right edge X coordinate
     */
    public double getRightX() {
        return x + width;
    }
}
