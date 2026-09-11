package net.itskiwibread.civtfg_progression.client;

import com.mojang.blaze3d.systems.RenderSystem;

import net.itskiwibread.civtfg_progression.Menu.LaboratoryMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LaboratoryScreen
        extends AbstractContainerScreen<LaboratoryMenu> {

    // =========================================================
    // TEXTURES
    // =========================================================

    private static final ResourceLocation GUI_TEXTURE =
            new ResourceLocation(
                    "civtfg_progression",
                    "textures/gui/laboratory.png"
            );

    private static final ResourceLocation PROGRESS_TEXTURE =
            new ResourceLocation(
                    "civtfg_progression",
                    "textures/gui/laboratory_progress.png"
            );

    private static final ResourceLocation PROCESSING_TEXTURE =
            new ResourceLocation(
                    "civtfg_progression",
                    "textures/gui/laboratory_progressing.png"
            );

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public LaboratoryScreen(
            LaboratoryMenu menu,
            Inventory inventory,
            Component title) {

        super(
                menu,
                inventory,
                title
        );

        // Your GUI is 176 x 166
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    // =========================================================
    // BACKGROUND
    // =========================================================

    @Override
    protected void renderBg(
            GuiGraphics guiGraphics,
            float partialTick,
            int mouseX,
            int mouseY) {

        int x =
                (width - imageWidth) / 2;

        int y =
                (height - imageHeight) / 2;

        // =====================================================
        // MAIN GUI
        // =====================================================

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

        // =====================================================
        // FIRST BAR
        //
        // Shared item-consumption timer.
        //
        // 0 -> 100
        //
        // When it reaches 100, one item is consumed from
        // EVERY occupied valid laboratory slot.
        // =====================================================

        int consumeProgress =
                menu.getConsumeProgress();

        int consumeMax =
                menu.getConsumeProgressMax();

        if (consumeMax > 0 && consumeProgress > 0) {

            int progressWidth =
                    (int) (
                            (float) consumeProgress
                                    / consumeMax
                                    * 126
                    );

            guiGraphics.blit(
                    PROGRESS_TEXTURE,

                    // Position
                    x + 26,
                    y + 56,

                    // Texture UV
                    0,
                    0,

                    // Size to render
                    progressWidth,
                    5,

                    // Full texture size
                    126,
                    5
            );
        }

        // =====================================================
        // SECOND BAR
        //
        // Overall laboratory progress.
        //
        // Each consumption cycle adds the number of items
        // consumed during that cycle.
        // =====================================================

        int laboratoryProgress =
                menu.getLaboratoryProgress();

        int laboratoryMax =
                menu.getLaboratoryProgressMax();

        if (laboratoryMax > 0 && laboratoryProgress > 0) {

            int progressWidth =
                    (int) (
                            (float) laboratoryProgress
                                    / laboratoryMax
                                    * 126
                    );

            guiGraphics.blit(
                    PROCESSING_TEXTURE,

                    // Position
                    x + 26,
                    y + 65,

                    // Texture UV
                    0,
                    0,

                    // Size to render
                    progressWidth,
                    5,

                    // Full texture size
                    126,
                    5
            );
        }
    }

    // =========================================================
    // RENDER
    // =========================================================

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick) {

        this.renderBackground(graphics);

        super.render(
                graphics,
                mouseX,
                mouseY,
                partialTick
        );

        this.renderTooltip(
                graphics,
                mouseX,
                mouseY
        );
    }
}