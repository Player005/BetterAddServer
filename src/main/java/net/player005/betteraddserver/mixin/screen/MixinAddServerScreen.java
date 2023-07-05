package net.player005.betteraddserver.mixin.screen;

import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.player005.betteraddserver.IP2Name;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AddServerScreen.class)
public class MixinAddServerScreen extends Screen {

    @Shadow private TextFieldWidget serverNameField;

    @Shadow private TextFieldWidget addressField;

    protected MixinAddServerScreen(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        int addrXOld = addressField.getX();
        int addrYOld = addressField.getY();
        addressField.setX(serverNameField.getX());
        addressField.setY(serverNameField.getY());
        serverNameField.setX(addrXOld);
        serverNameField.setY(addrYOld);
        this.setInitialFocus(addressField);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if (IP2Name.toName(addressField.getText()) != null) {
            serverNameField.setText(IP2Name.toName(addressField.getText()));
        }
    }
}
