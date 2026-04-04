package com.advancedhpbar;

import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.Point;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.ItemStatChangesService;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

public class AdvancedHpBarOverlay extends Overlay
{
    private static final int BOX_GAP = 1;
    private static final int BOX_HEIGHT = 5;
    private static final int PRAYER_BAR_GAP = 1;

    private final Client client;
    private final AdvancedHpBarConfig config;
    private final ItemStatChangesService itemStatService;

    @Inject
    public AdvancedHpBarOverlay(Client client, AdvancedHpBarConfig config, ItemStatChangesService itemstatservice)
    {
        this.client = client;
        this.config = config;
        this.itemStatService = itemstatservice;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ALWAYS_ON_TOP);
    }

    @Override
    public Dimension render(Graphics2D g)
    {
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null)
        {
            return null;
        }

        if (localPlayer.getHealthScale() < 0 && !config.alwaysOn())
        {
            return null;
        }

        final LocalPoint localLocation = localPlayer.getLocalLocation();
        final Point canvasPoint = Perspective.localToCanvas(
                client,
                localLocation,
                client.getTopLevelWorldView().getPlane(),
                localPlayer.getLogicalHeight() + config.zOffset()
        );

        if (canvasPoint == null)
        {
            return null;
        }

        final int barX = canvasPoint.getX() + config.barXOffset();
        final int barY = canvasPoint.getY() + config.barHeightOffset();
        final int barWidth = config.barWidth();
        final int prayerBarHeight = config.prayerBarHeight();

        renderHpBar(g, barX, barY, barWidth);

        if (config.showPrayerBar())
        {
            renderPrayerBar(g, barX, barY, barWidth, prayerBarHeight);
        }

        return null;
    }

    private int getRestoreValue(String skill)
    {
        final MenuEntry[] menu = client.getMenu().getMenuEntries();
        final int menuSize = menu.length;
        if (menuSize == 0)
        {
            return 0;
        }

        final MenuEntry entry = menu[menuSize - 1];
        final Widget widget = entry.getWidget();
        int restoreValue = 0;

        if (widget != null && widget.getId() == InterfaceID.Inventory.ITEMS)
        {
            final Effect change = itemStatService.getItemStatChanges(widget.getItemId());

            if (change != null)
            {
                for (final StatChange c : change.calculate(client).getStatChanges())
                {
                    final int value = c.getTheoretical();

                    if (value != 0 && c.getStat().getName().equals(skill))
                    {
                        restoreValue = value;
                    }
                }
            }
        }

        return restoreValue;
    }

    private void renderHpBar(Graphics2D g, int barX, int barY, int barWidth)
    {
        final int maxHp = client.getRealSkillLevel(Skill.HITPOINTS);
        final int currentHp = client.getBoostedSkillLevel(Skill.HITPOINTS);
        final int overheal = Math.max(0, currentHp - maxHp);
        final int restoreValue = getRestoreValue("Hitpoints");

        if (config.hpPerBox() == 0)
        {
            g.setColor(config.hpDamagedColor());
            g.fillRect(barX, barY, barWidth, BOX_HEIGHT);

            if (maxHp > 0 && currentHp > 0)
            {
                if (overheal > 0)
                {
                    final int normalBoxWidth = (int) Math.round(barWidth * ((double) maxHp / currentHp));
                    final int overhealBoxWidth = barWidth - normalBoxWidth;
                    drawNormalSingleBox(g, barX, barY, normalBoxWidth, currentHp);
                    drawOverhealSingleBox(g, barX + normalBoxWidth, barY, overhealBoxWidth);
                }
                else
                {
                    final int fillWidth = (int) Math.round(barWidth * ((double) currentHp / maxHp));
                    drawNormalSingleBox(g, barX, barY, fillWidth, currentHp);

                    if (restoreValue > 0 && currentHp < maxHp)
                    {
                        final int healCap = Math.min(currentHp + restoreValue, maxHp);
                        final int restoreStart = (int) Math.round(barWidth * ((double) currentHp / maxHp));
                        final int restoreWidth = (int) Math.round(barWidth * ((double) healCap / maxHp)) - restoreStart;
                        if (restoreWidth > 0)
                        {
                            g.setColor(config.foodHealColor());
                            g.fillRect(barX + restoreStart, barY, restoreWidth, BOX_HEIGHT);
                        }
                    }
                }
            }
            return;
        }

        final int numNormalBoxes = (int) Math.ceil((double) maxHp / config.hpPerBox());
        final int lastNormalBoxCapacity = maxHp % config.hpPerBox() == 0
                ? config.hpPerBox()
                : maxHp % config.hpPerBox();

        final int numOverhealBoxes = overheal > 0
                ? (int) Math.ceil((double) overheal / config.hpPerBox())
                : 0;
        final int lastOverhealBoxCapacity = overheal > 0
                ? (overheal % config.hpPerBox() == 0 ? config.hpPerBox() : overheal % config.hpPerBox())
                : 0;

        final int totalBoxes = numNormalBoxes + numOverhealBoxes;
        final int totalGaps = (totalBoxes - 1) * BOX_GAP;

        final double lastNormalRatio = (double) lastNormalBoxCapacity / config.hpPerBox();
        final double lastOverhealRatio = numOverhealBoxes > 0
                ? (double) lastOverhealBoxCapacity / config.hpPerBox()
                : 0.0;

        final double effectiveBoxCount = (numNormalBoxes - 1) + lastNormalRatio
                + (numOverhealBoxes > 0 ? (numOverhealBoxes - 1) + lastOverhealRatio : 0.0);

        if (effectiveBoxCount <= 0)
        {
            return;
        }

        final double fullBoxWidth = (barWidth - totalGaps) / effectiveBoxCount;

        g.setColor(config.hpBackgroundColor());
        g.fillRect(barX, barY, barWidth, BOX_HEIGHT);

        drawNormalBoxes(g, barX, barY, currentHp, maxHp, numNormalBoxes, lastNormalBoxCapacity, fullBoxWidth, restoreValue);
        drawOverhealBoxes(g, barX, barY, overheal, numOverhealBoxes, lastOverhealBoxCapacity, lastNormalRatio, numNormalBoxes, fullBoxWidth);
    }

    private void drawNormalSingleBox(Graphics2D g, int boxX, int barY, int fillWidth, int currentHp)
    {
        g.setColor(getHpColor(currentHp));
        g.fillRect(boxX, barY, fillWidth, BOX_HEIGHT);
    }

    private void drawOverhealSingleBox(Graphics2D g, int boxX, int barY, int fillWidth)
    {
        g.setColor(config.overhealColor());
        g.fillRect(boxX, barY, fillWidth, BOX_HEIGHT);
    }

    private void drawNormalBoxes(Graphics2D g, int barX, int barY, int currentHp, int maxHp,
                                 int numNormalBoxes, int lastNormalBoxCapacity,
                                 double fullBoxWidth, int restoreValue)
    {
        final int healCap = Math.min(currentHp + restoreValue, maxHp);

        for (int i = 0; i < numNormalBoxes; i++)
        {
            final boolean isLast = (i == numNormalBoxes - 1);
            final int boxCapacity = isLast ? lastNormalBoxCapacity : config.hpPerBox();
            final double boxRatio = (double) boxCapacity / config.hpPerBox();

            final double floatStart = barX + i * (fullBoxWidth + BOX_GAP);
            final double floatEnd = floatStart + fullBoxWidth * boxRatio;
            final int boxX = (int) Math.round(floatStart);
            final int thisBoxPixelWidth = (int) Math.round(floatEnd) - boxX;

            final int boxMinHp = i * config.hpPerBox();
            final int boxMaxHp = boxMinHp + boxCapacity;
            final int fill = Math.max(0, Math.min(currentHp - boxMinHp, boxCapacity));

            g.setColor(config.hpDamagedColor());
            g.fillRect(boxX, barY, thisBoxPixelWidth, BOX_HEIGHT);

            if (fill > 0)
            {
                final int fillWidth = (int) Math.round(thisBoxPixelWidth * ((double) fill / boxCapacity));
                g.setColor(getHpColor(currentHp));
                g.fillRect(boxX, barY, fillWidth, BOX_HEIGHT);
            }

            if (restoreValue > 0 && healCap > currentHp)
            {
                final int healStart = Math.max(currentHp, boxMinHp);
                final int healEnd = Math.min(healCap, boxMaxHp);
                if (healEnd > healStart)
                {
                    final int restoreStartPx = (int) Math.round(thisBoxPixelWidth * ((double)(healStart - boxMinHp) / boxCapacity));
                    final int restoreEndPx = (int) Math.round(thisBoxPixelWidth * ((double)(healEnd - boxMinHp) / boxCapacity));
                    g.setColor(config.foodHealColor());
                    g.fillRect(boxX + restoreStartPx, barY, restoreEndPx - restoreStartPx, BOX_HEIGHT);
                }
            }
        }
    }

    private void drawOverhealBoxes(Graphics2D g, int barX, int barY, int overheal,
                                   int numOverhealBoxes, int lastOverhealBoxCapacity,
                                   double lastNormalRatio, int numNormalBoxes, double fullBoxWidth)
    {
        for (int i = 0; i < numOverhealBoxes; i++)
        {
            final boolean isLast = (i == numOverhealBoxes - 1);
            final int boxCapacity = isLast ? lastOverhealBoxCapacity : config.hpPerBox();
            final double boxRatio = (double) boxCapacity / config.hpPerBox();

            final double normalWidth = (numNormalBoxes - 1) * (fullBoxWidth + BOX_GAP)
                    + fullBoxWidth * lastNormalRatio + BOX_GAP;
            final double floatStart = barX + normalWidth + i * (fullBoxWidth + BOX_GAP);
            final double floatEnd = floatStart + fullBoxWidth * boxRatio;
            final int boxX = (int) Math.round(floatStart);
            final int thisBoxPixelWidth = (int) Math.round(floatEnd) - boxX;

            final int boxMinOverheal = i * config.hpPerBox();
            final int fill = Math.max(0, Math.min(overheal - boxMinOverheal, boxCapacity));
            final int fillWidth = (int) Math.round(thisBoxPixelWidth * ((double) fill / boxCapacity));

            if (fillWidth > 0)
            {
                g.setColor(config.overhealColor());
                g.fillRect(boxX, barY, fillWidth, BOX_HEIGHT);
            }
        }
    }

    private void renderPrayerBar(Graphics2D g, int barX, int barY, int barWidth, int barHeight)
    {
        final int maxPrayer = client.getRealSkillLevel(Skill.PRAYER);
        final int currentPrayer = client.getBoostedSkillLevel(Skill.PRAYER);
        final int prayerBarY = barY + BOX_HEIGHT + PRAYER_BAR_GAP;
        final int prayerRestoreValue = getRestoreValue("Prayer");

        g.setColor(config.prayerBackgroundColor());
        g.fillRect(barX, prayerBarY, barWidth, barHeight);

        if (maxPrayer > 0 && currentPrayer > 0)
        {
            final int prayerFillWidth = (int) Math.round(barWidth * ((double) currentPrayer / maxPrayer));
            g.setColor(getPrayerColor(currentPrayer));
            g.fillRect(barX, prayerBarY, prayerFillWidth, barHeight);
        }

        // Prayer restore preview starting where the prayer bar fill ends
        if (prayerRestoreValue > 0 && currentPrayer < maxPrayer && maxPrayer > 0)
        {
            final int prayerHealCap = Math.min(currentPrayer + prayerRestoreValue, maxPrayer);
            final int restoreStart = (int) Math.round(barWidth * ((double) currentPrayer / maxPrayer));
            final int restoreWidth = (int) Math.round(barWidth * ((double) prayerHealCap / maxPrayer)) - restoreStart;
            if (restoreWidth > 0)
            {
                g.setColor(config.prayerRestoreColor());
                g.fillRect(barX + restoreStart, prayerBarY, restoreWidth, barHeight);
            }
        }
    }

    private Color getHpColor(int currentHp)
    {
        return currentHp < config.lowHpThreshold() ? config.lowHpColor() : config.hpColor();
    }

    private Color getPrayerColor(int currentPray)
    {
        return currentPray < config.lowPrayerThreshold() ? config.lowPrayerColor() : config.prayerColor();
    }
}