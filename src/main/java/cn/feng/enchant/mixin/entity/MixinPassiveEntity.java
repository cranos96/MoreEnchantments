package cn.feng.enchant.mixin.entity;

import net.minecraft.entity.passive.PassiveEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author ChengFeng
 * @since 2024/2/27
 **/
@Mixin(PassiveEntity.class)
public abstract class MixinPassiveEntity extends MixinPathAwareEntity {
}
