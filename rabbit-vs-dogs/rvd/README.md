# Rabbit vs 3 Dogs
A responsive JavaFX GUI for a node-based "Rabbit vs 3 Dogs" pursuit
strategy game (Morabaraba-inspired), built with **JDK 21** and **JavaFX
21 (OpenJFX SDK)**, matching the approved design mockup's layout and
colour palette.

# Project structure
```
src/main/java/com/limkokwing/rabbitvsdogs/
  app/       Main.java, RabbitVsDogsApp.java      – entry point & screen navigation
  model/     BoardGraph, GameState, Difficulty...  – pure data, no JavaFX
  engine/    GameEngine.java                       – rules, win conditions, Rabbit AI
  ui/        WelcomeScreen, GameScreen, ...         – the six screens
  util/      Assets, Theme, SoundManager, ...       – shared helpers

src/main/resources/com/limkokwing/rabbitvsdogs/
  images/    background.png, logo.png, settingdog.png, sittingrabbit.png
  videos/    background.mp4
  audio/     background.mp3
```

# Screens

1. **Welcome / Main Menu** — `background.png` + `logo.png`, Play Game,
   Instructions, Settings, Exit/Quit.
2. **Game Screen** — the 11-node board (0–10), Dogs-pressure / Rabbit-health
   bars, level + countdown timer, Pause button.
3. **Instructions** — description, objective, controls, rules (over
   `background.mp4`).
4. **Settings** — Sound Effects, Background Music, Difficulty, Volume
   (over `background.mp4`).
5. **Pause overlay** — Resume / Restart / Main Menu / Settings.
6. **Game Over** — win/lose message, final time & move count, Play Again,
   Main Menu.

# How the game works

- The board is the exact 11-node / 22-edge graph from the approved design
  (`BoardGraph.java`). The Rabbit starts at node 0; the three Dogs start on
  three distinct random nodes from 1–10.
- **You control the three Dogs**: click a dog to select it (its legal
  moves light up in blue), then click a highlighted node to move there.
- **The Rabbit moves itself**, using a small heuristic in
  `GameEngine.chooseRabbitMove()` that favours nodes with more open
  neighbours and more distance from the Dogs.
- **Dogs win** by trapping the Rabbit (every node next to it occupied).
- **Rabbit wins** if the countdown reaches `00:00` while still free, or if
  all three Dogs run out of legal moves.
- Difficulty (Easy 10 min / Medium 5 min / Hard 2 min / Extreme 1 min)
  sets how long the Rabbit must survive, and is chosen from the Welcome
  screen's Play button or from Settings.

# Audio & video, exactly as specified

- **One single audio track**, `background.mp3`, loops continuously across
  every screen and is only ever stopped when Exit/Quit is pressed
  (`SoundManager.java`). No `.wav` sound-effect files are used anywhere.
- The **Welcome screen** is the only screen that uses a static image
  background (`background.png` + `logo.png`).
- **Every other screen** (Game, Instructions, Settings, Pause, Game Over)
  uses the looping, muted `background.mp4` video (`VideoBackground.java`).

# About the bundled media assets

This repository ships **original, generated placeholder assets** so the
project runs out of the box:

- `logo.png` and `background.png` were generated to match the exact
  colours and layout of the approved design mockup.
- `settingdog.png` / `sittingrabbit.png` are simple original flat-style
  silhouettes — the stock photographs used as inspiration in the design
  document are copyrighted and were **not** reused here.
- `background.mp4` is a generated looping gradient animation, and
  `background.mp3` is a generated ambient placeholder tone loop.

**Swap these four files for your own artwork/video/music** before final
submission — just keep the same file names
(`background.png`, `logo.png`, `settingdog.png`, `sittingrabbit.png`,
`background.mp4`, `background.mp3`) and drop them into
`src/main/resources/com/limkokwing/rabbitvsdogs/{images,videos,audio}/`.

# Responsive design

The board, header, side panels and every screen's background scale with
the window using percentage/fraction-based layout (`BoardGraph` stores
node positions as 0–1 fractions, redrawn on every resize) rather than
fixed pixel coordinates, so the interface adapts across desktop, tablet
and mobile-sized windows.
