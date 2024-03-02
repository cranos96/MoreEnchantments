package cn.feng.enchant.mixin.entity;

import cn.feng.enchant.MoreEnchantments;
import cn.feng.enchant.util.EnchantUtil;
import cn.feng.enchant.util.ItemUtil;
import cn.feng.enchant.util.TimerUtil;
import cn.feng.enchant.util.WorldUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author ChengFeng
 * @since 2024/2/25
 **/
@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends MixinLivingEntity {
    @Shadow
    @Final
    private PlayerInventory inventory;
    @Unique
    private TimerUtil timerUtil = new TimerUtil();

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (!timerUtil.hasDelayed(100)) return;
        WorldUtil.armorLightning((PlayerEntity) (Object) this);
        timerUtil.reset();
    }

    /**
     * @author ChengFeng
     * @reason Schrodinger Curse
     */
    @Overwrite
    public void dropInventory() {
        if (!this.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
            vanishCursedItems();
            this.inventory.dropAll();
        }
    }

    @Unique
    private void vanishCursedItems() {
        for (int i = 0; i < this.inventory.size(); ++i) {
            ItemStack itemStack = this.inventory.getStack(i);
            if (itemStack.isEmpty()) continue;
            if (EnchantmentHelper.hasVanishingCurse(itemStack))
                this.inventory.removeStack(i);
            if (EnchantUtil.has(itemStack, MoreEnchantments.SCHRODINGER_CURSE, 1)) {
                this.inventory.setStack(i, ItemUtil.randomItem().getDefaultStack());
            }
        }
    }
}
