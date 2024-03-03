package cn.feng.enchant.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

/**
 * @author ChengFeng
 * @since 2024/2/25
 **/
public class HotPotatoEnchantment extends Enchantment {
    public HotPotatoEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public boolean isCursed() {
        return true;
    }
}
