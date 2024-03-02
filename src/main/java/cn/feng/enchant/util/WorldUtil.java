package cn.feng.enchant.util;

import cn.feng.enchant.MoreEnchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * @author ChengFeng
 * @since 2024/2/25
 **/
public class WorldUtil {
    public static void summonLightningNearby(BlockPos center, LivingEntity entity) {
        BlockPos pos = PositionUtil.randomNearbyPos(center, 20);

        LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(entity.getWorld());
        if (lightningEntity != null) {
            lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(pos));
            entity.getWorld().spawnEntity(lightningEntity);
            SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_THUNDER;
            entity.playSound(soundEvent, 1, 1.0F);
        }
    }

    public static void armorLightning(LivingEntity entity) {
        boolean channeling = false;
        for (ItemStack armor : entity.getArmorItems()) {
            channeling = EnchantUtil.has(armor, MoreEnchantments.LIGHTNING_GOD, 1);
            if (channeling) break;
        }
        if (channeling) {
            WorldUtil.summonLightningNearby(entity.getBlockPos(), entity);
        }
    }
}
