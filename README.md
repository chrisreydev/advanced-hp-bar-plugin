# Advanced HP Bar
A customizable HP bar overlay for your local player, replacing the default HP bar with a segmented design that gives you more control over how your health and prayer are displayed.


<img width="600" height="600" src="https://github.com/user-attachments/assets/a9cbea2f-337a-4a82-be41-d76af31e5f5f" />
<img width="250" height="573" src="https://github.com/user-attachments/assets/e81870e3-c0fb-4ebf-8225-d84a94be5f87" />


<img width="280" height="280" src="https://github.com/user-attachments/assets/b2cd855f-6499-4070-bc39-2b800618a8cc" />
<img width="280" height="280" src="https://github.com/user-attachments/assets/2a325d58-7865-40b7-afe2-3ce59a6197ae" />
<img width="280" height="280" src="https://github.com/user-attachments/assets/7cbc6e74-8351-4360-b0ce-13e993a6dc46" />

---

## Features
- **Segmented HP bar** — HP is split into boxes, each representing a configurable number of HP points (e.g. 10 HP per box)
- **Overheal display** — HP above your maximum is shown as additional boxes in a distinct color, scaled proportionally alongside your normal HP boxes
- **Low HP warning color** — the bar switches to a separate color when HP drops below a configurable threshold
- **Damaged HP color** — the empty portion of each box is colored to indicate missing HP
- **Food heal preview** — hovering a food item in your inventory highlights the portion of the bar that would be restored, starting exactly where your current HP ends
- **Optional prayer bar** — a thin bar below the HP bar showing current prayer points as a proportion of max prayer
- **Prayer restore preview** — hovering a prayer-restoring item in your inventory highlights the portion of the prayer bar that would be restored
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
| Food Heal Preview Color | Color of the heal preview shown when hovering food in your inventory | Pink |

### Prayer Bar
| Setting | Description | Default |
|---|---|---|
| Show Prayer Bar | Display a thin prayer bar below the HP bar | `true` |
| Prayer Color | Color of the prayer points fill | Cyan |
| Prayer Background Color | Background color of the prayer bar | Black |
| Prayer Restore Preview Color | Color of the restore preview shown when hovering a prayer item in your inventory | Light Blue |

---

## How it works
The HP bar is divided into segments, each representing a fixed number of HP (configured via **HP Per Box**).

When your HP exceeds your maximum (e.g. via Saradomin brews or other boosting mechanics), overheal is shown as additional boxes appended after your normal HP boxes, rendered in the **Overheal Color**. All boxes — normal and overheal — scale proportionally so the bar always fits within the configured width.

Setting **HP Per Box** to `0` switches to a single full-width bar. If overhealed in this mode, the bar splits into two flush segments: the left portion represents your normal HP (scaled to your max), and the right portion represents the overheal amount.

When hovering a food item in your inventory, a colored preview is drawn on the HP bar showing exactly how much HP would be restored — starting precisely where your current HP ends and capped at your maximum HP. The same applies to the prayer bar when hovering items that restore prayer points.

The prayer bar renders as a single thin bar directly below the HP bar.
