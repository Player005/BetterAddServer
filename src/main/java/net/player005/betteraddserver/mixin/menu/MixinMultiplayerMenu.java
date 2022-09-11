package net.player005.betteraddserver.mixin.menu;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.player005.betteraddserver.gui.SkinScreen.SkinScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MixinMultiplayerMenu extends Screen {
    public MixinMultiplayerMenu(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void init(CallbackInfo info) {


        this.addDrawableChild(new ButtonWidget(25, 6, 100, 20, Text.translatable("skin.change_skin"), button -> {
            MinecraftClient.getInstance().setScreen(new SkinScreen(this));
        }));

    }

}