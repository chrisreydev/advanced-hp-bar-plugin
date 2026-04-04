package com.advancedhpbar;

import net.runelite.client.config.*;
import java.awt.*;

@ConfigGroup("advancedhpbar")
public interface AdvancedHpBarConfig extends Config
{
    // -------------------------------------------------------------------------
    // Sections
    // -------------------------------------------------------------------------

    @ConfigSection(
            name = "General",
            description = "General bar settings",
            position = 0
    )
    String generalSection = "general";

    @ConfigSection(
            name = "HP Bar",
            description = "HP bar appearance settings",
            position = 1
    )
    String hpSection = "hp";

    @ConfigSection(
            name = "Prayer Bar",
            description = "Prayer bar appearance settings",
            position = 2
    )
    String prayerSection = "prayer";

    // -------------------------------------------------------------------------
    // General
    // -------------------------------------------------------------------------

    @ConfigItem(
            keyName = "alwaysOn",
            name = "Always Show",
            description = "Always display the bar, or only during combat",
            section = generalSection,
            position = 0
    )
    default boolean alwaysOn()
    {
        return false;
    }

    @ConfigItem(
            keyName = "barWidth",
            name = "Bar Width",
            description = "Total fixed width of the bar in pixels",
            section = generalSection,
            position = 1
    )
    @Range(min = 10, max = 500)
    default int barWidth()
    {
        return 60;
    }

    @ConfigItem(
            keyName = "zOffset",
            name = "Z Offset",
            description = "Z offset of the bar for vertical world-space alignment",
            section = generalSection,
            position = 2
    )
    @Range(min = -50, max = 50)
    default int zOffset()
    {
        return 23;
    }

    @ConfigItem(
            keyName = "barHeightOffset",
            name = "Vertical Offset",
            description = "How high above the character to render the bar",
            section = generalSection,
            position = 3
    )
    @Range(min = -500, max = 500)
    default int barHeightOffset()
    {
        return -3;
    }

    @ConfigItem(
            keyName = "barXOffset",
            name = "Horizontal Offset",
            description = "Horizontal offset of the bar relative to the player's centre",
            section = generalSection,
            position = 4
    )
    @Range(min = -500, max = 500)
    default int barXOffset()
    {
        return -28;
    }

    // -------------------------------------------------------------------------
    // HP Bar
    // -------------------------------------------------------------------------

    @ConfigItem(
            keyName = "hpPerBox",
            name = "HP Per Box",
            description = "How many HP points each box segment represents",
            section = hpSection,
            position = 0
    )
    default int hpPerBox()
    {
        return 10;
    }

    @ConfigItem(
            keyName = "hpColor",
            name = "HP Color",
            description = "Color of the HP fill",
            section = hpSection,
            position = 1
    )
    default Color hpColor()
    {
        return Color.GREEN;
    }

    @ConfigItem(
            keyName = "hpDamagedColor",
            name = "HP Damaged Color",
            description = "Color shown for the damaged (missing) portion of HP",
            section = hpSection,
            position = 2
    )
    default Color hpDamagedColor()
    {
        return Color.RED;
    }

    @ConfigItem(
            keyName = "lowHpThreshold",
            name = "Low HP Threshold",
            description = "HP value at which the bar switches to the low HP color. Set to 0 to disable",
            section = hpSection,
            position = 3
    )
    default int lowHpThreshold()
    {
        return 5;
    }

    @ConfigItem(
            keyName = "lowHpColor",
            name = "Low HP Color",
            description = "Color of the HP bar when HP drops below the low HP threshold",
            section = hpSection,
            position = 4
    )
    default Color lowHpColor()
    {
        return Color.YELLOW;
    }

    @ConfigItem(
            keyName = "overhealColor",
            name = "Overheal Color",
            description = "Color of the HP bar portion above your base max HP",
            section = hpSection,
            position = 5
    )
    default Color overhealColor()
    {
        return Color.WHITE;
    }

    @Alpha
    @ConfigItem(
            keyName = "foodHealColor",
            name = "Food heal preview color",
            description = "Color shown for potential HP restored by hovered food",
            section = hpSection,
            position = 6
    )
    default Color foodHealColor()
    {
        return new Color(228, 148, 148, 255);
    }

    @ConfigItem(
            keyName = "hpBackgroundColor",
            name = "HP Background Color",
            description = "Background color behind the HP boxes",
            section = hpSection,
            position = 7
    )
    default Color hpBackgroundColor()
    {
        return Color.BLACK;
    }
    // -------------------------------------------------------------------------
    // Prayer Bar
    // -------------------------------------------------------------------------

    @ConfigItem(
            keyName = "showPrayerBar",
            name = "Show Prayer Bar",
            description = "Display a thin prayer bar below the HP bar",
            section = prayerSection,
            position = 0
    )
    default boolean showPrayerBar()
    {
        return true;
    }

    @ConfigItem(
            keyName = "prayerColor",
            name = "Prayer Color",
            description = "Color of the prayer points fill",
            section = prayerSection,
            position = 1
    )
    default Color prayerColor()
    {
        return Color.CYAN;
    }

    @Alpha
    @ConfigItem(
            keyName = "prayerRestoreColor",
            name = "Prayer restore preview color",
            description = "Color shown for potential prayer restored by hovered item",
            section = prayerSection,
            position = 2
    )
    default Color prayerRestoreColor()
    {
        return new Color(187, 187, 255, 255); // soft blue, semi-transparent
    }

    @Alpha
    @ConfigItem(
            keyName = "lowPrayerThreshold",
            name = "Low Prayer Threshold",
            description = "Prayer value at which the bar switches to the low Prayer color. Set to 0 to disable",
            section = prayerSection,
            position = 3
    )
    default int lowPrayerThreshold()
    {
        return 0;
    }

    @Alpha
    @ConfigItem(
            keyName = "lowPrayerColor",
            name = "Low Prayer Color",
            description = "Color of the Prayer bar when Prayer drops below the low Prayer threshold",
            section = prayerSection,
            position = 4
    )
    default Color lowPrayerColor()
    {
        return new Color(244, 224, 102, 255);
    }

    @ConfigItem(
            keyName = "prayerBackgroundColor",
            name = "Prayer Background Color",
            description = "Background color of the prayer bar",
            section = prayerSection,
            position = 5
    )
    default Color prayerBackgroundColor()
    {
        return Color.BLACK;
    }

    @Range(min = 1, max = 10)
    @ConfigItem(
            keyName = "prayerBarHeight",
            name = "Prayer Bar Height",
            description = "Height of the prayer bar",
            section = prayerSection,
            position = 6
    )
    default int prayerBarHeight()
    {
        return 3;
    }
}