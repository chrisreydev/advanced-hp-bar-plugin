package com.advancedhpbar;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
        name = "Advanced Hp Bar"
)
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

    @Override
    protected void startUp()
    {
        log.info("Advanced HP Bar started!");
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown()
    {
        log.info("Advanced HP Bar stopped!");
        overlayManager.remove(overlay);
    }

    @Provides
    AdvancedHpBarConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(AdvancedHpBarConfig.class);
    }
}