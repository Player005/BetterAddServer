package net.player005.betteraddserver.mixin.screen;

import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.player005.betteraddserver.BetterAddServer;
import net.player005.betteraddserver.IP2Name;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(AddServerScreen.class)
public abstract class MixinAddServerScreen extends Screen {
    public TextFieldWidget serverNameField;
    public TextFieldWidget addressField;
    @Shadow private void addAndClose(){}

    public MixinAddServerScreen(Text title) {
        super(title);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        if (this.addressField.isActive()) {
            this.serverNameField.setText(IP2Name.toName(this.addressField.getText()));
        }
        if (this.serverNameField.isFocused()) {
            this.addressField.setTextFieldFocused(false);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            BetterAddServer.LOGGER.info("ENTER");

            if (!Objects.equals(this.addressField.getText(), "") & !Objects.equals(this.serverNameField.getText(), "")) {
                this.addAndClose();
            }
            return false;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void init(CallbackInfo info) {
        int adrX = this.addressField.getX();
        int adrY = this.addressField.getY();
        int nameX = this.serverNameField.getX();
        int nameY = this.serverNameField.getY();

        //swap position of addressField and nameField
        this.addressField.setX(nameX);
        this.addressField.setY(nameY);
        this.serverNameField.setX(adrX);
        this.serverNameField.setY(adrY);

        if (Objects.equals(this.serverNameField.getText(), "Minecraft-Server")) {
            this.serverNameField.setText("");
        }
        this.serverNameField.setTextFieldFocused(false);
        this.setInitialFocus(this.addressField);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        AddServerScreen.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 17, 0xFFFFFF);
        AddServerScreen.drawTextWithShadow(matrices, this.textRenderer, Text.translatable("addServer.enterName"), this.width / 2 - 100, 94, 0xA0A0A0);
        AddServerScreen.drawTextWithShadow(matrices, this.textRenderer, Text.translatable("addServer.enterIp"), this.width / 2 - 100, 53, 0xA0A0A0);
        this.serverNameField.render(matrices, mouseX, mouseY, delta);
        this.addressField.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
