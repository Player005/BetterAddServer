package net.player005.betteraddserver.mixin.screen;

import net.minecraft.client.gui.screens.EditServerScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.player005.betteraddserver.AddressToName;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(EditServerScreen.class)
public abstract class MixinEditServerScreen extends Screen {

    @Shadow
    private EditBox nameEdit;
    @Shadow
    private EditBox ipEdit;
    @Shadow
    private Button addButton;

    protected MixinEditServerScreen(Component title) {
        super(title);
    }

    @Shadow
    protected abstract void onAdd();

    @Shadow
    protected abstract void updateAddButtonStatus();


    @Unique
    private String lastGeneratedName = "";

    @Unique
    public boolean wasNameCustomised = false;

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        swapInputFields();

        ipEdit.setResponder(this::onAddressFieldChange);
        nameEdit.setResponder(s -> {
            updateAddButtonStatus();
            updateSuggestions();
            wasNameCustomised = !Objects.equals(s, lastGeneratedName);
        });

        if (ipEdit.getValue().isEmpty())
            nameEdit.setValue("");

        lastGeneratedName = AddressToName.toName(ipEdit.getValue());
        wasNameCustomised = !Objects.equals(nameEdit.getValue(), lastGeneratedName);

        updateSuggestions();
    }

    @Unique
    private void onAddressFieldChange(String s) {
        var generatedName = AddressToName.toName(s);
        lastGeneratedName = generatedName;
        if (!generatedName.isEmpty() && !wasNameCustomised) nameEdit.setValue(generatedName);
        this.updateAddButtonStatus();
        updateSuggestions();
    }

    @Unique
    private void swapInputFields() {
        int addressFieldOldX = ipEdit.getX();
        int addressFieldOldY = ipEdit.getY();
        ipEdit.setX(nameEdit.getX());
        ipEdit.setY(nameEdit.getY());
        nameEdit.setX(addressFieldOldX);
        nameEdit.setY(addressFieldOldY);
    }

    /**
     * @author Player005
     * @reason initial focus should always be the ip field
     */
    @Overwrite
    public void setInitialFocus() {
        setInitialFocus(ipEdit);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER && this.addButton.active) {
            this.onAdd();
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Unique
    private void updateSuggestions() {
        if (nameEdit.getValue().isEmpty() && ipEdit.getValue().isEmpty()) {
            nameEdit.setSuggestion("Hypixel");
            ipEdit.setSuggestion("hypixel.net");
        } else {
            nameEdit.setSuggestion("");
            ipEdit.setSuggestion("");
        }
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I"), index = 3)
    private int injectedY(int Y) {
        if (Y <= 53) return 94;
        else return 53;
    }

}
