# AI Prison Escape — Game Specification v4
 
---
 
## JavaDoc Requirement
Every Java class and every method/function MUST have a JavaDoc comment block directly above it.
All JavaDoc blocks MUST open with `/**` and close with `*/`. No exceptions.
 
---
 
## Project Overview
 
| Field | Value |
|---|---|
| Game Title | AI Prison Escape |
| Platform | Java Console Application |
| Genre | Interactive Text Adventure / Choice-Based Escape Game |
| Language | Java |
| Input Method | Text commands via Scanner (stdin) |
| Output Method | System.out.println() to console |
 
---
 
## Opening Display (Print Exactly at Game Start)
 
Print the following premise block verbatim when the game launches, before accepting any input:
 
```
In the future, artificial intelligence has advanced beyond human control after you gave AI a
human body, allowing it to roam free in the world. The AI eventually took over the world,
enslaving humanity and imprisoning survivors.
 
You begin inside a prison cell in this dystopian future. Your objective is to travel back in
time to stop the AI uprising before it begins.
 
The prison contains multiple rooms connected by both normal pathways and a ventilation system.
You must explore carefully, collect items, talk to NPCs, and avoid deadly mistakes.
```
 
Immediately after the premise, print the following command reference:
 
```
Available Commands:
 look              — See what is in your current room
 inventory         — View items you are currently carrying
 vent              — Enter or exit the ventilation system from your current position
 paths             — Show available paths from your location (inside vent: shows the room below)
 talk to [name]    — Talk to an NPC (e.g. "talk to billy")
 interact [name]   — Alias for talk to
 trade [item]      — Trade an item to the NPC in the current room (e.g. "trade fork")
 commands          — Re-display this command list
 enter [room]      — Walk into a connected room (e.g. "enter cafeteria")
 walk [room]       — Alias for enter
 pick up [item]    — Pick up an item in the current room (e.g. "pick up coin")
drop [item]           — Drop an item from your inventory (e.g. "drop brick")
use [item] on [target] — Use an item on something in the room (e.g. "use coin on vent")

```

All commands are case-insensitive. If the player enters an unrecognized command, print:
"I don't understand that command. Type 'commands' to see the full list."

 
---
 
## Game State Flags
 
All flags are instance variables on the `Game` class. Default value is `false` unless specified.
 
```java
boolean riotStarted;       // true after Buff Dude starts the riot
boolean ventUnlocked;      // true after coin is used to unscrew the vent in Prison Room
boolean hasCoin;           // true while player carries the Coin
boolean hasBrick;          // true while player carries the Red Painted Brick
boolean hasWrench;         // true while player carries the Wrench
boolean hasFork;           // true while player carries the Fork
boolean hasFortuneCookie;  // true after receiving Fortune Cookie from Billy
boolean gameOver;          // true when any death/fail condition is triggered
```
 
---
 
## Timer
 
- A **2-minute countdown timer** starts automatically when the player picks up the Red Painted Brick.
- The timer runs in the background.
- If it expires before the player wins, `gameOver = true` and the game terminates and restarts.
- The brick "slows the player down" — this is represented entirely through the timer mechanic.
---
 
## Class Structure
 
### Main Classes
 
| Class | Responsibility |
|---|---|
| `Game` | Holds game state flags, main game loop, processes commands |
| `Player` | Tracks current room, current vent status, calls to inventory |
| `Room` | Abstract base class for all rooms |
| `Item` | Represents a collectible item (name, description) |
| `NPC` | Abstract base class for all NPCs |
| `Inventory` | Manages the player's list of held items |
 
### NPC Subclasses
 
| Subclass | Location | Behavior |
|---|---|---|
| `Billy` | Cafeteria | Trades Fortune Cookie for Fork |
| `Bookworm` | Library | Trades hints for items |
| `BuffDude` | Yard | Starts riot in exchange for Coin |
| `ScrawnyGuy` | Yard | Kills player immediately on interaction |
| `Warden` | Warden's Office | Catches player if no riot active |
| `Jilly` | Letter Room | Gives player the three time machine codes |
 
### Room Subclasses
 
| Subclass | Display Name |
|---|---|
| `PrisonRoom` | Prison Room |
| `VentilationSystem` | Ventilation System |
| `Cafeteria` | Cafeteria |
| `LetterRoom` | Letter Room |
| `Library` | Library |
| `Yard` | Yard |
| `WardensOffice` | Warden's Office |
 
---
 
## Items
 
| Item Name | Starting Location | Retained After Use? | Purpose |
|---|---|---|---|
| Coin | Prison Room | YES | Unscrew vent (one-time action), trade to Buff Dude to start riot, trade to Bookworm for hint |
| Wrench | Prison Room | NO (consumed on trade) | Trade to Bookworm for hint about fence/yard |
| Fork | Prison Room | NO (consumed on trade) | Trade to Billy in exchange for Fortune Cookie |
| Red Painted Brick | Ventilation System | NO (must drop to climb fence) | Trade to Bookworm for hint about Warden's Office; starts 2-minute timer on pickup; Player must use drop brick to remove it from inventory before the fence climb is permitted.|
| Fortune Cookie | Received from Billy | YES | Contains the Time Machine unlock code |
 
---
 
## Map — Walking Connections (Normal Paths)
 
| From | Can Walk To |
|---|---|
| Prison Room | Cafeteria |
| Cafeteria | Prison Room, Letter Room, Yard |
| Letter Room | Cafeteria, Library |
| Yard | Cafeteria, Library |
| Library | Yard, Letter Room |
| Warden's Office | NOT accessible by walking — vent only |
 
---
 
## Map — Ventilation Connections
 
The vent system is a separate travel layer. Rules:
 
| Location | Can Enter Vent? | Can Exit Vent Into Room? | Special Rule |
|---|---|---|---|
| Prison Room | YES (after unscrewing with Coin) | YES | Vent must be unlocked first (`ventUnlocked = true`) |
| Cafeteria | NO — caught by Sora AI | NO — caught by Sora AI | Any vent entry or exit here = GAME OVER |
| Letter Room | YES | YES | Freely usable in both directions |
| Library | NO | YES (exit only) | Player can exit vent into Library but CANNOT re-enter vent from Library |
| Warden's Office | YES | YES | Safe only if `riotStarted = true`; otherwise GAME OVER |
| Yard | NO | NO | Yard has no vent access at all |
 
### Vent Navigation Inside the Duct
 
The vent duct connects: Prison Room → Cafeteria → Letter Room → Library / Warden's Office
 
When inside the vent, `paths` shows the room directly below the player's current duct position.
 
---
 
## Room Specifications
 
### Prison Room
- **Starting room** — player begins here.
- **Items present:** Coin, Wrench, Fork
- The player must pick up coin then use coin on vent to unlock. ventUnlocked becomes true. Coin is retained
- **First item pickup:** Display the `inventory` command hint to the player.
---
 
### Ventilation System
- **Items present:** Red Painted Brick
- **Picking up the Brick:** Sets `hasBrick = true` and starts the 2-minute background timer.
- **Travel:** Player navigates through the duct. Exiting into Cafeteria from any direction = GAME OVER (Sora AI).
---
 
### Cafeteria
- **NPC:** Billy
- **Walking access:** From Prison Room or Letter Room or Yard only.
- **Vent access:** NONE — any attempt to enter or exit via vent triggers GAME OVER.
#### Billy — Trade Logic
- **Trigger:** `trade fork`
- **Requirement:** `hasFork = true`
- **Result:** Fork removed from inventory; Fortune Cookie added. `hasFortuneCookie = true`.
- **Fortune Cookie contents:** Time Machine unlock code (used in Warden's Office).
- **If no fork:** Billy refuses and tells the player he wants a fork.
---
 
### Letter Room
- **NPC:** Jilly
- **Walking access:** From Cafeteria or Library.
- **Vent access:** Full (enter and exit freely).
#### Jilly — Timecode Dialogue
When the player talks to Jilly (no trade required), she provides all three time codes:
 
| Destination | Code |
|---|---|
| Far Future | 01382 |
| Day Your Crush Rejected You | 18572 |
| High School Freshman Orientation | 90928 |
 
Jilly simply tells the player these codes in dialogue. No item required.
 
---
 
### Library
- **NPC:** Bookworm
- **Walking access:** From Letter Room or Yard.
- **Vent access:** Exit from vent INTO Library allowed. Cannot re-enter vent from Library.
#### Bookworm — Hint Trade Table
 
| Item Traded | Item Consumed? | Hint Given |
|---|---|---|
| Wrench | YES | "There's a fence in the Yard. If the Warden were distracted... you might be able to climb it." |
| Coin | YES | "Stay away from the Scrawny Guy. He's dangerous." |
| Red Painted Brick | YES | "The Warden never leaves his office. But maybe something could pull him away." |
 
- Each trade is independent; player can make any or all three trades.
- If the player tries to trade an item they don't have, Bookworm declines.
---
 
### Yard
- **Walking access:** From Cafeteria or Library.
- **Vent access:** NONE.
- **Interactable elements:** Buff Dude (NPC), Scrawny Guy (NPC), Fence (object)
#### Buff Dude
- **Trigger:** `talk to buff dude` or `interact buff dude`
- **Requirement:** `hasCoin = true`
- **Result:** Coin consumed. `riotStarted = true`. Buff Dude tells the player the Warden has left his office.
- **If no coin:** Buff Dude asks for a coin first.
#### Scrawny Guy
- **Trigger:** Any attempt to `talk to`, `interact`, or approach Scrawny Guy.
- **Result:** Immediate GAME OVER. Print death message. Terminate and restart.
#### Fence
- **Requirements to climb:** `hasBrick = false` AND `riotStarted = true`
- **If brick in inventory:** Print message that the brick is too heavy to climb with.
- **If no riot:** Print message that the Warden is watching.
- **If both conditions met:** Player climbs fence → BAD ENDING (Ending 3). GAME OVER. Restart.
---
 
### Warden's Office
- **Walking access:** NONE — only accessible via vent from Letter Room duct position.
- **Vent entry rule:**
 - `riotStarted = false` → GAME OVER: "Caught by Warden (Claude Opus 9.2)". Restart.
 - `riotStarted = true` → Entry allowed.
#### Time Machine
- **Trigger:** `use time machine` or `interact time machine` (once inside the office)
- **Step 1:** Prompt player to enter the Time Machine unlock code (from Fortune Cookie). Correct code is stored in the Fortune Cookie item. If wrong: print error, let player retry.
- **Step 2:** Prompt player to select a destination by entering a time code (from Jilly). Accept any of the three valid codes.
 - `01382` → BAD ENDING (Far Future). Restart.
 - `18572` → BAD ENDING (Rejected by crush). Restart.
 - `90928` → TRUE ENDING (Freshman Orientation). WIN.
- **If player does not have Fortune Cookie:** Time Machine prompts for code but player cannot proceed. Print hint that they are missing something.
---
 
## Endings
 
| # | Name | Trigger | Outcome |
|---|---|---|---|
| 1 | Death by Scrawny Guy | Talk to Scrawny Guy | Immediate death. Game over. Restart. |
| 2 | Caught by Warden | Enter Warden's Office via vent with no riot active | Game over. "Caught by Warden (Claude Opus 9.2)". Restart. |
| 3 | Prison Escape (Bad) | Climb fence (no brick, riot active) | Nano Banana patrols catch player. Banana peel is thrown, player slips and dies. Restart. |
| 4 | Sora AI Caught | Enter or exit Cafeteria via vent | "Sora AI petrifies you in the blockchain." Game over. Restart. |
| 5 | True Ending (WIN) | Enter Time Machine with correct codes, select code 90928 | Player travels back to Freshman Orientation and stops themselves from taking AP CSA, preventing the AI uprising. Game ends with victory message. |
 
---
 
## Win Condition (All must be true)
 
1. `hasFortuneCookie = true` (received from Billy)
2. Player has spoken to Jilly and knows time code `90928`
3. `riotStarted = true` (Buff Dude started the riot)
4. Player entered Warden's Office safely via vent
5. Player used Time Machine with correct unlock code AND selected `90928`
---
 
## Game Over / Restart Behavior
 
Any time `gameOver = true` is set:
1. Print the appropriate death/failure message.
2. Terminate all active threads (including timer if running).
3. Restart the game from the beginning (re-run `main()`).
---
 
## Sample Gameplay Flow (Reference Only)
 
```
1.  Start in Prison Room
2.  pick up coin
3.  pick up fork
4.  pick up wrench
5.  use coin on vent         → ventUnlocked = true
6.  vent                     → enter ventilation system
7.  pick up brick            → hasBrick = true, 2-min timer starts
8.  navigate duct to Letter Room position, exit vent
9.  walk cafeteria
10. talk to billy / trade fork → hasFortuneCookie = true
11. walk letter room
12. talk to jilly            → learn codes 01382, 18572, 90928
13. walk library             (optional: trade wrench/coin/brick to Bookworm for hints)
14. walk yard
15. talk to buff dude        → riotStarted = true (coin consumed)
16. walk library
17. walk letter room
18. vent                     → enter duct from Letter Room
19. navigate duct to Warden's Office, exit vent → safe (riot active)
20. use time machine         → enter unlock code from Fortune Cookie
21. enter time code 90928    → TRUE ENDING
```

