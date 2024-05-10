package net.player005.betteraddserver.mixin.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import net.minecraft.text.Text;
import net.player005.betteraddserver.AddressToName;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(MultiplayerScreen.class)
public abstract class MixinMultiplayerScreen extends Screen {
    protected MixinMultiplayerScreen(Text title) {
        super(title);
    }

    @Shadow public abstract ServerList getServerList();

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        var serverList = getServerList();

        boolean changedSomething = false;
        for (int i = 0; i < serverList.size(); i++) {
            ServerInfo serverInfo = serverList.get(i);
            if (Objects.equals(serverInfo.name, "Minecraft Server")) {
                var name = AddressToName.toName(serverInfo.address);
                if (name.isEmpty()) continue;
                serverInfo.name = name;
                changedSomething = true;
            }
        }

        if (changedSomething) serverList.saveFile();
    }
}
