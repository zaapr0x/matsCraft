package com.mallardlabs.matscraft.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

public class CustomHudRenderer implements HudRenderCallback {
    private String firstLine = "";
    private String secondLine = "";

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getScaledWidth();

        // Calculate positions for both lines
        int firstLineWidth = client.textRenderer.getWidth(firstLine);
        int secondLineWidth = client.textRenderer.getWidth(secondLine);
        int x1 = (width - firstLineWidth) / 2;
        int x2 = (width - secondLineWidth) / 2;

        // Y position: 10 pixels from the top for the first line,
        // and 10 pixels below that for the second line
        int y1 = 10;
        int y2 = y1 + client.textRenderer.fontHeight + 2; // 2 pixels of extra spacing

        // Draw both lines
        drawContext.drawTextWithShadow(client.textRenderer, Text.literal(firstLine), x1, y1, 0xFFFFFF);
        drawContext.drawTextWithShadow(client.textRenderer, Text.literal(secondLine), x2, y2, 0xFFFFFF);
    }

    public void setFirstLine(String text) {
        this.firstLine = text;
    }

    public void setSecondLine(String text) {
        this.secondLine = text;
    }
}