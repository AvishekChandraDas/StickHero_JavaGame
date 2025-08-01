package com.stickhero.utils;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages loading and caching of game assets (images, sounds, etc.)
 */
public class AssetManager {
    
    private Map<String, Image> imageCache;
    private Map<String, AudioClip> audioCache;
    
    public AssetManager() {
        imageCache = new HashMap<>();
        audioCache = new HashMap<>();
        loadAssets();
    }
    
    private void loadAssets() {
        // Load default assets or create placeholder images
        createPlaceholderAssets();
    }
    
    private void createPlaceholderAssets() {
        // For now, we'll create simple colored images programmatically
        // In a real game, you'd load these from files in src/main/resources
        
        // Hero sprites could be loaded here
        // Platform textures could be loaded here
        // Background images could be loaded here
        
        // Example of how to load an image from resources:
        // Image heroImage = loadImageFromResources("/images/hero.png");
        // if (heroImage != null) {
        //     imageCache.put("hero", heroImage);
        // }
    }
    
    /**
     * Load an image from resources
     */
    public Image loadImageFromResources(String resourcePath) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(resourcePath);
            if (inputStream != null) {
                return new Image(inputStream);
            }
        } catch (Exception e) {
            System.err.println("Failed to load image: " + resourcePath);
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get an image from cache or load it if not cached
     */
    public Image getImage(String key) {
        Image image = imageCache.get(key);
        if (image == null) {
            // Try to load the image
            String resourcePath = "/images/" + key + ".png";
            image = loadImageFromResources(resourcePath);
            if (image != null) {
                imageCache.put(key, image);
            }
        }
        return image;
    }
    
    /**
     * Load an audio clip from resources
     */
    public AudioClip loadAudioFromResources(String resourcePath) {
        try {
            String url = getClass().getResource(resourcePath).toExternalForm();
            return new AudioClip(url);
        } catch (Exception e) {
            System.err.println("Failed to load audio: " + resourcePath);
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get an audio clip from cache or load it if not cached
     */
    public AudioClip getAudio(String key) {
        AudioClip audio = audioCache.get(key);
        if (audio == null) {
            // Try to load the audio
            String resourcePath = "/sounds/" + key + ".wav";
            audio = loadAudioFromResources(resourcePath);
            if (audio != null) {
                audioCache.put(key, audio);
            }
        }
        return audio;
    }
    
    /**
     * Preload an image into cache
     */
    public void preloadImage(String key, String resourcePath) {
        Image image = loadImageFromResources(resourcePath);
        if (image != null) {
            imageCache.put(key, image);
        }
    }
    
    /**
     * Preload an audio clip into cache
     */
    public void preloadAudio(String key, String resourcePath) {
        AudioClip audio = loadAudioFromResources(resourcePath);
        if (audio != null) {
            audioCache.put(key, audio);
        }
    }
    
    /**
     * Check if an image is loaded
     */
    public boolean hasImage(String key) {
        return imageCache.containsKey(key);
    }
    
    /**
     * Check if an audio clip is loaded
     */
    public boolean hasAudio(String key) {
        return audioCache.containsKey(key);
    }
    
    /**
     * Clear all cached assets
     */
    public void clearCache() {
        imageCache.clear();
        audioCache.clear();
    }
    
    /**
     * Get cache statistics
     */
    public String getCacheStats() {
        return String.format("Images cached: %d, Audio clips cached: %d", 
                           imageCache.size(), audioCache.size());
    }
}
