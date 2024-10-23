package net.player005.betteraddserver.mixin.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.network.chat.Component;
import net.player005.betteraddserver.AddressToName;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(JoinMultiplayerScreen.class)
public abstract class MixinMultiplayerScreen extends Screen {
    protected MixinMultiplayerScreen(Component title) {
        super(title);
    }

    @Shadow public abstract ServerList getServers();

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        var serverList = getServers();

        boolean changedSomething = false;
        for (int i = 0; i < serverList.size(); i++) {
            ServerData serverInfo = serverList.get(i);
            if (Objects.equals(serverInfo.name, "Minecraft Server")) {
                var name = AddressToName.toName(serverInfo.ip);
                if (name.isEmpty()) continue;
                serverInfo.name = name;
                changedSomething = true;
            }
        }

        if (changedSomething) serverList.save();
    }
}
