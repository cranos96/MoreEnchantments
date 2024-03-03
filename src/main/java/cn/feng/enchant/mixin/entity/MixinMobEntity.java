package cn.feng.enchant.mixin.entity;

import cn.feng.enchant.MoreEnchantments;
import cn.feng.enchant.util.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author ChengFeng
 * @since 2024/2/25
 **/
@Mixin(MobEntity.class)
public abstract class MixinMobEntity extends MixinLivingEntity {
    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow protected abstract float getDropChance(EquipmentSlot slot);

    @Shadow public abstract void equipStack(EquipmentSlot slot, ItemStack stack);
    @Unique
    private TimerUtil timerUtil = new TimerUtil();

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (((Object) this) instanceof HostileEntity entity) {
            if (!timerUtil.hasDelayed(100)) return;
            WorldUtil.armorLightning(entity);
            timerUtil.reset();
        }
    }

    @Inject(method = "tryAttack", at = @At("HEAD"), cancellable = true)
    private void tryAttack(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (target instanceof LivingEntity livingEntity && EntityUtil.isUnSelectable(livingEntity)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    /**
     * @author ChengFeng
     * @reason Schrodinger Curse
     */
    @Overwrite
    public void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            boolean bl;
            ItemStack itemStack = this.getEquippedStack(equipmentSlot);

            if (EnchantUtil.has(itemStack, MoreEnchantments.SCHRODINGER_CURSE, 1)) {
                ItemStack newStack = ItemUtil.randomItem().getDefaultStack();
                this.equipStack(equipmentSlot, newStack);
                itemStack = newStack;
            }

            float f = this.getDropChance(equipmentSlot);
            bl = f > 1.0f;
            if (itemStack.isEmpty() || EnchantmentHelper.hasVanishingCurse(itemStack) || !allowDrops && !bl || !(Math.max(this.random.nextFloat() - (float)lootingMultiplier * 0.01f, 0.0f) < f)) continue;
            if (!bl && itemStack.isDamageable()) {
                itemStack.setDamage(itemStack.getMaxDamage() - this.random.nextInt(1 + this.random.nextInt(Math.max(itemStack.getMaxDamage() - 3, 1))));
            }
            this.dropStack(itemStack);
            this.equipStack(equipmentSlot, ItemStack.EMPTY);
        }
    }
}
