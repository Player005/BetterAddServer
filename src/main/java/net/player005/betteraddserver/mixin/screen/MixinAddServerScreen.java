package net.player005.betteraddserver.mixin.screen;

import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.player005.betteraddserver.IP2Name;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AddServerScreen.class)
public abstract class MixinAddServerScreen extends Screen {

    @Shadow private TextFieldWidget serverNameField;
    @Shadow private TextFieldWidget addressField;
    @Shadow protected abstract void addAndClose();

    protected MixinAddServerScreen(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        //swap fields
        int addrXOld = addressField.getX();
        int addrYOld = addressField.getY();
        addressField.setX(serverNameField.getX());
        addressField.setY(serverNameField.getY());
        serverNameField.setX(addrXOld);
        serverNameField.setY(addrYOld);
        addressField.setChangedListener(s -> {
            String name = IP2Name.toName(s);
            if (name != null) serverNameField.setText(name);
        });

        this.setInitialFocus(addressField);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == GLFW.GLFW_KEY_ENTER) {
            this.addAndClose();
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/AddServerScreen;drawTextWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V"), index = 4)
    private int injectedY(int Y) {
        if (Y <= 53) return 94;
        else return 53;
    }
}
