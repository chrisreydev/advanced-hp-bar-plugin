package com.advancedhpbar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

/**
 * Redraws NPC healthbars after the vanilla ones have been blanked by
 * {@link BlankHealthBarOverride}.
 * The local player is intentionally skipped as we are drawing the advanced hp bar
 */
public class NpcHpBarOverlay extends Overlay
{
    private static final int BAR_WIDTH = 30;
    private static final int BAR_HEIGHT = 5;

    private static final int BAR_Z_OFFSET = 25;
    private static final int BAR_X_OFFSET = -(BAR_WIDTH / 2);
    private static final int BAR_Y_OFFSET = 0;

    private static final Color BAR_HEALTH = Color.GREEN;   // green
    private static final Color BAR_DAMAGE = Color.RED;  // dark red

    private final Client client;

    @Inject
    public NpcHpBarOverlay(Client client)
    {
        this.client = client;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D g)
    {
        final Player localPlayer = client.getLocalPlayer();

        for (final NPC npc : client.getTopLevelWorldView().npcs())
        {
            if (npc == null)
            {
                continue;
            }

            if (isLocalPlayer(npc, localPlayer))
            {
                // skip if local player. We are rendering our Advanced HP Bar
                continue;
            }

            drawHealthBar(g, npc);
        }

        return null;
    }

    private boolean isLocalPlayer(Actor actor, Player localPlayer)
    {
        return localPlayer != null && actor == localPlayer;
    }

    private void drawHealthBar(Graphics2D g, NPC npc)
    {
        final int healthScale = npc.getHealthScale();
        final int healthRatio = npc.getHealthRatio();

        if (healthScale <= 0 || healthRatio < 0)
        {
            return;
        }

        final LocalPoint lp = npc.getLocalLocation();
        if (lp == null)
        {
            return;
        }

        final Point canvasPoint = Perspective.localToCanvas(
                client,
                lp,
                client.getTopLevelWorldView().getPlane(),
                npc.getLogicalHeight() + BAR_Z_OFFSET
        );

        if (canvasPoint == null)
        {
            return;
        }

        final int x = canvasPoint.getX() + BAR_X_OFFSET;
        final int y = canvasPoint.getY() + BAR_Y_OFFSET;

        final double fraction = Math.max(0.0, Math.min(1.0,
                (double) healthRatio / (double) healthScale));
        final int fillWidth = (int) Math.round(BAR_WIDTH * fraction);

        g.setColor(BAR_DAMAGE);
        g.fillRect(x, y, BAR_WIDTH, BAR_HEIGHT);

        if (fillWidth > 0)
        {
            g.setColor(BAR_HEALTH);
            g.fillRect(x, y, fillWidth, BAR_HEIGHT);
        }
    }
}