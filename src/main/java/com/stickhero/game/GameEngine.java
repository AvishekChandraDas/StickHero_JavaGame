package com.stickhero.game;

import com.stickhero.entities.Hero;
import com.stickhero.entities.Platform;
import com.stickhero.entities.Stick;
import com.stickhero.ui.GameUI;
import com.stickhero.utils.AssetManager;
import com.stickhero.utils.ParticleSystem;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main game engine handling game loop, rendering, and game logic
 */
public class GameEngine {
    
    private final Canvas gameCanvas;
    private final GraphicsContext gc;
    private final GameUI gameUI;
    private final AssetManager assetManager;
    private final ParticleSystem particleSystem;
    
    private AnimationTimer gameLoop;
    private long lastFrameTime;
    private double deltaTime;
    
    // Game state
    private GameState currentState;
    private int score;
    private boolean isMousePressed;
    
    // Game objects
    private Hero hero;
    private Stick currentStick;
    private List<Platform> platforms;
    private Random random;
    
    // Camera and scaling
    private double cameraOffsetX;
    private double scaleX, scaleY;
    
    // Constants
    private static final double GROUND_HEIGHT = 150;
    private static final double PLATFORM_HEIGHT = 100;
    private static final double MIN_PLATFORM_GAP = 50;
    private static final double MAX_PLATFORM_GAP = 200;
    private static final double MIN_PLATFORM_WIDTH = 50;
    private static final double MAX_PLATFORM_WIDTH = 100;

    public enum GameState {
        READY, GROWING_STICK, ROTATING_STICK, HERO_WALKING, GAME_OVER
    }

    public GameEngine(double width, double height) {
        gameCanvas = new Canvas(width, height);
        gc = gameCanvas.getGraphicsContext2D();
        gameUI = new GameUI();
        assetManager = new AssetManager();
        particleSystem = new ParticleSystem();
        
        platforms = new ArrayList<>();
        random = new Random();
        scaleX = scaleY = 1.0;
        
        initializeGame();
        setupInputHandlers();
    }

    private void initializeGame() {
        currentState = GameState.READY;
        score = 0;
        cameraOffsetX = 0;
        
        // Create initial platforms
        platforms.clear();
        Platform firstPlatform = new Platform(50, gameCanvas.getHeight() - GROUND_HEIGHT - PLATFORM_HEIGHT, 100, PLATFORM_HEIGHT);
        platforms.add(firstPlatform);
        
        generateNextPlatform();
        
        // Create hero
        hero = new Hero(firstPlatform.getX() + firstPlatform.getWidth() - 30, 
                       firstPlatform.getY() - 40);
        
        currentStick = null;
    }

    private void generateNextPlatform() {
        if (platforms.isEmpty()) return;
        
        Platform lastPlatform = platforms.get(platforms.size() - 1);
        double gap = MIN_PLATFORM_GAP + random.nextDouble() * (MAX_PLATFORM_GAP - MIN_PLATFORM_GAP);
        double width = MIN_PLATFORM_WIDTH + random.nextDouble() * (MAX_PLATFORM_WIDTH - MIN_PLATFORM_WIDTH);
        double x = lastPlatform.getX() + lastPlatform.getWidth() + gap;
        double y = lastPlatform.getY();
        
        platforms.add(new Platform(x, y, width, PLATFORM_HEIGHT));
    }

    private void setupInputHandlers() {
        gameCanvas.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY && currentState == GameState.READY) {
                startGrowingStick();
                isMousePressed = true;
            }
        });
        
        gameCanvas.setOnMouseReleased(e -> {
            if (e.getButton() == MouseButton.PRIMARY && currentState == GameState.GROWING_STICK) {
                stopGrowingAndRotateStick();
                isMousePressed = false;
            }
        });
        
        // Add keyboard input for restart
        gameCanvas.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.R && currentState == GameState.GAME_OVER) {
                restartGame();
            }
        });
        
        // Make canvas focusable for input
        gameCanvas.setFocusTraversable(true);
    }

    private void startGrowingStick() {
        if (platforms.size() < 2) return; // Safety check
        
        Platform currentPlatform = platforms.get(platforms.size() - 2);
        double stickX = currentPlatform.getX() + currentPlatform.getWidth();
        double stickY = currentPlatform.getY();
        
        currentStick = new Stick(stickX, stickY);
        currentState = GameState.GROWING_STICK;
    }

    private void stopGrowingAndRotateStick() {
        if (currentStick != null) {
            currentStick.startRotating();
            currentState = GameState.ROTATING_STICK;
        }
    }

    public void start() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastFrameTime == 0) {
                    lastFrameTime = now;
                    return;
                }
                
                deltaTime = (now - lastFrameTime) / 1_000_000_000.0;
                lastFrameTime = now;
                
                update(deltaTime);
                render();
            }
        };
        gameLoop.start();
    }

    public void stop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    private void update(double deltaTime) {
        switch (currentState) {
            case GROWING_STICK:
                if (currentStick != null) {
                    currentStick.grow(deltaTime);
                }
                break;
                
            case ROTATING_STICK:
                if (currentStick != null) {
                    currentStick.update(deltaTime);
                    if (currentStick.isRotationComplete()) {
                        startHeroWalking();
                    }
                }
                break;
                
            case HERO_WALKING:
                hero.update(deltaTime);
                if (hero.isWalkingComplete()) {
                    checkCollisionAndContinue();
                }
                break;
        }
        
        particleSystem.update(deltaTime);
        updateCamera();
    }

    private void startHeroWalking() {
        if (currentStick != null) {
            double walkDistance = currentStick.getLength();
            hero.startWalking(walkDistance);
            currentState = GameState.HERO_WALKING;
        }
    }

    private void checkCollisionAndContinue() {
        if (platforms.size() < 2) return; // Safety check
        
        Platform nextPlatform = platforms.get(platforms.size() - 1);
        double heroEndX = hero.getX() + hero.getWalkDistance();
        
        // Check if hero lands on the platform
        if (heroEndX >= nextPlatform.getX() && 
            heroEndX <= nextPlatform.getX() + nextPlatform.getWidth()) {
            
            // Success!
            score++;
            particleSystem.createSuccessParticles(heroEndX, nextPlatform.getY());
            
            // Move hero to new platform
            hero.setPosition(nextPlatform.getX() + nextPlatform.getWidth() - 30, 
                           nextPlatform.getY() - 40);
            
            // Generate next platform and reset
            generateNextPlatform();
            currentStick = null;
            currentState = GameState.READY;
            
        } else {
            // Game Over
            particleSystem.createFailParticles(heroEndX, hero.getY());
            currentState = GameState.GAME_OVER;
        }
    }

    private void updateCamera() {
        if (hero != null) {
            double targetCameraX = hero.getX() - gameCanvas.getWidth() / 3;
            cameraOffsetX += (targetCameraX - cameraOffsetX) * 0.05; // Smooth camera follow
        }
    }

    private void render() {
        // Clear canvas
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        
        // Save graphics state
        gc.save();
        
        // Apply camera transform
        gc.translate(-cameraOffsetX * scaleX, 0);
        gc.scale(scaleX, scaleY);
        
        // Draw background
        drawBackground();
        
        // Draw platforms
        for (Platform platform : platforms) {
            platform.render(gc);
        }
        
        // Draw stick
        if (currentStick != null) {
            currentStick.render(gc);
        }
        
        // Draw hero
        if (hero != null) {
            hero.render(gc);
        }
        
        // Draw particles
        particleSystem.render(gc);
        
        // Restore graphics state
        gc.restore();
        
        // Draw UI (not affected by camera)
        gameUI.render(gc, score, currentState);
    }

    private void drawBackground() {
        double canvasWidth = gameCanvas.getWidth() / scaleX;
        double canvasHeight = gameCanvas.getHeight() / scaleY;
        double startX = cameraOffsetX;
        double endX = startX + canvasWidth;
        
        // Sky gradient
        LinearGradient skyGradient = new LinearGradient(0, 0, 0, canvasHeight * 0.7, false, 
            CycleMethod.NO_CYCLE, new Stop(0, Color.LIGHTBLUE), new Stop(1, Color.LIGHTYELLOW));
        gc.setFill(skyGradient);
        gc.fillRect(startX, 0, canvasWidth, canvasHeight * 0.7);
        
        // Ground
        gc.setFill(Color.DARKGREEN);
        gc.fillRect(startX, canvasHeight - GROUND_HEIGHT, canvasWidth, GROUND_HEIGHT);
        
        // Ground details
        gc.setFill(Color.GREEN);
        for (double x = startX - 50; x < endX + 50; x += 30) {
            gc.fillOval(x, canvasHeight - GROUND_HEIGHT + 10, 8, 4);
        }
    }

    public Canvas getGameCanvas() {
        return gameCanvas;
    }

    public void handleResize(double newWidth, double newHeight) {
        gameCanvas.setWidth(newWidth);
        gameCanvas.setHeight(newHeight);
        
        // Calculate new scale to maintain aspect ratio
        double originalWidth = 800;
        double originalHeight = 600;
        scaleX = newWidth / originalWidth;
        scaleY = newHeight / originalHeight;
    }
    
    /**
     * Restart the game to initial state
     */
    private void restartGame() {
        currentState = GameState.READY;
        score = 0;
        cameraOffsetX = 0;
        
        // Clear platforms and recreate initial setup
        platforms.clear();
        Platform firstPlatform = new Platform(50, gameCanvas.getHeight() - GROUND_HEIGHT - PLATFORM_HEIGHT, 100, PLATFORM_HEIGHT);
        platforms.add(firstPlatform);
        generateNextPlatform();
        
        // Reset hero position
        hero.reset(firstPlatform.getX() + firstPlatform.getWidth() - 30, 
                   firstPlatform.getY() - 40);
        
        // Clear current stick and particles
        currentStick = null;
        particleSystem.clear();
    }
}
