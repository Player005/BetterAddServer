package net.player005.betteraddserver.mixin.screen;

import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.player005.betteraddserver.BetterAddServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AddServerScreen.class)
public abstract class MixinAddServerScreen extends Screen {

    public MixinAddServerScreen(Text title) {
        super(title);
    }

    @Inject(method = "tick",at = @At("HEAD"))
    public void tick(CallbackInfo info){
        BetterAddServer.LOGGER.info("Ticked!");
    }
}
