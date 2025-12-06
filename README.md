# 🧊🎣 Ice Fishing MiniGame – Java & Object-Oriented Programming

> This project is a **fishing minigame** developed in **Java**, applying core principles of **Object-Oriented Programming (OOP)**. The goal is to catch as many fish as possible while managing bait, avoiding underwater hazards, and controlling the hook's depth using the mouse.
>> Inspired by Club Penguin's Ice Fishing, the mini-game has been redesigned with original 2D pixel art graphics, sounds, and music.

---

## 🖥️ Main Features

### **Start Screen**

* Two buttons: **Play** and **Instructions**.
* **Instructions** opens a full-screen image with an **“X”** button to return to the start screen.
* **Play** begins the game immediately.

![Start Screen Screenshot](https://github.com/guilhermeeid/Ice-Fishing-OOP/blob/main/IceFishing/src/assets/sprites/ui/start_screen.png)

### 🖱️ **Gameplay**

* The player controls the **hook depth** by moving the mouse vertically. 🪝
* The game starts with:

  * **1 fishing line** 🪡
  * **3 worm baits** (one already equipped) 🪱
* The top-left corner displays the remaining number of worms.
* At the surface:

  * A **fish box**, initially empty, showing the number of caught fish.
  * A **worm can** to select new bait.
  * Both are **clickable**.

![GIF](https://gifdb.com/images/high/club-penguin-fishing-o3k9oqxyjc5nlklz.gif)

* Check it out on YouTube
  * [Gameplay video](https://www.youtube.com/)

---

## 🐟 Aquatic Entities

### **Common Fish**

* **Golden Fish** and **Grey Fish**

  * Are hooked upon colliding with a worm.
  * Do *not* consume the worm.
  * When brought to the surface, they are added to the fish box.

### **Mullet Fish**

* Can only be caught **using a Golden Fish as bait**.
* To equip a Golden Fish as bait:

1. Reel the line back to the surface.
2. Click the fish box.
3. One caught Golden Fish is moved to the hook, replacing the worm.
* If the player clicks the worm can while a Golden Fish is unused on the hook, it returns to the fish box and the worm becomes the current bait again.
* After successfully catching a Mullet Fish:

  * The Golden Fish bait is **consumed** and disappears from the hook.

---

## 🦈🪼 Underwater Hazards

* **Sharks** and **Jellyfish**

  * Remove the current bait (worm or Golden Fish) and any hooked fish.
  * Do *not* cut the line.
* **Boot** 🥾

  * Removes only the hooked fish, if any.
* **Metal Can** 🥫

  * **Cuts the fishing line** on collision.
  * Instantly ends the game.

---

## 🏁 Game Over Conditions

The game ends when:

1. The line is **cut** by the Metal Can, or
2. The number of worms reaches **zero**

At the end of the game:

* The **total number of caught fish** is displayed.
* The **total play time** is shown.
* The game returns to the start screen.

![Game Over Screen Screenshot](https://raw.githubusercontent.com/guilhermeeid/Ice-Fishing-OOP/refs/heads/main/IceFishing/bin/Sprites/game_over_printscreen.png)

---

## 🧱 Object-Oriented Programming Concepts Used

* **Classes and Objects** for each game entity (fish, hazards, bait, UI, etc.)
* **Inheritance** to share behavior between aquatic entities
* **Polymorphism** for different reactions when colliding with the hook
* **Encapsulation** of internal states (current bait, worm count, line status)
* **Composition and Aggregation** for structuring the game environment

---

## 🚀 How to Run the Project

1. Make sure you have:

   * **Java 17+**
   * An IDE such as Eclipse, IntelliJ IDEA, or VS Code
2. Compile the project:

   ```bash
   javac -d bin src/**/*.java
   ```
3. Run the game:

   ```bash
   java -cp bin Main
   ```

---

## 📁 Project Structure

```
src/
│
├── assets/
│   │
│   ├── fonts/
│   │   ├── Jersey10-Regular.ttf
│   │   └── License.txt
│   │
│   ├── sprites/
│   │   ├── background/
│   │   ├── fish/
│   │   ├── hazards/
│   │   ├── obstacles/
│   │   ├── player/
│   │   └── ui/
│   │
│   └── sound/
│       ├── sfx/
│       └── music/
│
├── game/
│   │
│   ├── core/
|   |   ├── entities/
│   │   ├── Entity.java
│   │   ├── Sprite.java
│   │   └── SpriteRegistry.java
│   │
│   ├── screens/
│   │   ├── MainMenuScreen.java
│   │   ├── InstructionsScreen.java
│   │   └── GameStartScreen.java
│   │
│   └── Game.java
```
---

## 👥 Authors

#### **🔗 Meet the authors:**

- **[Antônio Magalhães Roquete Macedo](https://github.com/antonio-mrm)**
- **[Guilherme Eid Godoy](https://github.com/guilhermeeid)**
- **[Lucas Espica Rezende](https://github.com/Lucasespica)**

---
