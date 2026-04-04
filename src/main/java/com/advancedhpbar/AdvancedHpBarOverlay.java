package com.advancedhpbar;

import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.Point;
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
    private static final int PRAYER_BAR_HEIGHT = 2;
    private static final int PRAYER_BAR_GAP = 1;

    private final Client client;
    private final AdvancedHpBarConfig config;

    @Inject
    public AdvancedHpBarOverlay(Client client, AdvancedHpBarConfig config)
    {
        this.client = client;
        this.config = config;
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

        renderHpBar(g, barX, barY, barWidth);

        if (config.showPrayerBar())
        {
            renderPrayerBar(g, barX, barY, barWidth);
        }

        return null;
    }

    private void renderHpBar(Graphics2D g, int barX, int barY, int barWidth)
    {
        final int maxHp = client.getRealSkillLevel(Skill.HITPOINTS);
        final int currentHp = client.getBoostedSkillLevel(Skill.HITPOINTS);
        final int overheal = Math.max(0, currentHp - maxHp);

        // When hpPerBox is 0, render a single full-width bar.
        if (config.hpPerBox() == 0)
        {
            g.setColor(config.hpDamagedColor());
            g.fillRect(barX, barY, barWidth, BOX_HEIGHT);

            if (maxHp > 0 && currentHp > 0)
            {
                if (overheal > 0)
                {
                    // Split proportionally: normal occupies (maxHp / currentHp) of the bar, overheal the rest
                    final int normalBoxWidth = (int) Math.round(barWidth * ((double) maxHp / currentHp));
                    final int overhealBoxWidth = barWidth - normalBoxWidth;
                    drawNormalSingleBox(g, barX, barY, normalBoxWidth, currentHp);
                    drawOverhealSingleBox(g, barX + normalBoxWidth, barY, overhealBoxWidth);
                }
                else
                {
                    final int fillWidth = (int) Math.round(barWidth * ((double) currentHp / maxHp));
                    drawNormalSingleBox(g, barX, barY, fillWidth, currentHp);
                }
            }
            return;
        }

        // --- Normal HP boxes ---
        final int numNormalBoxes = (int) Math.ceil((double) maxHp / config.hpPerBox());
        final int lastNormalBoxCapacity = maxHp % config.hpPerBox() == 0
                ? config.hpPerBox()
                : maxHp % config.hpPerBox();

        // --- Overheal boxes ---
        final int numOverhealBoxes = overheal > 0
                ? (int) Math.ceil((double) overheal / config.hpPerBox())
                : 0;
        final int lastOverhealBoxCapacity = overheal > 0
                ? (overheal % config.hpPerBox() == 0 ? config.hpPerBox() : overheal % config.hpPerBox())
                : 0;

        final int totalBoxes = numNormalBoxes + numOverhealBoxes;
        final int totalGaps = (totalBoxes - 1) * BOX_GAP;

        // The last box in each group may be narrower if HP doesn't divide evenly.
        // This ratio drives how wide the last box is relative to a full box.
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

        // Draw background across the full bar; gaps between boxes will show this color
        g.setColor(config.hpBackgroundColor());
        g.fillRect(barX, barY, barWidth, BOX_HEIGHT);

        drawNormalBoxes(g, barX, barY, currentHp, maxHp, numNormalBoxes, lastNormalBoxCapacity, fullBoxWidth);
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
                                 int numNormalBoxes, int lastNormalBoxCapacity, double fullBoxWidth)
    {
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
            final int fill = Math.max(0, Math.min(currentHp - boxMinHp, boxCapacity));

            // Draw damaged (empty) portion
            g.setColor(config.hpDamagedColor());
            g.fillRect(boxX, barY, thisBoxPixelWidth, BOX_HEIGHT);

            // Draw filled portion
            if (fill > 0)
            {
                final int fillWidth = (int) Math.round(thisBoxPixelWidth * ((double) fill / boxCapacity));
                g.setColor(getHpColor(currentHp));
                g.fillRect(boxX, barY, fillWidth, BOX_HEIGHT);
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

            // Offset past all normal boxes
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

    private void renderPrayerBar(Graphics2D g, int barX, int barY, int barWidth)
    {
        final int maxPrayer = client.getRealSkillLevel(Skill.PRAYER);
        final int currentPrayer = client.getBoostedSkillLevel(Skill.PRAYER);
        final int prayerBarY = barY + BOX_HEIGHT + PRAYER_BAR_GAP;

        g.setColor(config.prayerBackgroundColor());
        g.fillRect(barX, prayerBarY, barWidth, PRAYER_BAR_HEIGHT);

        if (maxPrayer > 0 && currentPrayer > 0)
        {
            final int prayerFillWidth = (int) Math.round(barWidth * ((double) currentPrayer / maxPrayer));
            g.setColor(config.prayerColor());
            g.fillRect(barX, prayerBarY, prayerFillWidth, PRAYER_BAR_HEIGHT);
        }
    }

    private Color getHpColor(int currentHp)
    {
        return currentHp < config.lowHpThreshold() ? config.lowHpColor() : config.hpColor();
    }
}