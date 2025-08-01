# Stick Hero Game

[![Java CI with Gradle](https://github.com/AvishekChandraDas/StickHero_JavaGame/actions/workflows/gradle.yml/badge.svg)](https://github.com/AvishekChandraDas/StickHero_JavaGame/actions/workflows/gradle.yml)

A JavaFX implementation of the popular Stick Hero mobile game where players must extend a stick to the perfect length to bridge gaps between platforms.

![Stick Hero Game Screenshot](https://raw.githubusercontent.com/AvishekChandraDas/StickHero_JavaGame/main/screenshots/game-screenshot.png)

## Game Description

In Stick Hero, you control a hero who must cross from one platform to another by extending a stick. Hold down the mouse button to grow the stick, then release it to drop the stick and let your hero walk across. The challenge is to make the stick exactly the right length - too short and your hero falls into the gap, too long and the stick extends past the platform.

## Features

- **Smooth Animation**: 60 FPS game loop with delta-time based animations
- **Physics Simulation**: Realistic stick rotation and hero movement
- **Particle Effects**: Visual feedback for success and failure
- **Progressive Difficulty**: Randomly generated platforms with varying gaps
- **Responsive Design**: Window resizing support with proper scaling
- **Clean Architecture**: Modular design with separated concerns

## Technical Stack

- **Java 17+**: Modern Java features and performance
- **JavaFX 17+**: Rich UI framework for graphics and animation
- **Gradle**: Build system with dependency management
- **Module System**: Java 9+ modules for better encapsulation

## Project Structure

```
src/main/java/
├── module-info.java              # Module definition
├── com/stickhero/
│   ├── App.java                  # Main application class
│   ├── entities/                 # Game objects
│   │   ├── Hero.java            # Player character
│   │   ├── Platform.java        # Game platforms
│   │   └── Stick.java           # Growing/rotating stick
│   ├── game/                     # Core game logic
│   │   └── GameEngine.java      # Main game loop and logic
│   ├── ui/                       # User interface
│   │   └── GameUI.java          # UI rendering and overlays
│   └── utils/                    # Utility classes
│       ├── AssetManager.java    # Asset loading and caching
│       └── ParticleSystem.java  # Visual effects system
└── src/main/resources/
    ├── images/                   # Game sprites and textures
    └── sounds/                   # Audio files
```

## Building and Running

### Prerequisites

- Java 17 or higher
- No additional setup required (Gradle wrapper included)

### Running the Game

```bash
# On Unix/Linux/macOS
./gradlew run

# On Windows
gradlew.bat run
```

### Building Distribution

```bash
# Create runnable JAR
./gradlew jar

# Create native runtime image (requires JDK)
./gradlew jlink
```

## Game Controls

- **Mouse Button**: Hold to grow stick, release to drop it
- **Window**: Resizable, game scales appropriately

## Game States

1. **Ready**: Waiting for player input to start growing stick
2. **Growing Stick**: Stick length increases while mouse is held
3. **Rotating Stick**: Stick rotates down after mouse release
4. **Hero Walking**: Hero walks across the stick
5. **Game Over**: Hero missed the platform

## Architecture Highlights

### Game Loop
- `AnimationTimer` provides smooth 60 FPS rendering
- Delta-time calculations ensure consistent speed across different frame rates
- Separate update and render phases for clean separation of logic

### Entity System
- Each game object (Hero, Platform, Stick) is self-contained
- Update and render methods for consistent interface
- Physics and collision detection integrated into entities

### Asset Management
- Centralized asset loading and caching
- Support for images, sounds, and other resources
- Placeholder system for development without assets

### Particle System
- Lightweight particle effects for visual polish
- Different particle types (sparks, dust, debris)
- Automatic lifecycle management

## Customization

### Adding Assets
Place image files in `src/main/resources/images/` and sound files in `src/main/resources/sounds/`. The AssetManager will automatically load them.

### Modifying Game Parameters
Key game parameters can be found as constants in:
- `GameEngine.java`: Platform dimensions, gaps, camera settings
- `Stick.java`: Growth speed, rotation speed, appearance
- `Hero.java`: Walking speed, animation timing, appearance
- `ParticleSystem.java`: Particle behavior and visual effects

### Extending Functionality
The modular architecture makes it easy to add:
- New particle effects
- Sound effects and music
- Power-ups and special abilities
- Different platform types
- Menu system and settings
- High score tracking

## Performance Considerations

- Efficient rendering with minimal object creation per frame
- Particle system automatically manages lifecycle
- Asset caching prevents repeated loading
- Canvas-based rendering for optimal performance

## Development Notes

This implementation focuses on:
- Clean, readable code structure
- Proper separation of concerns
- Extensible architecture
- Modern Java practices
- Cross-platform compatibility

The game demonstrates several important JavaFX concepts:
- Canvas and GraphicsContext for custom rendering
- AnimationTimer for game loops
- Event handling for user input
- Transforms and scaling for camera systems
- Scene management and window handling

## License

This is a demonstration project. Feel free to use and modify as needed for learning purposes.
