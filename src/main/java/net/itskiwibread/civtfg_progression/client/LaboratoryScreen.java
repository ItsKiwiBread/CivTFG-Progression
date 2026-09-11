package net.itskiwibread.civtfg_progression.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.itskiwibread.civtfg_progression.Menu.LaboratoryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LaboratoryScreen extends AbstractContainerScreen<LaboratoryMenu> {

    private static final ResourceLocation GUI_TEXTURE =
            new ResourceLocation("civtfg_progression", "textures/gui/laboratory.png");

    private static final ResourceLocation PROGRESS_TEXTURE =
            new ResourceLocation("civtfg_progression", "textures/gui/laboratory_progress.png");

    private static final ResourceLocation PROCESSING_TEXTURE =
            new ResourceLocation("civtfg_progression", "textures/gui/laboratory_progressing.png");

    public LaboratoryScreen(LaboratoryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.imageWidth = 176;
        this.imageHeight = 166;
    }
    private static final Component TITLE =
            Component.translatable("container.civtfg_progression.laboratory");

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Main GUI
        guiGraphics.blit(
                GUI_TEXTURE,
                x,
                y,
                0,
                0,
                imageWidth,
                imageHeight,
                imageWidth,
                imageHeight
        );

        // First bar - consuming item
        int consumeProgress = menu.getConsumeProgress();
        int consumeMax = menu.getConsumeProgressMax();

        if (consumeMax > 0 && consumeProgress > 0) {
            int progressWidth =
                    (int) ((float) consumeProgress / consumeMax * 126);

            guiGraphics.blit(
                    PROGRESS_TEXTURE,
                    x + 26,
                    y + 56,
                    0,
                    0,
                    progressWidth,
                    5,
                    126,
                    5
            );
        }

        // Second bar - laboratory progress
        int laboratoryProgress = menu.getLaboratoryProgress();
        int laboratoryMax = menu.getLaboratoryProgressMax();

        if (laboratoryMax > 0 && laboratoryProgress > 0) {
            int progressWidth =
                    (int) ((float) laboratoryProgress / laboratoryMax * 126);

            guiGraphics.blit(
                    PROCESSING_TEXTURE,
                    x + 26,
                    y + 65,
                    0,
                    0,
                    progressWidth,
                    5,
                    126,
                    5
            );
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }
}