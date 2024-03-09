package cn.feng.enchant.enchantment;

import cn.feng.enchant.util.EntityUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

/**
 * @author ChengFeng
 * @since 2024/3/3
 **/
public class LifeShorteningEnchantment extends Enchantment {
    public LifeShorteningEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (target instanceof LivingEntity livingEntity) {
            int newAge = EntityUtil.getAge(livingEntity) + 6 * level;
            EntityUtil.setAge(livingEntity, newAge);

            if (newAge > 100) {
                EntityUtil.removeAge(livingEntity);
                livingEntity.kill();
            } else {
                EntityUtil.setAge(user, EntityUtil.getAge(user) + 3 * level);
                user.sendMessage(Text.literal("你使用秘法让敌人折寿 " + 6 * level + " 年，受到反噬，苍老 " + 3 * level + " 岁！"));
            }
        }
    }
}
