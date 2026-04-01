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
import net.runelite.client.ui.overlay.OverlayPriority;

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
        setPriority(OverlayPriority.MED);
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
                client.getPlane(),
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
        final int numBoxes = (int) Math.ceil((double) maxHp / config.hpPerBox());
        final int totalGaps = (numBoxes - 1) * BOX_GAP;
        final double boxWidth = (double)(barWidth - totalGaps) / numBoxes;

        g.setColor(config.hpBackgroundColor());
        g.fillRect(barX, barY, barWidth, BOX_HEIGHT);

        for (int i = 0; i < numBoxes; i++)
        {
            final int boxX = barX + (int) Math.round(i * (boxWidth + BOX_GAP));
            final int boxMinHp = i * config.hpPerBox();
            final int thisBoxCapacity = Math.min(boxMinHp + config.hpPerBox(), maxHp) - boxMinHp;
            final int thisBoxPixelWidth = (int) Math.round(boxWidth * ((double) thisBoxCapacity / config.hpPerBox()));
            final int thisBoxFill = Math.max(0, Math.min(currentHp - boxMinHp, thisBoxCapacity));
            final int fillWidth = (int) Math.round(thisBoxPixelWidth * ((double) thisBoxFill / thisBoxCapacity));

            g.setColor(config.hpDamagedColor());
            g.fillRect(boxX, barY, thisBoxPixelWidth, BOX_HEIGHT);

            if (fillWidth > 0)
            {
                g.setColor(getHpColor(currentHp));
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