package cn.feng.enchant.mixin.entity;

import net.minecraft.entity.mob.PathAwareEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author ChengFeng
 * @since 2024/2/27
 **/
@Mixin(PathAwareEntity.class)
public abstract class MixinPathAwareEntity extends MixinMobEntity {
}
