# Advanced HP Bar
A customizable HP bar overlay for your local player, replacing the default HP bar with a segmented design that gives you more control over how your health and your prayer are displayed.



<img width="250" height="250" alt="advanced-hp-bar-1" src="https://github.com/user-attachments/assets/b2cd855f-6499-4070-bc39-2b800618a8cc" />
<img width="250" height="250" alt="advanced-hp-bar-2" src="https://github.com/user-attachments/assets/2a325d58-7865-40b7-afe2-3ce59a6197ae" />
<img width="250" height="250" alt="Screenshot 2026-04-03 at 8 38 32 PM" src="https://github.com/user-attachments/assets/7cbc6e74-8351-4360-b0ce-13e993a6dc46" />
<img width="238" height="542" alt="Screenshot 2026-04-03 at 9 37 26 PM" src="https://github.com/user-attachments/assets/6e18f3c0-6427-47cf-8c52-452ac892c51d" />


---

## Features
- **Segmented HP bar** — HP is split into boxes, each representing a configurable number of HP points (e.g. 10 HP per box)
- **Overheal display** — HP above your maximum is shown as additional boxes in a distinct color, scaled proportionally alongside your normal HP boxes
- **Low HP warning color** — the bar switches to a separate color when HP drops below a configurable threshold
- **Damaged HP color** — the empty portion of each box is colored to indicate missing HP
- **Optional prayer bar** — a thin bar below the HP bar showing current prayer points as a proportion of max prayer
- **Always-on mode** — optionally show the bar at all times, even outside of combat
- **Fully configurable colors** — every color in the overlay is customizable
- **Fixed bar width** — the bar always renders at a configurable fixed pixel width regardless of how many boxes there are
- **Position offsets** — adjust the bar's horizontal position, vertical position, and world-space Z offset

---

## Configuration
Settings are grouped into three sections in the config panel:

### General
| Setting | Description | Default |
|---|---|---|
| Always Show | Show the bar at all times, not just during combat | `false` |
| Bar Width | Total fixed width of the bar in pixels | `60` |
| Z Offset | Vertical world-space offset for alignment above the player model | `23` |
| Vertical Offset | How high above the character to render the bar | `-3` |
| Horizontal Offset | Horizontal offset relative to the player's centre | `-28` |

### HP Bar
| Setting | Description | Default |
|---|---|---|
| HP Per Box | How many HP points each segment represents. Set to `0` for a single full-width bar | `10` |
| HP Color | Color of the filled (current HP) portion | Green |
| HP Damaged Color | Color of the empty (missing HP) portion | Red |
| HP Background Color | Background color drawn behind all boxes | Black |
| Low HP Threshold | HP value at which the bar switches to the low HP color. Set to `0` to disable | `5` |
| Low HP Color | Color used when HP drops below the threshold | Yellow |
| Overheal Color | Color used to display HP above your maximum | White |

### Prayer Bar
| Setting | Description | Default |
|---|---|---|
| Show Prayer Bar | Display a thin prayer bar below the HP bar | `true` |
| Prayer Color | Color of the prayer points fill | Cyan |
| Prayer Background Color | Background color of the prayer bar | Black |

---

## How it works
The HP bar is divided into segments, each representing a fixed number of HP (configured via **HP Per Box**).

When your HP exceeds your maximum (e.g. via Saradomin brews or other boosting mechanics), overheal is shown as additional boxes appended after your normal HP boxes, rendered in the **Overheal Color**. All boxes — normal and overheal — scale proportionally so the bar always fits within the configured width.

Setting **HP Per Box** to `0` switches to a single full-width bar. If overhealed in this mode, the bar splits into two flush segments: the left portion represents your normal HP (scaled to your max), and the right portion represents the overheal amount.

The prayer bar renders as a single thin bar directly below the HP bar.
