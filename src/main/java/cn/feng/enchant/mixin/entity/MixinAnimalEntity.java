package cn.feng.enchant.mixin.entity;

import net.minecraft.entity.passive.AnimalEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author ChengFeng
 * @since 2024/2/27
 **/
@Mixin(AnimalEntity.class)
public abstract class MixinAnimalEntity extends MixinPassiveEntity {
}
