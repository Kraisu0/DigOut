# AGENTS.md - DigOut Project Guidelines

This document provides guidelines for AI coding agents working on the DigOut codebase.

## Project Overview

DigOut is a libGDX-based Java survival game where players manage survivors in a collapsed mine. The goal is to rescue survivors by excavating rooms, managing resources, and building infrastructure to reach the surface.

**Tech Stack**: Java 11+ (runtime: Java 18+), libGDX 1.12.1, Gradle, Gson, KTX (Kotlin extensions for libGDX)

## Build Commands

```bash
# Build the entire project
./gradlew build

# Run the game (desktop/LWJGL3)
./gradlew lwjgl3:run

# Clean build artifacts
./gradlew clean

# Generate JAR distribution
./gradlew lwjgl3:jar

# Build native distributions (Linux, macOS, Windows)
./gradlew lwjgl3:construo

# Compile only (no tests)
./gradlew compileJava

# Generate asset list
./gradlew generateAssetList
```

**Note**: This project does not have unit tests configured. The `compileJava` task serves as the primary verification.

## Project Structure

```
DigOut/
├── build.gradle          # Root build configuration
├── settings.gradle       # Subprojects: core, lwjgl3
├── gradle.properties     # Version configurations
├── assets/               # Game assets (images, sounds, skins)
├── core/                 # Main game logic
│   └── src/main/java/com/kraisu/digout/
│       ├── DigOutGame.java      # Main game class (extends Game)
│       ├── Main.java            # Alternative entry (unused)
│       ├── game/                # Game state, player
│       ├── managers/            # Resource, Room, Survivor, Equipment managers
│       ├── rooms/               # Room types and coordinates
│       ├── scenes/              # UI screens (GameScreen, menus, etc.)
│       ├── survivor/            # Survivor entities and tasks
│       ├── stuff/               # Equipment, resources, diary
│       ├── loaders/             # JSON loading utilities
│       ├── help/                # Constants, utilities
│       ├── logs/                # Logging system
│       └── tween/               # Animation accessors
└── lwjgl3/               # Desktop launcher
    └── src/main/java/com/kraisu/digout/lwjgl3/
        └── Lwjgl3Launcher.java  # Desktop application entry point
```

## Code Style Guidelines

### Formatting (per .editorconfig)

- **Indentation**: 4 spaces for Java/Scala/Groovy/Kotlin, 2 spaces for Gradle
- **Line endings**: LF (Unix-style)
- **Charset**: UTF-8
- **Trim trailing whitespace**: Yes
- **Final newline**: Yes

### Naming Conventions

- **Packages**: `com.kraisu.digout.<module>` (lowercase)
- **Classes**: PascalCase (e.g., `RoomManager`, `DiscoveredRoom`)
- **Methods**: camelCase (e.g., `getSurvivorManager`, `increaseRound`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_SURVIVOR_ENERGY`)
- **Enums**: PascalCase for type, UPPER_CASE for values (e.g., `Buildings.ELEVATOR`)
- **Static inner classes**: PascalCase (e.g., `Constants.BuildingPrices`)

### Imports

Organize imports in this order:
1. `java.*` / `javax.*`
2. `com.badlogic.gdx.*` (libGDX core)
3. Third-party libraries (Gson, KTX)
4. `com.kraisu.digout.*` (project-specific)

Use static imports sparingly, primarily for:
- Logging: `import static com.kraisu.digout.logs.DateLogs.logs;`
- Constants access when frequently used

### Class Structure

```java
public class ExampleClass implements Serializable {
    // 1. Static constants
    public static final int CONSTANT = 100;
    
    // 2. Instance fields (private)
    private UUID gameId;
    private String name;
    
    // 3. Constructors
    public ExampleClass(UUID gameId, String name) {
        this.gameId = gameId;
        this.name = name;
    }
    
    // 4. Getters and setters
    public UUID getGameId() { return gameId; }
    public void setGameId(UUID gameId) { this.gameId = gameId; }
    
    // 5. Business methods
    public void doSomething() { /* ... */ }
    
    // 6. toString(), equals(), hashCode() at end
    @Override
    public String toString() { /* ... */ }
}
```

### Types and nullability

- Use `UUID` for game/entity identifiers
- Use `transient` for non-serializable fields (e.g., `Texture` objects)
- Implement `Serializable` for game state classes
- Prefer `Map<K,V>` and `List<T>` over arrays for collections
- Use `LinkedHashMap` when order matters

### Error Handling

Use the project's custom logging system:

```java
import static com.kraisu.digout.logs.DateLogs.logs;

// Info log
logs(DateLogs.LogType.INFO, myGame.getGameId(), "Message", null);

// Error log with exception
logs(DateLogs.LogType.ERROR, myGame.getGameId(), "Error message", exception);

// Log levels: INFO, WARN, ERROR, DEBUG
```

For UI dialogs, use `uiHelps.displayConfirmBox()` for confirmations.

### libGDX Patterns

- **Screen lifecycle**: Implement `show()`, `render()`, `resize()`, `pause()`, `resume()`, `dispose()`
- **Stage/UI**: Use `Stage` with `ScreenViewport` for UI screens
- **Skins**: Access via `DigOutGame.skin`, `DigOutGame.skinButton`, etc.
- **Assets**: Load from internal files via `Gdx.files.internal("path")`
- **Game class**: Extend `Game` and use `setScreen()` for navigation

### UI Code (Scene2D)

```java
// Button with listener
Button myButton = new TextButton("LABEL", DigOutGame.skinButton.get("default", TextButton.TextButtonStyle.class));
myButton.addListener(new ChangeListener() {
    public void changed(ChangeEvent event, Actor actor) {
        // Handle click
    }
});

// Tables for layout
Table table = new Table();
table.setFillParent(true);
table.add(label).pad(10);
table.row();
```

## Key Classes Reference

| Class | Purpose |
|-------|---------|
| `DigOutGame` | Main Game class, loads skins, manages screens |
| `MyGame` | Game state container (player, managers, round) |
| `RoomManager` | Manages all rooms and coordinates |
| `SurvivorManager` | Handles survivor entities and their states |
| `ResourceManager` | Tracks materials, food, tools, electricity |
| `GameScreen` | Main gameplay UI screen |
| `Constants` | All enums and static configuration values |
| `DateLogs` | Centralized logging utility |

## Common Tasks

### Adding a new building type
1. Add enum value to `Constants.Buildings`
2. Add price to `Constants.BuildingPrices`
3. Create building logic in appropriate manager
4. Update UI in `GameScreen`

### Adding a new screen
1. Create class implementing `Screen`
2. Add to appropriate package under `scenes/`
3. Navigate via `game.setScreen(new NewScreen())`

### Modifying game balance
- Edit values in `Constants.BuildingPrices` and `Constants.EquipmentPrices`
- Adjust `Constants.SurvivorLimitations` for energy limits
