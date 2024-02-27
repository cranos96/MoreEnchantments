package cn.feng.enchant.mixin.entity;

import cn.feng.enchant.MoreEnchantments;
import cn.feng.enchant.util.ItemUtil;
import cn.feng.enchant.util.TimerUtil;
import cn.feng.enchant.util.WorldUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author ChengFeng
 * @since 2024/2/25
 **/
@Mixin(AbstractHorseEntity.class)
public abstract class MixinAbstractHorseEntity extends MixinAnimalEntity {
    @Shadow
    protected SimpleInventory items;

    @Shadow
    public abstract @Nullable LivingEntity getControllingPassenger();
    @Unique
    private TimerUtil timerUtil = new TimerUtil();

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        LivingEntity passenger = this.getControllingPassenger();
        ItemStack armor = this.items.getStack(1);
        if (passenger == null) return;

        if (EnchantmentHelper.getLevel(MoreEnchantments.LIGHTNING_GOD, armor) > 0) {
            if (!timerUtil.hasDelayed(100)) return;
            WorldUtil.summonLightningNearby(passenger.getBlockPos(), passenger);
        }
    }

    /**
     * @author ChengFeng
     * @reason Schrodinger Curse
     */
    @Overwrite
    public void dropInventory() {
        if (this.items == null) {
            return;
        }
        for (int i = 0; i < this.items.size(); ++i) {
            ItemStack itemStack = this.items.getStack(i);
            if (itemStack.isEmpty()) continue;
            if (EnchantmentHelper.hasVanishingCurse(itemStack)) continue;
            if (EnchantmentHelper.getLevel(MoreEnchantments.SCHRODINGER_CURSE, itemStack) > 0) {
                ItemStack newStack = ItemUtil.randomItem().getDefaultStack();
                this.items.setStack(i, newStack);
                itemStack = newStack;
            }
            this.dropStack(itemStack);
        }
    }
}
