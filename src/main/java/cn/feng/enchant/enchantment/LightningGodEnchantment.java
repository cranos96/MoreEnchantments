package cn.feng.enchant.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

/**
 * @author ChengFeng
 * @since 2024/2/25
 **/
public class LightningGodEnchantment extends Enchantment {
    public LightningGodEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.ARMOR, EquipmentSlot.values());
    }
}
