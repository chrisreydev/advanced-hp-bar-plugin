package com.advancedhpbar;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.plugins.itemstats.ItemStatPlugin;

@Slf4j
@PluginDescriptor(
        name = "Advanced Hp Bar"
)
@PluginDependency(ItemStatPlugin.class)
public class AdvancedHpBarPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private AdvancedHpBarConfig config;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private AdvancedHpBarOverlay overlay;

    private long lastTickTime;

    @Override
    protected void startUp()
    {
        log.info("Advanced HP Bar started!");
        lastTickTime = System.currentTimeMillis();
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown()
    {
        log.info("Advanced HP Bar stopped!");
        overlayManager.remove(overlay);
    }

    @Subscribe
    public void onGameTick(GameTick tick)
    {
        lastTickTime = System.currentTimeMillis();
    }

    long millisSinceTick()
    {
        return System.currentTimeMillis() - lastTickTime;
    }

    @Provides
    AdvancedHpBarConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(AdvancedHpBarConfig.class);
    }
}