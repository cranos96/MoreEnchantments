package cn.feng.enchant.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author ChengFeng
 * @since 2024/2/25
 **/
@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow public abstract World getWorld();

    @Shadow public abstract void emitGameEvent(GameEvent event);

    @Shadow public abstract BlockPos getBlockPos();
}
