# Advanced HP Bar

A customizable HP bar overlay for your local player, replacing the default combat bar with a segmented design that gives you more control over how your health — and optionally your prayer — is displayed.


<img width="200" height="200" alt="Screenshot 2026-04-01 at 3 45 17 PM" src="https://github.com/user-attachments/assets/d35a5de9-405f-48d7-bbf3-b0e8d56ef910" />
<img width="200" height="200" alt="Screenshot 2026-04-01 at 3 48 16 PM" src="https://github.com/user-attachments/assets/1f21654e-1117-4d6e-a6a7-9090a0b8703d" />


---

## Features

- **Segmented HP bar** — HP is split into boxes, each representing a configurable number of HP points (e.g. 10 HP per box)
- **Fixed bar width** — the bar always renders at a fixed pixel width regardless of how many boxes there are
- **Low HP warning color** — the bar switches to a separate color when HP drops below a configurable threshold
- **Damaged HP color** — the empty portion of each box is colored to indicate missing HP
- **Optional prayer bar** — a thin bar below the HP bar showing current prayer points as a proportion of max prayer
- **Always-on mode** — optionally show the bar at all times, not just during combat
- **Fully configurable colors** — every color in the overlay is customizable
- **Position offsets** — adjust the bar's horizontal position, vertical position, and world-space Z offset to align it exactly where you want

---

## Configuration

Settings are grouped into three sections in the config panel:

### General
| Setting | Description | Default |
|---|---|---|
| Always Show | Show the bar at all times, not just during combat | `true` |
| Bar Width | Total fixed width of the bar in pixels | `60` |
| Z Offset | Vertical world-space offset for alignment above the player model | `23` |
| Vertical Offset | How high above the character to render the bar | `-3` |
| Horizontal Offset | Horizontal offset relative to the player's centre | `-40` |

### HP Bar
| Setting | Description | Default |
|---|---|---|
| HP Per Box | How many HP points each segment represents | `10` |
| HP Color | Color of the filled (current HP) portion | Green |
| HP Damaged Color | Color of the empty (missing HP) portion | Red |
| HP Background Color | Background color drawn behind all boxes | Black |
| Low HP Threshold | HP value at which the bar switches to the low HP color. Set to `0` to disable | `0` |
| Low HP Color | Color used when HP drops below the threshold | Yellow |

### Prayer Bar
| Setting | Description | Default |
|---|---|---|
| Show Prayer Bar | Display a thin prayer bar below the HP bar | `false` |
| Prayer Color | Color of the prayer points fill | Cyan |
| Prayer Background Color | Background color of the prayer bar | Black |

---

## How it works

The HP bar is divided into segments, each representing a fixed number of HP (configured via **HP Per Box**). The total bar width is fixed in pixels and the segments scale to fill it evenly. The last segment is proportionally smaller if your max HP is not evenly divisible by the HP per box value.

The prayer bar, when enabled, renders as a single thin bar directly below the HP bar. It fills proportionally from left to right based on your current prayer points relative to your maximum.
