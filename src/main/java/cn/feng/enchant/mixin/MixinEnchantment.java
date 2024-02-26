package cn.feng.enchant.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * @author ChengFeng
 * @since 2024/2/25
 **/
@Mixin(Enchantment.class)
public class MixinEnchantment {
    /**
     * @author ChengFeng
     * @reason Make enchantments acceptable for everything.
     */
    @Overwrite
    public boolean isAcceptableItem(ItemStack stack) {
        return true;
    }
}
