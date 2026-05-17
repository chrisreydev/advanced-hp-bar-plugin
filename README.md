# Advanced HP Bar
A customizable HP bar overlay for your local player, replacing the default HP bar with a segmented design. You can also modify NPC HP bars.

<img width="250" src="https://github.com/user-attachments/assets/17a46bfc-0ccf-4b27-aa2f-5ef5cf2d56ed" />
<img width="250" src="https://github.com/user-attachments/assets/eae63fbc-844f-4f5e-9870-1f201421f55e" />
<img width="210" src="https://github.com/user-attachments/assets/3c75fd20-142e-45d2-ae66-9b53570de6a5" />

<img width="224" height="175" alt="Screenshot 2026-04-04 at 2 16 01 PM" src="https://github.com/user-attachments/assets/c500b53e-9054-4114-87b0-b8f8f71c7710" />
<img width="227" height="249" alt="Screenshot 2026-04-04 at 2 16 16 PM" src="https://github.com/user-attachments/assets/48d86c90-8a92-4280-b890-68562e6462f5" />
<img width="229" height="281" alt="Screenshot 2026-04-04 at 2 16 27 PM" src="https://github.com/user-attachments/assets/da5acfbb-f4aa-4328-ad61-7e4b31b8d651" />

---

## Features
- **Segmented HP bar** — HP is split into boxes, each worth a set number of HP points.
- **Overheal display** — HP above your maximum is shown as extra boxes in their own color.
- **Low HP warning** — the bar changes color when HP drops below a set threshold.
- **Food heal preview** — hovering food in your inventory shows how much HP it would restore.
- **Prayer bar** — an optional thin bar below the HP bar showing current prayer.
- **Prayer flick line** — an optional line that sweeps across the prayer bar once per game tick.
- **Low prayer warning** — the prayer bar changes color below a set threshold.
- **Prayer restore preview** — hovering a prayer-restoring item shows how much it would restore.
- **NPC HP bars** — redraws health bars above NPCs, with configurable size, position, and colors.
- **Always-on mode** — optionally show your bar even outside combat.
- **Configurable colors and position** — every color, plus the bar's size and offsets.

---

## Configuration
Settings are grouped into four sections.

### General
| Setting | Description | Default |
|---|---|---|
| Always Show | Show your bar at all times, not just during combat | `false` |
| Bar Width | Total width of your bar in pixels | `60` |
| Z Offset | World-space vertical offset for alignment above your model | `0` |
| Vertical Offset | How high above your character to render the bar | `0` |
| Horizontal Offset | Horizontal offset from your character's centre | `0` |

### HP Bar
| Setting | Description | Default |
|---|---|---|
| HP Per Box | HP each box represents. Set to `0` for a single full-width bar | `10` |
| HP Color | Color of the filled (current HP) portion | Green |
| HP Damaged Color | Color of the empty (missing HP) portion | Red |
| Low HP Threshold | HP value at which the bar switches to the low HP color. `0` disables | `5` |
| Low HP Color | Color used below the low HP threshold | Yellow |
| Overheal Color | Color used for HP above your maximum | White |
| Food Heal Preview Color | Color of the food heal preview | Pink |
| HP Background Color | Background color behind the boxes | Black |
| HP Bar Height | Height of the HP bar in pixels | `6` |

### Prayer Bar
| Setting | Description | Default |
|---|---|---|
| Show Prayer Bar | Display a thin prayer bar below the HP bar | `true` |
| Prayer Color | Color of the prayer fill | Cyan |
| Prayer Restore Preview Color | Color of the prayer restore preview | Light Blue |
| Low Prayer Threshold | Prayer value at which the bar switches to the low prayer color. `0` disables | `0` |
| Low Prayer Color | Color used below the low prayer threshold | Yellow |
| Prayer Background Color | Background color of the prayer bar | Black |
| Prayer Bar Height | Height of the prayer bar in pixels | `3` |
| Prayer Flick Helper | Show a swipe line that crosses the bar once per game tick | `false` |
| Prayer Flick Color | Color of the prayer flick line | Orange |

### NPC HP Bar
| Setting | Description | Default |
|---|---|---|
| NPC Bar Width | Width of the NPC health bar in pixels | `30` |
| NPC Bar Height | Height of the NPC health bar in pixels | `5` |
| NPC Z Offset | World-space vertical offset added above the NPC's model | `0` |
| NPC Horizontal Offset | Horizontal pixel offset of the NPC bar | `0` |
| NPC Vertical Offset | Vertical pixel offset of the NPC bar | `0` |
| NPC HP Color | Color of the NPC health fill | Green |
| NPC HP Damaged Color | Color of the missing-HP portion | Red |

---

## How it works
The HP bar is divided into segments, each worth a fixed amount of HP (set by **HP Per Box**).

If your HP goes above your maximum, the extra is shown as additional boxes in the **Overheal Color**. All boxes scale to fit the configured width. Setting **HP Per Box** to `0` makes a single full-width bar; if overhealed in this mode it splits into a normal portion and an overheal portion.

Hovering food in your inventory previews how much HP it would restore, starting where your current HP ends. Hovering a prayer-restoring item does the same on the prayer bar.

The prayer bar sits directly below the HP bar. With **Prayer Flick Helper** on, a line sweeps across it once every game tick (600ms) as a timing reference.

For NPCs, the plugin hides the game's default health bars and redraws its own using the **NPC HP Bar** settings. The redrawn NPC bar uses a single health/damage color.