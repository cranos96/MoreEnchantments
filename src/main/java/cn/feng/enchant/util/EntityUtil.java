package cn.feng.enchant.util;

import cn.feng.enchant.MoreEnchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

/**
 * @author ChengFeng
 * @since 2024/3/3
 **/
public class EntityUtil {
    public static boolean isUnSelectable(LivingEntity entity) {
        for (ItemStack armor : entity.getArmorItems()) {
            if (!EnchantUtil.has(armor, MoreEnchantments.UNSELECTABLE, 1)) return false;
        }
        return true;
    }

    public static boolean shouldBeBaby(LivingEntity entity) {
        if (entity.isBaby()) return true;
        for (ItemStack armor : entity.getArmorItems()) {
            if (!EnchantUtil.has(armor, MoreEnchantments.BABY, 1)) return false;
        }
        return true;
    }
}
