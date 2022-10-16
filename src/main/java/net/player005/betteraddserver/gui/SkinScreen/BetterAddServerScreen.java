package net.player005.betteraddserver.gui.SkinScreen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.io.File;

public class BetterAddServerScreen extends Screen {
    public final Screen parent;
    final File folder;
    public String error;

    public BetterAddServerScreen(Screen scr) {
        super(Text.translatable("skin.change_skin"));
        this.parent = scr;
        this.folder = new File("skins");
    }

    @Override
    protected void init() {

        //back button
        this.addDrawableChild(new ButtonWidget((this.width - (this.width / 4)) + 2, this.height - 24, 100, 20, Text.translatable("gui.back"), button -> {
            MinecraftClient.getInstance().setScreen(parent);
        }));


    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(0);
        super.render(matrices, mouseX, mouseY, delta);


        //draws error messages
        TextRenderer font = MinecraftClient.getInstance().textRenderer;
        drawCenteredText(matrices, font, error, this.width - this.width / 4, 12, 0xFFFFFF);
    }

}
