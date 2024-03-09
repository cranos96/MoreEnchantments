package cn.feng.enchant.util;

import cn.feng.enchant.MoreEnchantments;
import net.minecraft.enchantment.BindingCurseEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.VanishingCurseEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

/**
 * @author ChengFeng
 * @since 2024/2/26
 **/
public class EnchantUtil {
    public static boolean canSchrodinger(Enchantment one, Enchantment another) {
        return (one instanceof BindingCurseEnchantment && another instanceof VanishingCurseEnchantment) || (one instanceof VanishingCurseEnchantment && another instanceof BindingCurseEnchantment);
    }
    public static boolean shouldRenderAge(LivingEntity player) {
        return has(player.getMainHandStack(), MoreEnchantments.BABY, 1) || has(player.getMainHandStack(), MoreEnchantments.LIFE_SHORTENING, 1);
    }

    public static boolean has(ItemStack stack, Enchantment enchantment, int minLevel) {
        return EnchantmentHelper.getLevel(enchantment, stack) >= minLevel;
    }
}
