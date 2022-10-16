package net.player005.betteraddserver.mixin.screen;

import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.player005.betteraddserver.IP2Name;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AddServerScreen.class)
public abstract class MixinAddServerScreen extends Screen {
    public TextFieldWidget serverNameField;
    public TextFieldWidget addressField;

    public MixinAddServerScreen(Text title) {
        super(title);
    }

    @Inject(method = "tick",at = @At("TAIL"))
    public void tick(CallbackInfo info){
        if(this.addressField.isActive()) {
            this.serverNameField.setText(IP2Name.IP2Name(this.addressField.getText()));
        }
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void init(CallbackInfo info){
        int adrX = this.addressField.x;
        int adrY = this.addressField.y;
        int nameX = this.serverNameField.x;
        int nameY = this.serverNameField.y;

        //swap position of addressField and nameField and the labels
        this.addressField.x = nameX;
        this.addressField.y = nameY;
        this.serverNameField.x = adrX;
        this.serverNameField.y = adrY;

        this.addressField.setTextFieldFocused(true);
        this.addressField.active = true;
    }
}
