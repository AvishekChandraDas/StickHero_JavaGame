package com.stickhero.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

/**
 * Represents the stick that grows and rotates in the Stick Hero game
 */
public class Stick {
    
    private double baseX, baseY;
    private double length;
    private double rotation; // in degrees
    private double maxLength;
    
    private boolean isGrowing;
    private boolean isRotating;
    private boolean rotationComplete;
    
    // Constants
    private static final double GROWTH_SPEED = 100.0; // pixels per second
    private static final double ROTATION_SPEED = 180.0; // degrees per second (90 degrees in 0.5 seconds)
    private static final double STICK_WIDTH = 4.0;
    private static final double MAX_STICK_LENGTH = 300.0;

    public Stick(double baseX, double baseY) {
        this.baseX = baseX;
        this.baseY = baseY;
        this.length = 0;
        this.rotation = 0;
        this.maxLength = MAX_STICK_LENGTH;
        this.isGrowing = true;
        this.isRotating = false;
        this.rotationComplete = false;
    }

    public void grow(double deltaTime) {
        if (isGrowing && length < maxLength) {
            length += GROWTH_SPEED * deltaTime;
            if (length > maxLength) {
                length = maxLength;
            }
        }
    }

    public void startRotating() {
        isGrowing = false;
        isRotating = true;
    }

    public void update(double deltaTime) {
        if (isRotating && !rotationComplete) {
            rotation += ROTATION_SPEED * deltaTime;
            if (rotation >= 90) {
                rotation = 90;
                isRotating = false;
                rotationComplete = true;
            }
        }
    }

    public void render(GraphicsContext gc) {
        if (length <= 0) return;
        
        gc.save();
        
        // Move to base point
        gc.translate(baseX, baseY);
        
        // Apply rotation around the base point
        gc.rotate(rotation);
        
        // Draw stick shadow
        gc.setFill(Color.rgb(0, 0, 0, 0.3));
        gc.fillRect(1, -STICK_WIDTH/2 + 1, length, STICK_WIDTH);
        
        // Draw main stick with gradient effect
        gc.setFill(Color.SADDLEBROWN);
        gc.fillRect(0, -STICK_WIDTH/2, length, STICK_WIDTH);
        
        // Add wood texture
        gc.setStroke(Color.rgb(139, 69, 19, 0.6)); // Darker brown
        gc.setLineWidth(0.5);
        for (double i = 0; i < length; i += 8) {
            gc.strokeLine(i, -STICK_WIDTH/4, i + 4, STICK_WIDTH/4);
        }
        
        // Draw stick border
        gc.setStroke(Color.rgb(101, 67, 33));
        gc.setLineWidth(1);
        gc.strokeRect(0, -STICK_WIDTH/2, length, STICK_WIDTH);
        
        // Add highlight on top
        gc.setStroke(Color.rgb(205, 133, 63));
        gc.setLineWidth(0.8);
        gc.strokeLine(0, -STICK_WIDTH/2 + 0.5, length, -STICK_WIDTH/2 + 0.5);
        
        gc.restore();
    }

    // Getters
    public double getBaseX() { return baseX; }
    public double getBaseY() { return baseY; }
    public double getLength() { return length; }
    public double getRotation() { return rotation; }
    public boolean isGrowing() { return isGrowing; }
    public boolean isRotating() { return isRotating; }
    public boolean isRotationComplete() { return rotationComplete; }
    
    /**
     * Get the end point of the stick after rotation
     */
    public double getEndX() {
        double radians = Math.toRadians(rotation);
        return baseX + length * Math.cos(radians);
    }
    
    public double getEndY() {
        double radians = Math.toRadians(rotation);
        return baseY + length * Math.sin(radians);
    }
    
    /**
     * Check if the stick can support the hero walking on it
     */
    public boolean canWalkOn(double x, double y) {
        if (!rotationComplete) return false;
        
        double endX = getEndX();
        double endY = getEndY();
        
        // Check if point is roughly on the stick line
        double tolerance = STICK_WIDTH;
        double distanceToLine = pointToLineDistance(baseX, baseY, endX, endY, x, y);
        
        return distanceToLine <= tolerance && 
               x >= Math.min(baseX, endX) && 
               x <= Math.max(baseX, endX);
    }
    
    private double pointToLineDistance(double x1, double y1, double x2, double y2, 
                                     double pointX, double pointY) {
        double A = pointX - x1;
        double B = pointY - y1;
        double C = x2 - x1;
        double D = y2 - y1;
        
        double dot = A * C + B * D;
        double lenSq = C * C + D * D;
        double param = -1;
        if (lenSq != 0) {
            param = dot / lenSq;
        }
        
        double xx, yy;
        if (param < 0) {
            xx = x1;
            yy = y1;
        } else if (param > 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }
        
        double dx = pointX - xx;
        double dy = pointY - yy;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
