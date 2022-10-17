package net.player005.betteraddserver.mixin.screen;

import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
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

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta){
        this.renderBackground(matrices);
        AddServerScreen.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 17, 0xFFFFFF);
        AddServerScreen.drawTextWithShadow(matrices, this.textRenderer, Text.translatable("addServer.enterName"), this.width / 2 - 100, 94, 0xA0A0A0);
        AddServerScreen.drawTextWithShadow(matrices, this.textRenderer, Text.translatable("addServer.enterIp"), this.width / 2 - 100, 53, 0xA0A0A0);
        this.serverNameField.render(matrices, mouseX, mouseY, delta);
        this.addressField.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

}
