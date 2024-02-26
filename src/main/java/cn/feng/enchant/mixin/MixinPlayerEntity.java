package cn.feng.enchant.mixin;

import cn.feng.enchant.util.TimerUtil;
import cn.feng.enchant.util.WorldUtil;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author ChengFeng
 * @since 2024/2/25
 **/
@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends MixinLivingEntity {
    @Unique
    private TimerUtil timerUtil = new TimerUtil();

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (!timerUtil.hasDelayed(100)) return;
        WorldUtil.armorLightning((PlayerEntity) (Object) this);
        timerUtil.reset();
    }
}
