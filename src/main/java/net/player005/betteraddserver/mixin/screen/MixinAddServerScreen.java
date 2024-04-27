package net.player005.betteraddserver.mixin.screen;

import net.minecraft.client.gui.screen.multiplayer.AddServerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.player005.betteraddserver.IP2Name;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AddServerScreen.class)
public abstract class MixinAddServerScreen extends Screen {

    @Shadow
    private TextFieldWidget serverNameField;
    @Shadow
    private TextFieldWidget addressField;
    @Shadow
    private ButtonWidget addButton;

    protected MixinAddServerScreen(Text title) {
        super(title);
    }

    @Shadow
    protected abstract void addAndClose();

    @Shadow
    protected abstract void updateAddButton();

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        swapInputFields();

        addressField.setChangedListener(s -> {
            serverNameField.setText("");
            serverNameField.setText(IP2Name.toName(s));
            this.updateAddButton();
            updateFieldSuggestions();
        });

        serverNameField.setText("");
        updateFieldSuggestions();
    }

    @Unique
    private void swapInputFields() {
        int addressFieldOldX = addressField.getX();
        int addressFieldOldY = addressField.getY();
        addressField.setX(serverNameField.getX());
        addressField.setY(serverNameField.getY());
        serverNameField.setX(addressFieldOldX);
        serverNameField.setY(addressFieldOldY);
    }

    @Override
    public void setInitialFocus() {
        setInitialFocus(addressField);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER && this.addButton.active) {
            this.addAndClose();
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Unique
    private void updateFieldSuggestions() {
        if (serverNameField.getText().isEmpty() & addressField.getText().isEmpty()) {
            serverNameField.setSuggestion("Hypixel");
            addressField.setSuggestion("hypixel.net");
        } else {
            serverNameField.setSuggestion("");
            addressField.setSuggestion("");
        }
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"), index = 3)
    private int injectedY(int Y) {
        if (Y <= 53) return 94;
        else return 53;
    }

}
