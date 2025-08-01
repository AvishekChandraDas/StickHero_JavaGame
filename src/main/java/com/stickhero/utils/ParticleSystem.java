package com.stickhero.utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Simple particle system for visual effects in the Stick Hero game
 */
public class ParticleSystem {
    
    private List<Particle> particles;
    private Random random;
    
    public ParticleSystem() {
        particles = new ArrayList<>();
        random = new Random();
    }
    
    public void update(double deltaTime) {
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            particle.update(deltaTime);
            if (particle.isDead()) {
                iterator.remove();
            }
        }
    }
    
    public void render(GraphicsContext gc) {
        for (Particle particle : particles) {
            particle.render(gc);
        }
    }
    
    /**
     * Create particles for success effect
     */
    public void createSuccessParticles(double x, double y) {
        for (int i = 0; i < 15; i++) {
            double velocityX = (random.nextDouble() - 0.5) * 100;
            double velocityY = -random.nextDouble() * 80 - 20; // Upward
            Color color = Color.hsb(60 + random.nextDouble() * 60, 0.8, 0.9); // Yellow to green
            double size = 3 + random.nextDouble() * 4;
            double lifetime = 1.0 + random.nextDouble() * 1.5;
            
            particles.add(new Particle(x, y, velocityX, velocityY, color, size, lifetime, ParticleType.SPARK));
        }
    }
    
    /**
     * Create particles for failure effect
     */
    public void createFailParticles(double x, double y) {
        for (int i = 0; i < 20; i++) {
            double velocityX = (random.nextDouble() - 0.5) * 120;
            double velocityY = -random.nextDouble() * 60 - 10;
            Color color = Color.hsb(0 + random.nextDouble() * 30, 0.8, 0.9); // Red to orange
            double size = 2 + random.nextDouble() * 5;
            double lifetime = 1.5 + random.nextDouble() * 2.0;
            
            particles.add(new Particle(x, y, velocityX, velocityY, color, size, lifetime, ParticleType.SPARK));
        }
    }
    
    /**
     * Create dust particles when hero lands
     */
    public void createDustParticles(double x, double y) {
        for (int i = 0; i < 8; i++) {
            double velocityX = (random.nextDouble() - 0.5) * 40;
            double velocityY = -random.nextDouble() * 30;
            Color color = Color.rgb(139, 119, 101, 0.7); // Dusty brown
            double size = 2 + random.nextDouble() * 3;
            double lifetime = 0.5 + random.nextDouble() * 1.0;
            
            particles.add(new Particle(x, y, velocityX, velocityY, color, size, lifetime, ParticleType.DUST));
        }
    }
    
    /**
     * Create particles for stick impact
     */
    public void createStickImpactParticles(double x, double y) {
        for (int i = 0; i < 5; i++) {
            double velocityX = (random.nextDouble() - 0.5) * 30;
            double velocityY = -random.nextDouble() * 20;
            Color color = Color.BROWN;
            double size = 1 + random.nextDouble() * 2;
            double lifetime = 0.3 + random.nextDouble() * 0.5;
            
            particles.add(new Particle(x, y, velocityX, velocityY, color, size, lifetime, ParticleType.DEBRIS));
        }
    }
    
    /**
     * Clear all particles
     */
    public void clear() {
        particles.clear();
    }
    
    /**
     * Get number of active particles
     */
    public int getParticleCount() {
        return particles.size();
    }
    
    /**
     * Individual particle class
     */
    private static class Particle {
        private double x, y;
        private double velocityX, velocityY;
        private double gravity = 150; // pixels/second²
        private Color color;
        private double size;
        private double lifetime;
        private double age;
        private ParticleType type;
        private double initialSize;
        
        public Particle(double x, double y, double velocityX, double velocityY, 
                       Color color, double size, double lifetime, ParticleType type) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.color = color;
            this.size = size;
            this.initialSize = size;
            this.lifetime = lifetime;
            this.age = 0;
            this.type = type;
        }
        
        public void update(double deltaTime) {
            age += deltaTime;
            
            // Update position
            x += velocityX * deltaTime;
            y += velocityY * deltaTime;
            
            // Apply gravity (except for certain particle types)
            if (type != ParticleType.FLOAT) {
                velocityY += gravity * deltaTime;
            }
            
            // Apply air resistance
            velocityX *= 0.98;
            velocityY *= 0.98;
            
            // Update visual properties based on age
            double ageRatio = age / lifetime;
            
            switch (type) {
                case SPARK:
                    // Sparks fade and shrink
                    size = initialSize * (1.0 - ageRatio);
                    double alpha = Math.max(0.0, Math.min(1.0, 1.0 - ageRatio));
                    color = Color.color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
                    break;
                    
                case DUST:
                    // Dust grows and fades
                    size = initialSize * (1.0 + ageRatio * 0.5);
                    alpha = Math.max(0.0, Math.min(1.0, 1.0 - ageRatio));
                    color = Color.color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
                    break;
                    
                case DEBRIS:
                    // Debris just fades
                    alpha = Math.max(0.0, Math.min(1.0, 1.0 - ageRatio));
                    color = Color.color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
                    break;
            }
        }
        
        public void render(GraphicsContext gc) {
            if (size <= 0) return;
            
            gc.setFill(color);
            
            switch (type) {
                case SPARK:
                    // Draw as bright circle with glow effect
                    gc.fillOval(x - size/2, y - size/2, size, size);
                    // Add glow
                    Color glowColor = Color.color(color.getRed(), color.getGreen(), color.getBlue(), 0.3);
                    gc.setFill(glowColor);
                    gc.fillOval(x - size, y - size, size * 2, size * 2);
                    break;
                    
                case DUST:
                    // Draw as soft circle
                    gc.fillOval(x - size/2, y - size/2, size, size);
                    break;
                    
                case DEBRIS:
                    // Draw as small rectangle
                    gc.fillRect(x - size/2, y - size/2, size, size);
                    break;
            }
        }
        
        public boolean isDead() {
            return age >= lifetime || size <= 0;
        }
    }
    
    private enum ParticleType {
        SPARK, DUST, DEBRIS, FLOAT
    }
}
