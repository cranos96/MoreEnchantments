package cn.feng.enchant.mixin.entity;

import cn.feng.enchant.MoreEnchantments;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
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
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends MixinEntity {

    @Shadow public abstract ItemStack getStackInHand(Hand hand);

    @Shadow public abstract void setHealth(float health);

    @Shadow public abstract boolean clearStatusEffects();

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Shadow public abstract void equipStack(EquipmentSlot var1, ItemStack var2);

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Shadow protected boolean jumping;

    @Shadow private int jumpingCooldown;

    @Shadow protected abstract void jump();


    @Unique
    private boolean hasAirJump() {
        for (ItemStack item : this.getArmorItems()) {
            if (item.getItem() instanceof ArmorItem armor && armor.getSlotType() == EquipmentSlot.FEET
                    && EnchantmentHelper.getLevel(MoreEnchantments.AIR_JUMP, item) > 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Infinity food
     */
    @Inject(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    private void increaseFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0) {
            stack.increment(1);
        }
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void injectScud(CallbackInfo ci) {
        for (ItemStack item : this.getArmorItems()) {
            if (item.getItem() instanceof ArmorItem armor) {
                if (armor.getSlotType() == EquipmentSlot.LEGS
                        && EnchantmentHelper.getLevel(MoreEnchantments.SCUD, item) > 0) {
                    this.jumpingCooldown = 0;
                    if (((Object) this) instanceof PlayerEntity player) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1));


                        if (player.forwardSpeed > 0) {
                            player.setSprinting(true);
                        }
                    }
                } else if (armor.getSlotType() == EquipmentSlot.FEET
                    && EnchantmentHelper.getLevel(MoreEnchantments.LIGHT_KUNGFU, item) > 0) {
                    this.fallDistance = 0;
                }
            }
        }
    }

    @Inject(method = "tickMovement", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;jumping:Z"))
    private void injectAirJump(CallbackInfo ci) {
        if (hasAirJump() && jumping && jumpingCooldown == 0) {
            this.jump();
            this.jumpingCooldown = 10;
        }
    }

    /**
     * @author ChengFeng
     * @reason Infinity totem
     */
    @Overwrite
    private boolean tryUseTotem(DamageSource source) {
        if (source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }
        ItemStack itemStack = null;
        for (Hand hand : Hand.values()) {
            ItemStack itemStack2 = this.getStackInHand(hand);
            if (!itemStack2.isOf(Items.TOTEM_OF_UNDYING)) continue;
            itemStack = itemStack2.copy();
            if (EnchantmentHelper.getLevel(Enchantments.INFINITY, itemStack2) == 0) {
                itemStack2.decrement(1);
            }
            break;
        }
        if (itemStack != null) {
            LivingEntity livingEntity = (LivingEntity) (Object) this;
            if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(Items.TOTEM_OF_UNDYING));
                Criteria.USED_TOTEM.trigger(serverPlayerEntity, itemStack);
                this.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
            }
            this.setHealth(1.0f);
            this.clearStatusEffects();
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
            this.getWorld().sendEntityStatus(livingEntity, EntityStatuses.USE_TOTEM_OF_UNDYING);
        }
        return itemStack != null;
    }
}
