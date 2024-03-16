package cn.feng.enchant.enchantment;

import cn.feng.enchant.MoreEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;

/**
 * @author ChengFeng
 * @since 2024/3/16
 **/
public class SoapEnchantment extends Enchantment {
    public SoapEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.ARMOR, MoreEnchantments.ALL_ARMOR);
    }

    @Override
    public boolean isCursed() {
        return true;
    }
}
