package com.mallardlabs.matscraft;

import net.fabricmc.api.ClientModInitializer;

import com.mallardlabs.matscraft.client.CustomHudRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class MatsCraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CustomHudRenderer hudRenderer = new CustomHudRenderer();
        HudRenderCallback.EVENT.register(hudRenderer);
    }
}
