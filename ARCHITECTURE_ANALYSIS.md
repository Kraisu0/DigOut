# Comprehensive Architectural Analysis Report: DigOut

**Date:** February 21, 2026  
**Project:** DigOut - libGDX Survival Game  
**Analysis Type:** Game Architecture Review

---

## Executive Summary

The DigOut codebase exhibits **significant architectural issues** that will impede maintainability, testability, and future development. The most critical problems are:

- **Two god classes**: GameScreen (1197 lines), uiHelps (1655 lines)
- **Pervasive static state**: 36+ static fields in GameScreen alone
- **Tight coupling between business logic and UI**: Managers directly call UI methods
- **No separation of concerns**: Game logic embedded in UI helper classes
- **Zero testability**: No unit tests exist due to architectural constraints

---

## Table of Contents

1. [Project Structure Overview](#1-project-structure-overview)
2. [God Class Anti-Pattern](#2-god-class-anti-pattern)
3. [Static State Abuse](#3-static-state-abuse)
4. [Business Logic → UI Coupling](#4-business-logic--ui-coupling)
5. [UI → Business Logic Leakage](#5-ui--business-logic-leakage)
6. [Refresh Flag Pattern (Anti-Pattern)](#6-refresh-flag-pattern-anti-pattern)
7. [Duplicate Code](#7-duplicate-code)
8. [Constants Organization Issues](#8-constants-organization-issues)
9. [Packaging & Naming Issues](#9-packaging--naming-issues)
10. [Untestable Design](#10-untestable-design)
11. [Missing Layer Separation](#11-missing-layer-separation)
12. [Coupling Hotspots](#12-coupling-hotspots)
13. [Summary by Severity](#13-summary-by-severity)
14. [Recommended Refactoring Priority](#14-recommended-refactoring-priority)

---

## 1. Project Structure Overview

### Directory Layout

```
core/src/main/java/com/kraisu/digout/
├── DigOutGame.java           # Main Game class (87 lines)
├── Main.java                 # Unused alternative entry (28 lines)
├── game/
│   ├── MyGame.java           # Game state container (106 lines)
│   ├── Player.java           # Player entity (30 lines)
│   └── NewGame.java          # Game initialization (89 lines)
├── managers/
│   ├── RoomManager.java      # Room management (319 lines)
│   ├── SurvivorManager.java  # Survivor management (472 lines)
│   ├── ResourceManager.java  # Resource management (80 lines)
│   ├── EquipmentManager.java # Equipment management (82 lines)
│   └── DiaryManager.java     # Diary/entries (194 lines)
├── scenes/
│   ├── GameScreen.java       # Main game UI (1197 lines) ⚠️ GOD CLASS
│   ├── uiHelps.java          # UI utilities (1655 lines) ⚠️ GOD CLASS
│   ├── MainMenuScreen.java   # Main menu (144 lines)
│   ├── SplashScreen.java     # Splash screen (81 lines)
│   └── [8 other screen classes]
├── survivor/
│   ├── Survivor.java         # Survivor entity (174 lines)
│   └── Task.java             # Task entity (60 lines)
├── rooms/
│   ├── Room.java             # Abstract base (144 lines)
│   ├── Coordinate.java       # Position value object (57 lines)
│   ├── BaseRoom.java         # Base room (34 lines)
│   ├── ExitRoom.java         # Exit room (24 lines)
│   ├── DiscoveredRoom.java   # Discovered room (29 lines)
│   ├── UndiscoveredLightRoom.java (23 lines)
│   └── UndiscoveredHardRoom.java (23 lines)
├── stuff/
│   ├── Resource.java         # Resource entity (71 lines)
│   ├── Equipment.java        # Equipment entity (83 lines)
│   ├── BuildingPrice.java    # Building cost (72 lines)
│   ├── EquipmentPrice.java   # Equipment cost (70 lines)
│   ├── ReceivedStuff.java    # Task rewards (114 lines)
│   ├── Diary.java            # Diary container (198 lines)
│   └── DiaryEntry.java       # Entry formatter (98 lines)
├── help/
│   ├── Constants.java        # Game enums/config (121 lines)
│   ├── ConstantsGenerator.java # Drop rates (73 lines)
│   ├── ConstantsFileDirectory.java # File paths (9 lines)
│   └── Instruction.java      # Placeholder (10 lines)
├── loaders/
│   ├── JsonLoader.java       # JSON loading (54 lines)
│   └── CoordinateAdapter.java # Gson adapter (33 lines)
├── genertor/ (typo in package name)
│   └── Generators.java       # Random generators (126 lines)
├── logs/
│   └── DateLogs.java         # Logging utility (109 lines)
└── tween/
    └── SpriteAccessor.java   # Animation accessor (33 lines)
```

**Total: 45 Java files**

---

## 2. God Class Anti-Pattern

### 2.1 GameScreen.java (1197 lines) — CRITICAL

**Classification:** God Class / Blob

**Location:** `core/src/main/java/com/kraisu/digout/scenes/GameScreen.java`

#### Static State (36+ static fields)

```java
// GameScreen.java
private static MyGame myGame;
private static boolean isGameWin = false;
private static boolean isGameLose = false;
private static boolean isXWasClicked = true;
private static boolean isChooseRoomVisible = false;
private static Survivor tempSurvivor = null;
private static Table addEqAndTaskTable, outerPinRoomTable, centerTable, survivorsTable, roomsTable;
private static boolean isMenuOpen, isInfoBoxOpen;
private static Map<Coordinate, Table> gameTable = Map.of();
private static Button addTask, addEQ;
private static boolean needsRefreshAfterAddEQ = false;
private static boolean needsRefreshAfterAddTask = false;
private static boolean needsRefreshAfterEndRound = false;
private static Table menuTable;
```

#### Static Getters/Setters (30+ methods)

```java
public static boolean isNeedsRefreshAfterAddEQ()
public static void setNeedsRefreshAfterAddEQ(boolean needsRefreshAfterAddEQ1)
public static boolean isNeedsRefreshAfterAddTask()
public static void setNeedsRefreshAfterAddTask(boolean needsRefreshAfterAddTask1)
public static boolean isNeedsRefreshAfterEndRound()
public static void setNeedsRefreshAfterEndRound(boolean needsRefreshAfterEndRound1)
public static boolean isGameWin()
public static void setGameWin(boolean gameWin)
public static boolean isGameLose()
public static void setGameLose(boolean gameLose)
public static boolean isXWasClicked()
public static void setXWasClicked(boolean XWasClicked)
public static boolean isIsChooseRoomVisible()
public static void setIsChooseRoomVisible(boolean chooseRoomVisible)
public static Survivor getTempSurvivor()
public static void setTempSurvivor(Survivor tempSurvivor1)
public static Table getAddEqAndTaskTable()
public static void setAddEqAndTaskTable(Table addEqAndTaskTable1)
public static Table getOuterPinRoomTable()
public static void setOuterPinRoomTable(Table outerPinRoomTable1)
public static Table getCenterTable()
public static void setCenterTable(Table centerTable1)
public static Table getSurvivorsTable()
public static void setSurvivorsTable(Table survivorsTable1)
public static Table getRoomsTable()
public static void setRoomsTable(Table roomsTable1)
public static boolean isIsMenuOpen()
public static void setIsMenuOpen(boolean isMenuOpen)
public static boolean isIsInfoBoxOpen()
public static void setIsInfoBoxOpen(boolean isInfoBoxOpen)
public static Map<Coordinate, Table> getGameTable()
public static void setGameTable(Map<Coordinate, Table> gameTable)
public static Button getAddTask()
public static void setAddTask(Button addTask)
```

#### Mixed Responsibilities

| Category | Methods | Lines |
|----------|---------|-------|
| UI Rendering | `show()`, `populateResourceBar()`, `populateMiniMenu()`, `populateEqInfoTable()`, `populateSurvivorBar()`, `populateDiaryBox()` | ~400 |
| Game Logic | `discoveredRooms()`, `updateColorRoom()`, `colorTileAtCoordinate()`, `colorTileAtCoordinateBaseRoom()` | ~100 |
| State Management | 30+ static getters/setters | ~130 |
| Refresh Logic | `checkNeedsRefresh()` | ~90 |
| Input Handling | `openMenu()` | ~10 |
| UI Event Handlers | Various listeners in `show()` | ~200 |

#### Key Issues

1. **Single class manages entire game state and UI**
2. **Static fields create hidden global state**
3. **Any class can modify game state through static setters**
4. **Impossible to track state changes or debug**
5. **No encapsulation of UI components**

---

### 2.2 uiHelps.java (1655 lines) — CRITICAL

**Classification:** God Class / Utility Hell

**Location:** `core/src/main/java/com/kraisu/digout/scenes/uiHelps.java`

#### Responsibilities Breakdown

| Responsibility | Lines | Methods |
|---------------|-------|---------|
| Dialog Boxes | ~85 | `displayInfoBox()`, `displayConfirmBox()` |
| Equipment UI | ~110 | `tableAddEq()` |
| Task Selection UI | ~415 | `addChoseTask()`, `trainingTasksTable()`, `buildingTasksTable()`, `creatingTasksTable()` |
| Room Selection | ~200 | `showRoomChooser()`, `createBorderRooms()`, `drawAvailableRooms()`, `drawBaseRoom()` |
| **Task Execution (Game Logic!)** | ~180 | `doTheTasks()`, `doSurvivorTask()` |
| **Task Validation (Business Logic!)** | ~340 | `taskBlocker()` |
| Resource Management | ~45 | `reloadResources()`, `isEnoughResources()` |
| Save/Load | ~80 | `saveGame()`, `loadGame()` |
| Cost Display | ~100 | `createBuildCost()`, `createCreateCost()`, `createCreateToolsCost()` |
| Utilities | ~30 | `actionAfterCloseByX()`, `costOfTask()`, `eqPriceToBuildPrice()` |

#### Critical Issue: Game Logic in UI Class

The `doSurvivorTask()` method (lines 1404-1575) contains ALL game logic for executing tasks:

```java
// uiHelps.java:1404-1575
public void doSurvivorTask(MyGame myGame, Survivor survivor){
    switch(survivor.getTask().getTask()){
        case WAIT:
        case REST:
        case EAT:
            survivor.increaseEnergy(survivor.getTask().getReceivedStuff().getEnergyForSurvivor());
            break;
        case TRAIN_TO_COOK:
            survivor.setProfession(Constants.Survivors.COOK);
            survivor.increaseEnergy(survivor.getTask().getReceivedStuff().getEnergyForSurvivor());
            break;
        case CREATE_FOOD:
            myGame.getResourceManager().getResource(Constants.Resources.FOOD).increaseResource(
                survivor.getTask().getReceivedStuff().getFood()
            );
            // ... equipment bonus logic
            break;
        case DIG_OUT:
            // 50+ lines of resource granting, room transformation, etc.
            myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setDiscovered(true);
            myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setType(Constants.RoomType.ROOM_TO_ARRANGE);
            // ...
        case BUILD_RESTROOM:
            // Room modification logic
        // ... 15+ more cases
    }
}
```

**Problems:**
- UI class directly manipulates game state
- No separation between presentation and logic
- Cannot test game logic without instantiating UI
- Changes to game rules require modifying UI code

---

## 3. Static State Abuse

### 3.1 Global Static Fields in DigOutGame.java

**Location:** `core/src/main/java/com/kraisu/digout/DigOutGame.java`

```java
public class DigOutGame extends Game {
    public static Skin skin;
    public static Skin skinButton;
    public static Skin uiskin;
    public static Skin skinSurvivorBox;
    public static Skin skinAvatars;
    public static Skin borderSkin;
    public static Skin skinRoom;

    public static final String TITLE = "DIG OUT", VERSION = "1.1.3";
    public static File LOGFILE = new File("../../../digout.log");
    public static Integer titleBarHeight = null;
    // ...
}
```

**Problems:**
1. Any class can access skins without dependency injection: `DigOutGame.skin`
2. Makes testing impossible (cannot mock skins)
3. Initialization order is implicit
4. No clear ownership of these resources
5. Mixed `final` and mutable static fields

### 3.2 GameScreen as Global State Manager

```java
// From anywhere in codebase:
GameScreen.setGameWin(true);
GameScreen.setGameLose(true);
GameScreen.setNeedsRefreshAfterAddTask(true);
GameScreen.setNeedsRefreshAfterAddEQ(true);
GameScreen.setNeedsRefreshAfterEndRound(true);
GameScreen.setTempSurvivor(survivor);
GameScreen.setIsChooseRoomVisible(true);
GameScreen.getGameTable();
GameScreen.setTouchableEnabledGameRooms();
GameScreen.setTouchableDisabledGameRooms();
```

**Problems:**
- Hidden dependencies everywhere
- State can be modified from anywhere
- Impossible to track state changes
- No encapsulation
- Debugging state corruption is nearly impossible

### 3.3 Static Imports Creating Hidden Dependencies

```java
// uiHelps.java:33
import static com.kraisu.digout.scenes.GameScreen.*;

// This allows direct access to all GameScreen static members without qualification
// Example usage in uiHelps.java:
setNeedsRefreshAfterAddTask(true);  // Which class is this? Hard to tell.
setIsChooseRoomVisible(true);
getSurvivorsTable().setTouchable(Touchable.disabled);
```

---

## 4. Business Logic → UI Coupling

### 4.1 RoomManager Directly Calls UI

**Location:** `core/src/main/java/com/kraisu/digout/managers/RoomManager.java`

```java
// RoomManager.java:173
GameScreen.colorTileAtCoordinate(tempUp, roomU, name + "_U.0");

// RoomManager.java:181
GameScreen.colorTileAtCoordinate(tempRight, getRoom(tempUp), name + "_U.0");

// RoomManager.java:189
GameScreen.colorTileAtCoordinate(tempLeft, roomL, name + "_R_L.0");

// RoomManager.java:285-288
public void checkWinGame(){
    if(getExitRoom().getBuildUp().equals(Constants.Buildings.ELEVATOR))
        GameScreen.setGameWin(true);  // ← Business logic calling UI!
}
```

**Violation:** Managers should not know about UI existence. Win/lose conditions should be events or callbacks, not direct UI manipulation.

### 4.2 SurvivorManager Directly Calls UI

**Location:** `core/src/main/java/com/kraisu/digout/managers/SurvivorManager.java`

```java
// SurvivorManager.java:451-454
public void checkLoseGame(){
    if(survivors.isEmpty())
        GameScreen.setGameLose(true);  // ← Business logic calling UI!
}
```

### 4.3 DiaryManager Contains UI Methods

**Location:** `core/src/main/java/com/kraisu/digout/managers/DiaryManager.java`

```java
public static void displaySumBox(Stage stage, Diary diary, MyGame myGame) {
    // Creates and displays UI dialog directly
}
```

**Violation:** Manager classes should manage data, not create UI dialogs.

### Impact on Testing

```java
// Cannot test this method in isolation:
@Test
public void testCheckWinGame() {
    RoomManager manager = new RoomManager();
    // Setup exit room with elevator...
    manager.checkWinGame();
    // How do we verify win was triggered? 
    // We'd have to check GameScreen.isGameWin() - coupling test to UI!
}
```

---

## 5. UI → Business Logic Leakage

### 5.1 Task Execution in UI Helper

**Location:** `uiHelps.java:1392-1575`

The `doSurvivorTask()` method contains game logic that should be in domain layer:

```java
public void doSurvivorTask(MyGame myGame, Survivor survivor){
    switch(survivor.getTask().getTask()){
        case DIG_OUT:
            // Resource distribution
            myGame.getResourceManager().getResource(Constants.Resources.FOOD)
                .increaseResource(survivor.getTask().getReceivedStuff().getFood());
            myGame.getResourceManager().getResource(Constants.Resources.TOOLS)
                .increaseResource(survivor.getTask().getReceivedStuff().getTools());
            myGame.getResourceManager().getResource(Constants.Resources.MATERIALS)
                .increaseResource(survivor.getTask().getReceivedStuff().getMaterials());
            myGame.getResourceManager().getResource(Constants.Resources.ELECTRICITY)
                .increaseResource(survivor.getTask().getReceivedStuff().getElectricity());
            
            // Equipment distribution
            myGame.getEquipmentManager().getEquipment(Constants.Equipment.SEARCHLIGHT)
                .increaseEquipment(survivor.getTask().getReceivedStuff().getSearchlight());
            // ... more equipment
            
            // Survivor spawning
            if(survivor.getTask().getReceivedStuff().getSurvivor() != null)
                myGame.getSurvivorManager().addSurvivor(...);
            
            // Room transformation
            myGame.getRoomManager().getRoom(coordinate).setDiscovered(true);
            myGame.getRoomManager().getRoom(coordinate).setType(Constants.RoomType.ROOM_TO_ARRANGE);
            myGame.getRoomManager().getRoom(coordinate).setActualPicture("ROOM_TO_ARRANGE.0");
            break;
        // ... 15+ more task types
    }
}
```

### 5.2 Task Validation in UI Helper

**Location:** `uiHelps.java:744-1083` (340 lines)

```java
public static void taskBlocker(TextButton button, TextTooltip tooltip, MyGame myGame, 
                               Constants.Survivors profession, Constants.Tasks tasks){
    boolean hasKitchen = myGame.getRoomManager().hasBuildOfType(Constants.Buildings.KITCHEN);
    boolean hasRestroom = myGame.getRoomManager().hasBuildOfType(Constants.Buildings.RESTROOM);
    boolean hasWorkshop = myGame.getRoomManager().hasBuildOfType(Constants.Buildings.WORKSHOP);
    boolean hasTinkerRoom = myGame.getRoomManager().hasBuildOfType(Constants.Buildings.TINKER_ROOM);
    // ...
    
    switch(profession){
        case UNTRAINED:
            switch(tasks){
                case TRAIN_TO_COOK:
                    if(!hasKitchen) {
                        tooltip.getActor().setText(tooltip.getActor().getText() + 
                            "\n\n [RED]There is no Kitchen to do this action.");
                        button.setDisabled(true);
                    }
                    break;
                // ... 8 cases for UNTRAINED
            }
            break;
        case WORKER:
            // ... 10+ cases
            break;
        case COOK:
            // ... 10+ cases
            break;
        case ENGINEER:
            // ... 10+ cases
            break;
        case MINER:
            // ... 5+ cases
            break;
    }
}
```

**Problems:**
- 340 lines of nested switch statements
- Validation logic should be in domain objects (Survivor.canPerform(task))
- UI code directly contains business rules
- Changes to game rules require modifying UI code
- Cannot reuse validation logic elsewhere

---

## 6. Refresh Flag Pattern (Anti-Pattern)

### 6.1 Multiple Boolean Flags

**Location:** `GameScreen.java:58-68`

```java
private static boolean needsRefreshAfterAddEQ = false;
private static boolean needsRefreshAfterAddTask = false;
private static boolean needsRefreshAfterEndRound = false;
private static boolean isGameWin = false;
private static boolean isGameLose = false;
private static boolean isXWasClicked = true;
private static boolean isChooseRoomVisible = false;
private static boolean isMenuOpen = false;
private static boolean isInfoBoxOpen = false;
```

### 6.2 Flag Polling in Render Loop

**Location:** `GameScreen.java:448-539`

```java
private void checkNeedsRefresh() throws InterruptedException {
    if (needsRefreshAfterAddEQ) {
        needsRefreshAfterAddEQ = false;
        refreshEqInfoTable();
        refreshSurvivorBar();
        refreshResourceBar();
        survivorInfoTable.clear();
        survivorInfoTable.add(showSurvivorInfo(tempSurvivor));
        survivorsTable.setTouchable(Touchable.enabled);
    }

    if(needsRefreshAfterAddTask){
        needsRefreshAfterAddTask = false;
        updateColorRoom();
        refreshSurvivorBar();
        refreshResourceBar();
        survivorInfoTable.clear();
        survivorInfoTable.add(showSurvivorInfo(tempSurvivor));
        survivorsTable.setTouchable(Touchable.enabled);
        checkAbleToEndRound(myGame, endRoundTooltip, endRoundButton);
    }

    if(needsRefreshAfterEndRound){
        needsRefreshAfterEndRound = false;
        isXWasClicked = false;
        myGame.getSurvivorManager().allSurvivorGotReceivedStuff();
        myGame.getResourceManager().consumeAllocatedResources();
        uiHelps1.doTheTasks(myGame);
        myGame.getSurvivorManager().checkOxygenForAllLevels(myGame);
        Diary diary = new Diary();
        diary.makeEntryForAllSurvivors(myGame);
        myGame.getDiaryManager().makeDiaryEntryPerDay(myGame, diary);
        changeToDiary();
        displaySumBox(stage, diary, myGame);
        myGame.getSurvivorManager().clearSurvivorsTasks(myGame);
        myGame.getSurvivorManager().checkSurvivorStatus();
        refreshEqInfoTable();
        survivorInfoTable.clear();
        refreshSurvivorBar();
        refreshResourceBar();
        discoveredRooms(myGame);
        updateColorRoom();
        myGame.increaseRound();
        refreshMiniMenu();
        myGame.getSurvivorManager().checkLoseGame();
        myGame.getRoomManager().checkWinGame();
    }

    if (isXWasClicked && isGameLose) {
        endRoundButton.setDisabled(true);
        isGameLose = false;
        isXWasClicked = false;
        displayInfoBox(stage, "You lost...", Mark.ERROR, () -> {
            // Navigate to lose screen
        });
    }

    if (isXWasClicked && !isGameLose && isGameWin) {
        endRoundButton.setDisabled(true);
        isGameWin = false;
        isXWasClicked = false;
        displayInfoBox(stage, "You win!", Mark.INFO, () -> {
            // Navigate to win screen
        });
    }

    if(addTask != null) {
        if (isChooseRoomVisible) {
            addTask.setDisabled(true);
        } else {
            addTask.setDisabled(false);
        }
    }

    if(addEQ != null) {
        if (isChooseRoomVisible) {
            addEQ.setDisabled(true);
            // Update tooltip text
        } else {
            addEQ.setDisabled(false);
            // Update tooltip text
        }
    }
}
```

### 6.3 Problems with This Pattern

1. **Flags can be set from anywhere** - Static setters allow any class to modify flags
2. **No type safety** - Just booleans, no state machine
3. **Debugging difficulty** - Cannot easily trace who set a flag and when
4. **Race conditions possible** - Multiple flags checked/set in sequence
5. **Implicit dependencies** - Order of flag checking matters but isn't documented
6. **No event context** - When flag is set, you lose information about why/who

### 6.4 Proper Solution: Event System

```java
// What it should look like:
public interface GameEventListener {
    void onTaskAssigned(TaskAssignedEvent event);
    void onEquipmentAssigned(EquipmentAssignedEvent event);
    void onRoundEnded(RoundEndedEvent event);
    void onGameWon(GameWonEvent event);
    void onGameLost(GameLostEvent event);
}

// Or using callbacks:
public class GameEvents {
    private List<Runnable> onWinCallbacks = new ArrayList<>();
    private List<Runnable> onLoseCallbacks = new ArrayList<>();
    
    public void registerWinCallback(Runnable callback) {
        onWinCallbacks.add(callback);
    }
    
    public void triggerWin() {
        onWinCallbacks.forEach(Runnable::run);
    }
}
```

---

## 7. Duplicate Code

### 7.1 Resource Allocation Pattern (3+ Locations)

**Location 1:** `GameScreen.java:670-686`
```java
public static void backResources(Survivor survivor, MyGame myGame) {
    myGame.getResourceManager().getResource(Constants.Resources.MATERIALS).setAllocatedAmount
        (myGame.getResourceManager().getResource(Constants.Resources.MATERIALS).getAllocatedAmount()
            - survivor.getTask().getCost().getMaterials());

    myGame.getResourceManager().getResource(Constants.Resources.TOOLS).setAllocatedAmount
        (myGame.getResourceManager().getResource(Constants.Resources.TOOLS).getAllocatedAmount()
            - survivor.getTask().getCost().getTools());

    myGame.getResourceManager().getResource(Constants.Resources.FOOD).setAllocatedAmount
        (myGame.getResourceManager().getResource(Constants.Resources.FOOD).getAllocatedAmount()
            - survivor.getTask().getCost().getFood());

    myGame.getResourceManager().getResource(Constants.Resources.ELECTRICITY).setAllocatedAmount
        (myGame.getResourceManager().getResource(Constants.Resources.ELECTRICITY).getAllocatedAmount()
            - (survivor.getTask().getCost().isElectricityRequired() ? 1 : 0));
}
```

**Location 2:** `uiHelps.java:1110-1126` (reloadResources - same pattern)
```java
private static void reloadResources(Survivor survivor, MyGame myGame) {
    myGame.getResourceManager().getResource(Constants.Resources.MATERIALS).setAllocatedAmount
        (myGame.getResourceManager().getResource(Constants.Resources.MATERIALS).getAllocatedAmount()
            + survivor.getTask().getCost().getMaterials());
    // ... same pattern for TOOLS, FOOD, ELECTRICITY
}
```

**Solution:** Create a `ResourceManager.allocate(TaskCost cost, int multiplier)` method.

### 7.2 Task Cost Switch Statement

**Location:** `uiHelps.java:1319-1357`

```java
private static BuildingPrice costOfTask(Constants.Tasks task){
    BuildingPrice blank = new BuildingPrice(0,0,0,0, 1, false);
    switch(task){
        case WAIT:
        case REST:
        case TRAIN_TO_COOK:
        case CREATE_FOOD:
        case TRAIN_TO_ENGINEER:
        case DIG_OUT:
            return blank;
        case EAT:
            return new BuildingPrice(0,0,1,0, 1, false);
        case CREATE_SEARCHLIGHT:
            return eqPriceToBuildPrice(Constants.EquipmentPrices.SEARCHLIGHT_PRICE);
        case CREATE_KITCHEN_ROBOT:
            return eqPriceToBuildPrice(Constants.EquipmentPrices.KITCHEN_ROBOT_PRICE);
        case CREATE_OXYGEN_MASK:
            return eqPriceToBuildPrice(Constants.EquipmentPrices.OXYGEN_MASK_PRICE);
        case CREATE_PICKAXE:
            return eqPriceToBuildPrice(Constants.EquipmentPrices.PICKAXE_PRICE);
        case BUILD_RESTROOM:
            return Constants.BuildingPrices.RESTROOM_PRICE;
        case BUILD_KITCHEN:
            return Constants.BuildingPrices.KITCHEN_PRICE;
        case BUILD_ELEVATOR:
            return Constants.BuildingPrices.ELEVATOR_PRICE;
        case BUILD_WORKSHOP:
            return Constants.BuildingPrices.WORKSHOP_PRICE;
        case BUILD_POWER_STATION:
            return Constants.BuildingPrices.POWER_STATION_PRICE;
        case BUILD_AIR_PUMP:
            return Constants.BuildingPrices.AIR_PUMP_PRICE;
        case BUILD_TINKER_ROOM:
            return Constants.BuildingPrices.TINKER_ROOM_PRICE;
        case CREATE_TOOLS:
            return eqPriceToBuildPrice(Constants.EquipmentPrices.TOOLS_PRICE);
    }
    return blank;
}
```

**Problem:** This duplicates information already in `BuildingPrices` and `EquipmentPrices`. Should be a method on `Task` enum or a lookup map.

### 7.3 Cost Display Methods

**Location:** `uiHelps.java:641-742`

Three nearly identical methods:
- `createBuildCost(Table table, BuildingPrice buildingPrice, MyGame myGame)`
- `createCreateCost(Table table, EquipmentPrice equipmentPrice, MyGame myGame)`
- `createCreateToolsCost(Table table, EquipmentPrice equipmentPrice, MyGame myGame)`

All follow same pattern: create icons, labels, check affordability, add to table.

---

## 8. Constants Organization Issues

### 8.1 Scattered Across Multiple Files

- `Constants.java` - Main enums and prices
- `ConstantsGenerator.java` - Drop rates and random generators

### 8.2 Mutable Static Collections

**Location:** `ConstantsGenerator.java`

```java
public static class JsonData {
    public static final List<String> names = new ArrayList<>();  // MUTABLE!
    public static final Map<String, List<String>> descriptions = new HashMap<>();  // MUTABLE!
}
```

**Problem:** Despite being `final`, the collections themselves are mutable. Any code could add/remove entries.

**Solution:**
```java
public static final List<String> names = Collections.unmodifiableList(new ArrayList<>());
public static final Map<String, List<String>> descriptions = Collections.unmodifiableMap(new HashMap<>());
```

### 8.3 Constants Duplication

**Location:** `Constants.java:50-76`

```java
public static class BuildingPrices{
    public static BuildingPrice RESTROOM_PRICE = new BuildingPrice(4,0,0,1,1,false);
    public static BuildingPrice KITCHEN_PRICE = new BuildingPrice(2,0,2,1,1,false);
    public static BuildingPrice ELEVATOR_PRICE = new BuildingPrice(3,0,0,1,1,false);
    public static BuildingPrice WORKSHOP_PRICE = new BuildingPrice(3,1,0,2,1,false);
    public static BuildingPrice POWER_STATION_PRICE = new BuildingPrice(4,3,0,2,1,false);
    public static BuildingPrice AIR_PUMP_PRICE = new BuildingPrice(3,1,4,2,1,true);
    public static BuildingPrice TINKER_ROOM_PRICE = new BuildingPrice(6,1,1,2,2,true);
}

public static class TaskCost{
    public static BuildingPrice SEARCHLIGHT_ = new BuildingPrice(4,0,0,1,1,false);  // Different name
    public static BuildingPrice KITCHEN_PRICE = new BuildingPrice(2,0,2,1,1,false);  // DUPLICATE
    public static BuildingPrice ELEVATOR_PRICE = new BuildingPrice(3,0,0,1,1,false);  // DUPLICATE
    public static BuildingPrice WORKSHOP_PRICE = new BuildingPrice(3,1,0,2,1,false);  // DUPLICATE
    public static BuildingPrice POWER_STATION_PRICE = new BuildingPrice(4,3,0,2,1,false);  // DUPLICATE
    public static BuildingPrice AIR_PUMP_PRICE = new BuildingPrice(3,1,4,2,1,true);  // DUPLICATE
    public static BuildingPrice TINKER_ROOM_PRICE = new BuildingPrice(6,1,1,2,2,true);  // DUPLICATE
}
```

**Problem:** `TaskCost` class duplicates `BuildingPrices` with slightly different naming. Confusing and error-prone.

### 8.4 Inconsistent Naming in Enums

**Location:** `Constants.java:20-26`

```java
public enum RoomType{
    BASE_TYPE,
    LIGHT_ROOK_TYPE,  // Typo: "ROOK" instead of "ROOM"
    HARD_ROOK_TYPE,   // Typo
    EXIT_TYPE,
    ROOM_TO_ARRANGE;
}
```

---

## 9. Packaging & Naming Issues

### 9.1 Package Typo

```
core/src/main/java/com/kraisu/digout/genertor/  // Should be "generator"
```

This typo appears in:
- Directory name
- Package declaration in `Generators.java`
- Import statements throughout codebase

### 9.2 Inconsistent Naming Conventions

| Issue | Current | Should Be |
|-------|---------|-----------|
| Lowercase class name | `uiHelps` | `UiHelps` or `UiUtils` |
| Static final naming | `roomWidth`, `roomHeight` | `ROOM_WIDTH`, `ROOM_HEIGHT` |
| Typo in enum | `LIGHT_ROOK_TYPE` | `LIGHT_ROOM_TYPE` |
| Typo in enum | `HARD_ROOK_TYPE` | `HARD_ROOM_TYPE` |

### 9.3 Unused Code

**Location:** `Main.java`

```java
// Main.java - appears to be unused alternative entry point
public class Main {
    public static void main(String[] args) {
        // ...
    }
}
```

---

## 10. Untestable Design

Due to the architectural issues documented above, **no unit tests exist** in this codebase. The design makes testing impossible:

### 10.1 Static State Prevents Isolation

```java
// Cannot test this in isolation:
@Test
public void testRoomManager() {
    RoomManager manager = new RoomManager();
    // Any method might call GameScreen.setGameWin() or similar
    // Would need entire UI framework loaded
}
```

### 10.2 Business Logic in UI Classes

```java
// Cannot test task execution without UI:
@Test
public void testDigOutTask() {
    // doSurvivorTask() is in uiHelps
    // Would need to instantiate uiHelps, which requires Stage, Skin, etc.
}
```

### 10.3 No Interfaces for Mocking

```java
// Cannot mock dependencies:
public class SomeClass {
    public void doSomething() {
        // Direct static access - cannot mock
        GameScreen.setGameWin(true);
        DigOutGame.skin.get(...);
    }
}
```

### 10.4 Tight Coupling

```java
// Cannot test managers in isolation:
@Test
public void testSurvivorManager() {
    SurvivorManager manager = new SurvivorManager();
    manager.checkLoseGame();  // Calls GameScreen.setGameLose()
    // How to verify? Must check GameScreen static state
}
```

---

## 11. Missing Layer Separation

### 11.1 Current Architecture (Violations)

```
┌─────────────────────────────────────────────────────────────────┐
│                     NO CLEAR BOUNDARIES                          │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────────┐ │
│  │                    GameScreen (God Class)                   │ │
│  │  ┌─────────────┐  ┌──────────────┐  ┌────────────────────┐ │ │
│  │  │  UI Code    │  │ Game Logic   │  │   State Manager    │ │ │
│  │  │             │  │              │  │   (36+ statics)    │ │ │
│  │  └─────────────┘  └──────────────┘  └────────────────────┘ │ │
│  └─────────────────────────────────────────────────────────────┘ │
│         ↑ static calls              ↑ static calls               │
│         │                           │                            │
│  ┌──────┴───────────┐     ┌─────────┴───────────┐               │
│  │   RoomManager    │     │  SurvivorManager    │               │
│  │ (calls UI)       │     │ (calls UI)          │               │
│  └──────────────────┘     └─────────────────────┘               │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────────┐ │
│  │                    uiHelps (God Class)                      │ │
│  │  ┌─────────────┐  ┌──────────────┐  ┌────────────────────┐ │ │
│  │  │  UI Helpers │  │ Game Logic   │  │   Validation       │ │ │
│  │  │             │  │ (doSurvivor) │  │   (taskBlocker)    │ │ │
│  │  └─────────────┘  └──────────────┘  └────────────────────┘ │ │
│  └─────────────────────────────────────────────────────────────┘ │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

### 11.2 Expected Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                      PRESENTATION LAYER                          │
│  ┌─────────────────────────────────────────────────────────────┐│
│  │ GameScreen, MainMenuScreen, etc.                            ││
│  │ - UI rendering only                                          ││
│  │ - User input handling                                        ││
│  │ - NO game logic                                              ││
│  │ - NO static state                                            ││
│  └─────────────────────────────────────────────────────────────┘│
│                        ↓ events/callbacks                        │
├─────────────────────────────────────────────────────────────────┤
│                      APPLICATION LAYER                           │
│  ┌─────────────────────────────────────────────────────────────┐│
│  │ GameController, TaskController, etc.                        ││
│  │ - Coordinates UI and domain                                  ││
│  │ - Handles user actions                                       ││
│  │ - Manages game flow                                          ││
│  └─────────────────────────────────────────────────────────────┘│
│                        ↓ commands                                │
├─────────────────────────────────────────────────────────────────┤
│                         DOMAIN LAYER                             │
│  ┌─────────────────────────────────────────────────────────────┐│
│  │ RoomManager, SurvivorManager, ResourceManager, etc.         ││
│  │ - Pure game logic                                            ││
│  │ - NO UI dependencies                                          ││
│  │ - Emits events for UI updates                                ││
│  └─────────────────────────────────────────────────────────────┘│
│                        ↓ data access                             │
├─────────────────────────────────────────────────────────────────┤
│                         DATA LAYER                               │
│  ┌─────────────────────────────────────────────────────────────┐│
│  │ SaveManager, ConfigLoader                                    ││
│  │ - Persistence                                                ││
│  │ - Configuration                                              ││
│  └─────────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────────┘
```

---

## 12. Coupling Hotspots

```
┌────────────────────────────────────────────────────────────────────┐
│                        COUPLING HOTSPOTS                            │
├────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   GameScreen ◄──────────────────────────────────────────────┐      │
│       ▲           ▲           ▲           ▲                 │      │
│       │           │           │           │                 │      │
│       │ static    │ static    │ static    │ static          │      │
│       │ calls     │ calls     │ calls     │ calls           │      │
│       │           │           │           │                 │      │
│   RoomManager  SurvivorManager  uiHelps  DiaryManager ──────┘      │
│       │              │              │                              │
│       │              │              │ heavy access                 │
│       │              │              ▼                              │
│       │              │          MyGame                             │
│       │              │                                              │
│       └──────────────┴──────────────────────────────────► DigOutGame│
│                                     (static skin access)           │
│                                                                     │
│   DigOutGame ◄─── static skin access from ALL UI classes ─────┐    │
│                                                                │    │
└────────────────────────────────────────────────────────────────────┘

ARROWS INDICATE DIRECTION OF DEPENDENCY
Static calls create HIDDEN dependencies (not visible in class diagrams)
```

---

## 13. Summary by Severity

### CRITICAL (Must Fix Before Further Development)

| # | Issue | Location | Impact |
|---|-------|----------|--------|
| 1 | God Class: GameScreen | 1197 lines, 36+ static fields | Unmaintainable, impossible to test, hidden dependencies |
| 2 | God Class: uiHelps | 1655 lines, mixed concerns | Logic in UI, cannot test game mechanics |
| 3 | Business Logic → UI Coupling | RoomManager:287, SurvivorManager:453 | Managers untestable, circular dependencies |
| 4 | Static State Abuse | DigOutGame, GameScreen | No encapsulation, hidden dependencies, no thread safety |

### HIGH (Should Fix Soon)

| # | Issue | Location | Impact |
|---|-------|----------|--------|
| 5 | Refresh Flag Pattern | GameScreen:58-68, 448-539 | Debugging nightmare, race conditions |
| 6 | No Event System | Cross-cutting | Tight coupling everywhere |
| 7 | Game Logic in UI | uiHelps:1404-1575 | Cannot test game rules |
| 8 | Validation Logic in UI | uiHelps:744-1083 | Cannot reuse or test rules |

### MEDIUM (Fix When Possible)

| # | Issue | Location | Impact |
|---|-------|----------|--------|
| 9 | Duplicate Code | Multiple locations | Maintenance burden |
| 10 | Constants Duplication | Constants.java:50-76 | Confusion, inconsistency |
| 11 | Package Typo | genertor/ | Unprofessional, confusing |
| 12 | Inconsistent Naming | Various | Code readability |
| 13 | Mutable Static Collections | ConstantsGenerator.JsonData | Unexpected modifications |

### LOW (Nice to Have)

| # | Issue | Location | Impact |
|---|-------|----------|--------|
| 14 | Unused Code | Main.java | Dead code |
| 15 | Missing Documentation | Throughout | Onboarding difficulty |

---

## 14. Recommended Refactoring Priority

### Phase 1: Stop the Bleeding (Critical)

1. **Extract Event System**
   - Replace `GameScreen.setGameWin/Lose()` with observer pattern
   - Create `GameEvent` interface with `GameWonEvent`, `GameLostEvent`, etc.
   - Managers emit events, UI subscribes

2. **Extract TaskExecutor from uiHelps**
   - Move `doSurvivorTask()` to new `TaskExecutor` class
   - Move `survivorGotReceivedStuff()` to `TaskExecutor`
   - Place in domain layer, not UI

3. **Create TaskValidator**
   - Move `taskBlocker()` logic to domain
   - Add `Survivor.canPerform(Task task)` method
   - Add `Task.getRequirements()` method

### Phase 2: Break Up God Classes (High)

4. **Split GameScreen**
   ```
   GameScreen (thin coordinator)
   ├── ResourceBarView
   ├── MiniMenuView
   ├── SurvivorBarView
   ├── RoomGridView
   ├── InfoPanelView
   │   ├── SurvivorInfoPanel
   │   ├── EquipmentInfoPanel
   │   └── DiaryPanel
   └── GameState (non-static!)
   ```

5. **Split uiHelps**
   ```
   uiHelps (rename to UiUtils)
   ├── DialogManager (displayInfoBox, displayConfirmBox)
   ├── TaskSelectionUi
   ├── RoomSelectionUi
   ├── CostDisplayUtils
   └── SaveManager
   ```

### Phase 3: Remove Static State (Medium)

6. **Dependency Injection**
   - Pass `MyGame` through constructors
   - Pass `Skin` objects through constructors
   - Use interfaces for mocking

7. **Convert Statics to Instance Fields**
   ```java
   // Before
   private static boolean isGameWin = false;
   
   // After
   public class GameState {
       private boolean gameWon = false;
       
       public boolean isGameWon() { return gameWon; }
       public void setGameWon(boolean won) { 
           this.gameWon = won;
           notifyListeners();
       }
   }
   ```

### Phase 4: Clean Up (Low)

8. **Consolidate Constants**
   - Merge `Constants` and `ConstantsGenerator`
   - Remove `TaskCost` duplication
   - Make collections immutable

9. **Fix Naming**
   - Rename package `genertor` → `generator`
   - Rename `uiHelps` → `UiUtils`
   - Fix `ROOK_TYPE` → `ROOM_TYPE`

---

## Appendix A: Code Metrics

| File | Lines | Methods | Static Fields | Static Methods |
|------|-------|---------|---------------|----------------|
| GameScreen.java | 1197 | 50+ | 18 | 35+ |
| uiHelps.java | 1655 | 30+ | 0 | 25+ |
| RoomManager.java | 319 | 20 | 0 | 0 |
| SurvivorManager.java | 472 | 25 | 0 | 5 |
| Constants.java | 121 | 0 | 20+ | 0 |
| DigOutGame.java | 87 | 7 | 10 | 0 |

---

## Appendix B: Anti-Pattern Summary

1. **God Class** - GameScreen, uiHelps
2. **Static State Abuse** - GameScreen, DigOutGame
3. **Feature Envy** - uiHelps accessing MyGame internals
4. **Long Method** - taskBlocker (340 lines), doSurvivorTask (170 lines)
5. **Duplicate Code** - Resource allocation pattern
6. **Switch Statements** - taskBlocker, doSurvivorTask, costOfTask
7. **Primitive Obsession** - Using booleans for game state
8. **Missing Abstraction** - No TaskType hierarchy, no Event system

---

**End of Report**
