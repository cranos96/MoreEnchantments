package cn.feng.enchant.enchantment;

import cn.feng.enchant.MoreEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;

/**
 * @author ChengFeng
 * @since 2024/3/3
 **/
public class BabyEnchantment extends Enchantment {
    public BabyEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.ARMOR, MoreEnchantments.ALL_ARMOR);
    }
}
