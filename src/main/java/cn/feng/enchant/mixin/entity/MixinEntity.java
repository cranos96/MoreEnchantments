package cn.feng.enchant.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author ChengFeng
 * @since 2024/2/25
 **/
@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow public float fallDistance;
    @Shadow
    @Final
    protected Random random;

    @Shadow
    public abstract @Nullable ItemEntity dropStack(ItemStack stack);

    @Shadow
    public abstract World getWorld();

    @Shadow
    public abstract void emitGameEvent(GameEvent event);
}
