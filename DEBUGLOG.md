# Debug Log
<!-- Write your team notes here to keep track of how different iterations of your spec changed the output. -->
---
## Version 2 — Spec Update Notes
### 1. JavaDoc Requirement Added
- **New in v2:** All functions and classes must have JavaDoc comments with proper `/**` opening and `*/` closing brackets.
- This was not present in v1 at all.
---
### 2. Game Premise — Player-Facing Display Added
- **New in v2:** A specific block of narrative text was added to be displayed to the player at the start of the game. V1 had the premise written as developer context only; v2 explicitly marks it as something the player must see, written in second person ("you gave AI a human body...").
---
### 3. Command Reference — Player Intro Added
- **New in v2:** A list of commands must now be shown to the player at the start of the game. V1 had an example commands block buried in the Core Gameplay Mechanics section as developer reference. V2 formalizes these as player-facing onboarding text with the following commands explicitly listed:
- `Look` — see surroundings
- `Inventory` — view collected items
- `Vent` — toggle entering/exiting the vent
- `Paths` — show available paths (or vent drop points when inside vent)
- `Talk to` / `Interact` — interact with an NPC
- `Trade ___` — trade an item to an NPC
- `Commands` — display all commands
- `Enter/Walk __` — enter a room
- `Pick up ___` — pick up an item
---
### 4. No Structural / Mechanical Changes
- All rooms, NPCs, items, endings, game state flags, class structure, and win conditions remain **identical** between v1 and v2.
- The map layout, vent restrictions, NPC behaviors, item purposes, hint system, and trade requirements are unchanged.
---
---
## Version 3 — Spec Update Notes
### 1. ~~Possible Removal of `Pick up ___` Command~~ — RESOLVED
- **NOT intentional.** `Pick up ___` is retained in the player-facing command list.
- V3 is functionally identical to V2. No net changes.
---
## Version 4 — (No Mechanical Changes)
V4 is a language and structure cleanup pass to make the spec unambiguous for code generation. No game logic, mechanics, rooms, items, NPCs, or endings were changed.
### 1. Game State Flags — Expanded
- Added `ventUnlocked` flag (was implied in v1-v3 but never explicitly listed).
- Added `hasWrench` flag (was missing from the flags list despite Wrench being a key item).
- All flags now include inline comments explaining when they become `true`.
### 2. Items Table — Clarified Retention Behavior
- Added "Retained After Use?" column to the items table.
- Coin: explicitly noted as retained after unscrewing the vent.
- Fork, Wrench, Brick: explicitly noted as consumed on trade.
### 3. Map — Split Into Two Explicit Tables
- Walking connections and vent connections are now two separate tables instead of prose paragraphs.
- Vent table explicitly states enter/exit permissions per room and the special rule for each.
### 4. NPC Interactions — Explicit If/Else Logic
- Each NPC trade now specifies: trigger command, requirement flag, result if requirement met, result if requirement NOT met.
- Removes ambiguity about what happens when the player tries to interact without the right item.
### 5. Time Machine — Two-Step Input Flow Specified
- Step 1 (Fortune Cookie unlock code) and Step 2 (destination time code) are now separate explicit steps.
- Wrong-code behavior (retry vs. game over) is now defined.
- "Missing Fortune Cookie" case is now handled explicitly.
### 6. Endings Table — Added Sora AI as Ending 4
- Sora AI catch (vent into/out of Cafeteria) was described in room specs but never listed as a named ending.
- Added as Ending 4. True Ending renumbered to Ending 5.
### 7. Timer — Clarified as Background Thread
- Explicitly stated the 4-minute timer runs as a background thread.
- Added that all threads must be terminated on game over/restart.
### 8. Game Over Behavior — Explicit Restart Sequence
- Added a dedicated section specifying what happens on any `gameOver = true`: print message → terminate threads → restart from `main()`.


## Version 5 — Command & Input Handling Additions
### 1. Case-Insensitivity — Specified
- **New in v5:** All player input is now explicitly case-insensitive.
- V1–V4 never addressed this; behavior was undefined.
### 2. Unrecognized Command Behavior — Specified
- **New in v5:** Invalid input prints: "I don't understand that command. Type 'commands' to see the full list."
- V1–V4 had no defined behavior for invalid input.
### 3. `drop [item]` Command — Added
- **New in v5:** Added to command list and Yard/fence mechanic.
- V1–V4 referenced dropping the brick as a requirement but never defined the command.
### 4. `use [item] on [target]` Command — Formalized
- **New in v5:** Added to the player-facing command list.
- Was informally referenced in Prison Room section in v1–v4 but never listed as an official command.
---


---
### Summary Table
| Area | V1 | V2 | V3 | V4 | V5 |
|---|---|---|---|---|---|
| JavaDoc comments | Not mentioned | Required on all functions and classes | Unchanged | Unchanged | Unchanged |
| Opening premise | Developer-facing only | Must be displayed to player at game start | Unchanged | Unchanged | Unchanged |
| Command list | Developer reference block only | Must be shown to player at game start | Unchanged | Unchanged | Unchanged |
| `Pick up ___` command | Not listed | Listed in player intro | Missing (unintentional) | Restored | Unchanged |
| Game state flags | 7 flags listed | Unchanged | Unchanged | 2 flags added: `ventUnlocked`, `hasWrench`; all flags annotated | Unchanged |
| Items table | Purpose only | Unchanged | Unchanged | Added "Retained After Use?" column; consumption behavior explicit | Unchanged |
| Map layout | Prose description | Unchanged | Unchanged | Split into two explicit tables (walking vs. vent) | Unchanged |
| NPC interactions | Prose description | Unchanged | Unchanged | Explicit if/else logic per NPC (trigger, requirement, success, failure) | Unchanged |
| Time Machine flow | Described loosely | Unchanged | Unchanged | Two-step input flow specified; wrong-code behavior defined | Unchanged |
| Endings | 4 endings listed | Unchanged | Unchanged | Sora AI catch added as Ending 4; True Ending renumbered to 5 | Unchanged |
| Timer | Mentioned as mechanic | Unchanged | Unchanged | Explicitly defined as background thread; termination on restart noted | Unchanged |
| Game Over behavior | Implied | Unchanged | Unchanged | Explicit restart sequence defined | Unchanged |
| Rooms / Core Mechanics | Defined | Unchanged | Unchanged | Unchanged | Unchanged |
| NPCs / Items / Classes | Defined | Unchanged | Unchanged | Unchanged | Unchanged |
| Case-insensitive input | Not mentioned | Unchanged | Unchanged | Unchanged | Now explicitly required |
| Unrecognized command response | Not mentioned | Unchanged | Unchanged | Unchanged | Defined with exact output string |
| `drop [item]` command | Not mentioned | Unchanged | Unchanged | Unchanged | Added to command list and Yard/fence mechanic |
| `use [item] on [target]` command | Informally referenced | Unchanged | Unchanged | Unchanged | Formalized in command list |










