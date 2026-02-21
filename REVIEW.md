# Code Review - DigOut Project

**Review Date**: 2026-02-21  
**Reviewer**: AI Code Review  
**Version**: 1.1.3

---

## Overview

DigOut is a libGDX-based survival game where players manage survivors in a collapsed mine. The codebase consists of approximately 50 Java files across core and lwjgl3 modules. The architecture follows a manager-based pattern with game state serialization support.

**Overall Assessment**: The codebase demonstrates functional game logic but has several issues affecting maintainability, performance, and potential runtime stability. Priority should be given to resource management and static state issues.

---

## 🔴 Blocking Issues

### 1. Resource Leak - Texture Objects Not Disposed

**Location**: Multiple files

- `GameScreen.java:315`, `GameScreen.java:552`, `GameScreen.java:838`
- `Survivor.java:35`, `Survivor.java:141-142`
- `uiHelps.java:152`, `uiHelps.java:644-647`

**Problem**: `Texture` objects are created repeatedly without being disposed. libGDX textures are native resources that must be explicitly disposed to prevent memory leaks.

**Impact**: Memory leaks during gameplay, especially when survivor bars refresh or UI updates occur.

**Suggestion**: 
```java
// Use a TextureAtlas or cache textures in a manager
public class TextureManager implements Disposable {
    private final Map<String, Texture> textureCache = new HashMap<>();
    
    public Texture getTexture(String path) {
        return textureCache.computeIfAbsent(path, 
            p -> new Texture(Gdx.files.internal(p)));
    }
    
    @Override
    public void dispose() {
        textureCache.values().forEach(Texture::dispose);
        textureCache.clear();
    }
}
```

---

### 2. Static Mutable State in GameScreen

**Location**: `GameScreen.java:39-68`

**Problem**: 20+ static mutable fields used for UI state management:
```java
private static boolean needsRefreshAfterAddEQ = false;
private static boolean isGameWin = false;
private static Survivor tempSurvivor = null;
private static Table addEqAndTaskTable, outerPinRoomTable, centerTable...
```

**Impact**: 
- State persists between screen transitions
- Potential race conditions
- Difficult to test in isolation
- Memory leaks (screens not garbage collected)

**Suggestion**: Pass state via constructor or use a dedicated GameState class instance rather than static fields.

---

### 3. NullPointerException Risk in RoomManager

**Location**: `RoomManager.java:278-283`

**Problem**: 
```java
public boolean checkIsRookRoom(Coordinate coordinate) {
    if(rooms.get(coordinate).equals(Constants.RoomType.HARD_ROOK_TYPE) || 
       rooms.get(coordinate).equals(Constants.RoomType.LIGHT_ROOK_TYPE))
```
No null check before calling `equals()` on potentially null room.

**Impact**: `NullPointerException` when coordinate doesn't exist in rooms map.

**Suggestion**:
```java
public boolean checkIsRookRoom(Coordinate coordinate) {
    Room room = rooms.get(coordinate);
    if (room == null) return false;
    return room.getType() == Constants.RoomType.HARD_ROOK_TYPE || 
           room.getType() == Constants.RoomType.LIGHT_ROOK_TYPE;
}
```

---

### 4. Incorrect Log File Path

**Location**: `DigOutGame.java:32`

**Problem**: 
```java
public static File LOGFILE = new File("../../../digout.log");
```

**Impact**: Log file may be created in unexpected location; relative path is fragile and depends on working directory.

**Suggestion**: Use `Gdx.files.local("digout.log")` or an absolute path based on user home directory.

---

### 5. Logging IOException Swallowed

**Location**: `DateLogs.java:91-104`

**Problem**: 
```java
private static void writeToFile(File myObj, String logName, String message, Throwable throwable) {
    try {
        FileWriter myWriter = new FileWriter(myObj.getName(), true);
        // ...
    } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }
}
```

**Impact**: Silent logging failures; application continues without persistent logs.

**Suggestion**: At minimum, log to stderr. Consider using libGDX's built-in file handling.

---

### 6. Unused AWT Import

**Location**: `DigOutGame.java:16`

**Problem**: `import java.awt.*;` is imported but AWT is not compatible with libGDX and can cause issues on some platforms.

**Impact**: Potential conflicts with libGDX's OpenGL context; unused code.

**Suggestion**: Remove the import. If `Desktop` class was intended, it's not used.

---

## 🟡 Suggestions

### 1. God Class - GameScreen

**Location**: `GameScreen.java` (1197 lines)

**Current**: Single class handling UI, game logic, rendering, and state management.

**Suggested**: Split into smaller, focused classes:
- `GameUIRenderer` - UI layout and rendering
- `GameInputHandler` - Input processing
- `GameStateManager` - State transitions
- `SurvivorPanel` - Survivor bar logic
- `ResourcePanel` - Resource bar logic

---

### 2. Magic Numbers Throughout Codebase

**Location**: Multiple files

**Current**:
```java
if(coordinate.getY() > 8) { // RoomManager.java:116
if (survivorCount >= 4) { // SurvivorManager.java:443
float boxWidth = Gdx.graphics.getWidth() / 3f; // uiHelps.java:46
```

**Suggested**: Extract to named constants:
```java
private static final int HARD_ROOM_MIN_LEVEL = 9;
private static final int OXYGEN_WARNING_THRESHOLD = 4;
private static final float INFO_BOX_WIDTH_RATIO = 1f / 3f;
```

---

### 3. Complex Conditional Logic

**Location**: `RoomManager.java:163-235`

**Current**: 70+ line method with deeply nested conditions for room discovery.

**Suggested**: Extract to smaller, named methods:
```java
private boolean canDiscoverUpward(Coordinate coord, boolean hasElevator) { ... }
private boolean canDiscoverLeft(Coordinate coord) { ... }
private boolean canDiscoverRight(Coordinate coord) { ... }
```

---

### 4. Inconsistent Error Handling

**Location**: Various files

**Current**: Mix of `System.out.println`, `System.err.println`, and custom logging.

**Suggested**: Standardize on `DateLogs.logs()` method throughout.

---

### 5. Missing Input Validation

**Location**: `Player.java`, `NewGameNameScreen.java`

**Current**: Player name accepted without validation.

**Suggested**: Add validation for:
- Empty strings
- Maximum length
- Special characters that could cause display issues

---

### 6. TODO Comments Without Tracking

**Location**: 
- `MyGame.java:94`: `//TODO zrobić volume`
- `uiHelps.java:153`: `//TODO Dodać slidera mojego`

**Suggested**: Create GitHub issues for these and reference them in code.

---

### 7. Dead Code / Commented Code

**Location**: 
- `RoomManager.java:69-76`: Commented `getElevator()` method
- `SurvivorManager.java:116-153`: Commented debug avatar loading
- `GameScreen.java:292-295`: Commented survivor initialization
- `GameScreen.java:373-384`: Commented debug table lines

**Suggested**: Remove or document why it's kept.

---

### 8. Mixed Language Comments

**Location**: Multiple files contain Polish comments:
- `RoomManager.java:161`: `// Pobierz JAR, w którym jest uruchomiona aplikacja`
- `SurvivorManager.java:202`: `Brak dostępnych avatarów...`

**Suggested**: Standardize on English for codebase consistency.

---

### 9. Potential Integer Overflow

**Location**: `GameScreen.java:70-71`

**Current**:
```java
public static final float roomWidth = Gdx.graphics.getWidth() * 4 / 5f / 10;
public static final float roomHeight = Gdx.graphics.getHeight() * 11 / 14f / 10 - 2;
```

**Suggested**: These are computed at class load time. Move to instance initialization or resize() method.

---

### 10. Survivor.energy Icon Path Construction

**Location**: `Survivor.java:141`

**Current**:
```java
Texture energyTexture = new Texture(Gdx.files.internal("energy/ENERGY_" + energy + ".png"));
```

**Problem**: Creates new texture every time `getEnergyIconDrawable()` is called.

**Suggested**: Pre-load energy icons or cache them.

---

## 🟢 Highlights

### 1. Well-Structured Enums

`Constants.java` provides clear enumerations for buildings, room types, resources, equipment, survivors, and tasks. This makes the codebase self-documenting in these areas.

### 2. Proper Serializable Implementation

Game state classes correctly implement `Serializable` with `transient` for non-serializable fields like `Texture`.

### 3. Coordinate Class Implementation

`Coordinate.java` properly implements `equals()`, `hashCode()`, and `toString()` - essential for HashMap usage.

### 4. Resource Allocation Pattern

`Resource.java` implements a clean allocation/consumption pattern that prevents overspending resources.

### 5. Consistent Package Structure

The codebase follows a logical package structure separating concerns (managers, scenes, survivor, rooms, stuff).

---

## Test Coverage Assessment

**Status**: No automated tests configured.

The project relies on manual testing via `./gradlew lwjgl3:run`. Consider adding:
- Unit tests for manager classes (RoomManager, SurvivorManager, ResourceManager)
- Integration tests for game state serialization
- UI tests for critical user flows

**Recommended Test Framework**: JUnit 5 with Mockito for mocking libGDX dependencies.

---

## Security Checklist

| Item | Status | Notes |
|------|--------|-------|
| Input validation | ⚠️ | Player names not validated |
| File path injection | ⚠️ | Log file path is relative |
| Sensitive data exposure | ✅ | No credentials in code |
| Dependency security | ✅ | Standard libGDX dependencies |
| Save file integrity | ⚠️ | Java serialization vulnerable to tampering |

---

## Performance Concerns

1. **Texture Creation in Render Loop**: `Survivor.getEnergyIconDrawable()` creates textures that should be cached.

2. **Linear Search in Hot Paths**: `RoomManager.getExitRoom()` and `getBaseRoom()` iterate all rooms every call - cache these references.

3. **String Concatenation in Loops**: Multiple string concatenations in UI building; consider StringBuilder for complex strings.

4. **Map Re-iteration**: Several methods iterate over the same map multiple times when once would suffice.

---

## Recommendations Summary

### Immediate (Before Next Release)
1. Fix Texture memory leaks
2. Add null checks in `RoomManager.checkIsRookRoom()`
3. Fix log file path
4. Remove AWT import

### Short Term (Next Sprint)
1. Refactor static state in GameScreen
2. Extract magic numbers to constants
3. Remove dead/commented code
4. Standardize logging

### Long Term (Future Releases)
1. Split GameScreen into smaller classes
2. Add automated tests
3. Implement proper input validation
4. Consider save file encryption/checksums

---

## Files Reviewed

| File | Lines | Issues |
|------|-------|--------|
| DigOutGame.java | 87 | 2 |
| GameScreen.java | 1197 | 8 |
| RoomManager.java | 319 | 5 |
| SurvivorManager.java | 472 | 4 |
| ResourceManager.java | 80 | 0 |
| uiHelps.java | 866+ | 3 |
| DateLogs.java | 109 | 2 |
| Survivor.java | 174 | 2 |
| Constants.java | 121 | 0 |
| MyGame.java | 106 | 2 |
| JsonLoader.java | 54 | 1 |

---

*Review generated using systematic code review methodology.*
