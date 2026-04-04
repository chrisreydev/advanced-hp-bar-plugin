# Advanced HP Bar
A customizable HP bar overlay for your local player, replacing the default HP bar with a segmented design that gives you more control over how your health and prayer are displayed.



<img width="350" src="https://github.com/user-attachments/assets/17a46bfc-0ccf-4b27-aa2f-5ef5cf2d56ed" />
<img width="350" src="https://github.com/user-attachments/assets/eae63fbc-844f-4f5e-9870-1f201421f55e" />
<img width="290" src="https://github.com/user-attachments/assets/3c75fd20-142e-45d2-ae66-9b53570de6a5" />
<img width="231" height="711" alt="Screenshot 2026-04-04 at 2 09 52 PM" src="https://github.com/user-attachments/assets/e19b367c-61a1-4839-b32a-3661d308f4d3" />


---

## Features
- **Segmented HP bar** — HP is split into boxes, each representing a configurable number of HP points (e.g. 10 HP per box)
- **Overheal display** — HP above your maximum is shown as additional boxes in a distinct color, scaled proportionally alongside your normal HP boxes
- **Low HP warning color** — the bar switches to a separate color when HP drops below a configurable threshold
- **Damaged HP color** — the empty portion of each box is colored to indicate missing HP
- **Food heal preview** — hovering a food item in your inventory highlights the portion of the bar that would be restored, starting exactly where your current HP ends
- **Optional prayer bar** — a thin bar below the HP bar showing current prayer points as a proportion of max prayer
- **Prayer tick swipe line** — an optional line that sweeps left to right across the prayer bar once per game tick, giving a visual reference for tick timing
- **Low prayer warning color** — the prayer bar switches to a separate color when prayer drops below a configurable threshold (disabled by default)
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
| Low Prayer Threshold | Prayer value at which the bar switches to the low prayer color. Set to `0` to disable | `0` |
| Low Prayer Color | Color used when prayer drops below the threshold | Yellow |
| Prayer Restore Preview Color | Color of the restore preview shown when hovering a prayer item in your inventory | Light Blue |
| Prayer Bar Height | Height of the prayer bar in pixels | `3` |
| Prayer Flick Helper | Display a swipe line on the prayer bar that travels left to right once per game tick | `false` |
| Prayer Flick Color | Color of the prayer flick swipe line | Black |

---

## How it works
The HP bar is divided into segments, each representing a fixed number of HP (configured via **HP Per Box**).

When your HP exceeds your maximum (e.g. via Saradomin brews or other boosting mechanics), overheal is shown as additional boxes appended after your normal HP boxes, rendered in the **Overheal Color**. All boxes — normal and overheal — scale proportionally so the bar always fits within the configured width.

Setting **HP Per Box** to `0` switches to a single full-width bar. If overhealed in this mode, the bar splits into two flush segments: the left portion represents your normal HP (scaled to your max), and the right portion represents the overheal amount.

When hovering a food item in your inventory, a colored preview is drawn on the HP bar showing exactly how much HP would be restored — starting precisely where your current HP ends and capped at your maximum HP. The same applies to the prayer bar when hovering items that restore prayer points.

The prayer bar renders as a single thin bar directly below the HP bar. When **Low Prayer Threshold** is set to a value above `0`, the prayer bar will switch to the **Low Prayer Color** once your prayer points drop to or below that value, mirroring the low HP warning behavior.

When **Prayer Flick Helper** is enabled, a swipe line travels across the prayer bar from left to right once every game tick (600ms), resetting at the start of each tick. This gives a continuous visual reference for tick timing, useful for prayer flicking or other tick-based mechanics. The line color is configurable via **Prayer Flick Color**.
