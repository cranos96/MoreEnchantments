package cn.feng.enchant.util;

import cn.feng.enchant.MoreEnchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ChengFeng
 * @since 2024/3/3
 **/
public class EntityUtil {
    private static final Map<LivingEntity, Integer> agedEntities = new HashMap<>();

    public static boolean isUnSelectable(LivingEntity entity) {
        for (ItemStack armor : entity.getArmorItems()) {
            if (!EnchantUtil.has(armor, MoreEnchantments.UNSELECTABLE, 1)) return false;
        }
        return true;
    }

    public static boolean shouldBeBaby(LivingEntity entity) {
        if (entity == null) return false;
        if (entity.isBaby() || getAge(entity) < 18) return true;
        return hasYouthArmors(entity);
    }

    public static boolean hasYouthArmors(LivingEntity entity) {
        for (ItemStack armor : entity.getArmorItems()) {
            if (!EnchantUtil.has(armor, MoreEnchantments.BABY, 1)) return false;
        }
        return true;
    }

    public static int getAge(LivingEntity entity) {
        if (agedEntities.containsKey(entity)) return agedEntities.get(entity);
        int age = RandomUtil.randomInt(18, 70);
        agedEntities.put(entity, age);
        return age;
    }

    public static void setAge(LivingEntity entity, int age) {
        if (hasYouthArmors(entity)) return;
        agedEntities.put(entity, age);
    }

    public static void removeAge(LivingEntity entity) {
        agedEntities.remove(entity);
    }
}
