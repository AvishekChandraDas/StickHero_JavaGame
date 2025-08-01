package com.stickhero.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents the hero character in the Stick Hero game
 */
public class Hero {
    
    private double x, y;
    private double startX, startY;
    private double targetX, targetY;
    private double walkDistance;
    private double currentWalkDistance;
    
    private boolean isWalking;
    private boolean walkingComplete;
    private double walkingSpeed;
    private double animationTime;
    
    // Visual properties
    private double width, height;
    private Color bodyColor;
    private Color headColor;
    
    // Animation
    private int walkFrame;
    private double frameTime;
    private static final double FRAME_DURATION = 0.2; // seconds per frame
    private static final int WALK_FRAMES = 4;
    
    // Constants
    private static final double WALKING_SPEED = 120.0; // pixels per second
    private static final double HERO_WIDTH = 20.0;
    private static final double HERO_HEIGHT = 40.0;

    public Hero(double x, double y) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.width = HERO_WIDTH;
        this.height = HERO_HEIGHT;
        this.walkingSpeed = WALKING_SPEED;
        this.bodyColor = Color.BLUE;
        this.headColor = Color.PEACHPUFF;
        
        this.isWalking = false;
        this.walkingComplete = false;
        this.walkDistance = 0;
        this.currentWalkDistance = 0;
        this.animationTime = 0;
        this.walkFrame = 0;
        this.frameTime = 0;
    }

    public void startWalking(double distance) {
        this.walkDistance = distance;
        this.currentWalkDistance = 0;
        this.startX = x;
        this.startY = y;
        this.targetX = x + distance;
        this.targetY = y; // Hero walks horizontally
        this.isWalking = true;
        this.walkingComplete = false;
        this.animationTime = 0;
    }

    public void update(double deltaTime) {
        if (isWalking && !walkingComplete) {
            // Update walking animation
            animationTime += deltaTime;
            frameTime += deltaTime;
            
            // Update walk frame
            if (frameTime >= FRAME_DURATION) {
                walkFrame = (walkFrame + 1) % WALK_FRAMES;
                frameTime = 0;
            }
            
            // Update position
            currentWalkDistance += walkingSpeed * deltaTime;
            
            if (currentWalkDistance >= walkDistance) {
                currentWalkDistance = walkDistance;
                x = targetX;
                y = targetY;
                isWalking = false;
                walkingComplete = true;
            } else {
                // Interpolate position
                double progress = currentWalkDistance / walkDistance;
                x = startX + (targetX - startX) * progress;
                y = startY + (targetY - startY) * progress;
                
                // Add slight bouncing animation while walking
                double bounceOffset = Math.sin(animationTime * 8) * 2;
                y += bounceOffset;
            }
        }
    }

    public void render(GraphicsContext gc) {
        gc.save();
        
        // Draw shadow
        gc.setFill(Color.rgb(0, 0, 0, 0.3));
        gc.fillOval(x - width/2 + 2, y + height/2 + 2, width, height/4);
        
        // Calculate walking animation offset
        double legOffset = 0;
        if (isWalking) {
            legOffset = Math.sin(animationTime * 10) * 3; // Leg swing animation
        }
        
        // Draw legs
        gc.setStroke(bodyColor);
        gc.setLineWidth(3);
        // Left leg
        gc.strokeLine(x - 3, y + height/2, x - 3 - legOffset, y + height - 5);
        // Right leg
        gc.strokeLine(x + 3, y + height/2, x + 3 + legOffset, y + height - 5);
        
        // Draw body
        gc.setFill(bodyColor);
        gc.fillRoundRect(x - width/4, y, width/2, height/2, 5, 5);
        
        // Draw arms
        gc.setStroke(bodyColor);
        gc.setLineWidth(2);
        double armOffset = isWalking ? Math.sin(animationTime * 8) * 4 : 0;
        // Left arm
        gc.strokeLine(x - width/4, y + 8, x - width/3 + armOffset, y + height/3);
        // Right arm
        gc.strokeLine(x + width/4, y + 8, x + width/3 - armOffset, y + height/3);
        
        // Draw head
        gc.setFill(headColor);
        gc.fillOval(x - width/3, y - height/4, width * 2/3, height/3);
        
        // Draw face
        gc.setFill(Color.BLACK);
        // Eyes
        gc.fillOval(x - 5, y - 10, 2, 2);
        gc.fillOval(x + 3, y - 10, 2, 2);
        // Mouth
        if (isWalking) {
            // Happy walking expression
            gc.strokeLine(x - 3, y - 5, x + 3, y - 5);
        } else {
            // Neutral expression
            gc.fillOval(x - 1, y - 5, 2, 2);
        }
        
        // Draw simple hat or hair
        gc.setFill(Color.DARKBLUE);
        gc.fillOval(x - width/3, y - height/3, width * 2/3, height/4);
        
        gc.restore();
    }

    // Position methods
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
    }

    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public boolean isWalking() { return isWalking; }
    public boolean isWalkingComplete() { return walkingComplete; }
    public double getWalkDistance() { return walkDistance; }
    public double getCurrentWalkDistance() { return currentWalkDistance; }
    
    /**
     * Get the hero's bounding box for collision detection
     */
    public double getLeftX() { return x - width/2; }
    public double getRightX() { return x + width/2; }
    public double getTopY() { return y - height/4; }
    public double getBottomY() { return y + height; }
    
    /**
     * Check if hero has fallen off the world
     */
    public boolean hasFallen(double worldHeight) {
        return y > worldHeight;
    }
    
    /**
     * Reset hero state for new game
     */
    public void reset(double x, double y) {
        setPosition(x, y);
        isWalking = false;
        walkingComplete = false;
        walkDistance = 0;
        currentWalkDistance = 0;
        animationTime = 0;
        walkFrame = 0;
        frameTime = 0;
    }
    
    /**
     * Make hero fall (for game over animation)
     */
    public void startFalling() {
        isWalking = false;
        walkingComplete = true;
        // Could add falling animation here
    }
}
