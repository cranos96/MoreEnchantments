package cn.feng.enchant.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author ChengFeng
 * @since 2024/2/25
 **/
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends MixinEntity {

    @Inject(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    private void increaseFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0) {
            stack.increment(1);
        }
    }
}
