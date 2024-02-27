package cn.feng.enchant.mixin.handler;

import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author ChengFeng
 * @since 2024/2/26
 **/
@Mixin(ScreenHandler.class)
public abstract class MixinScreenHandler {
    @Shadow public abstract void sendContentUpdates();
}
