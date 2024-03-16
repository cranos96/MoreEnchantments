package cn.feng.enchant.mixin.entity;

import cn.feng.enchant.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author ChengFeng
 * @since 2024/2/25
 **/
@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow
    public float fallDistance;
    @Shadow
    @Final
    protected Random random;

    @Shadow
    public abstract @Nullable ItemEntity dropStack(ItemStack stack);

    @Shadow
    public abstract World getWorld();

    @Shadow
    public abstract void emitGameEvent(GameEvent event);

    @Shadow public abstract @Nullable ItemEntity dropItem(ItemConvertible item);

    @Inject(method = "getTargetingMargin", at = @At("RETURN"), cancellable = true)
    private void hookMargin(CallbackInfoReturnable<Float> callback) {
        Entity entity = (Entity) (Object) this;
        if (!(entity instanceof LivingEntity)) return;
        callback.setReturnValue(EntityUtil.shouldBeBaby((LivingEntity) entity)? -0.5f : 0f);
    }
}
