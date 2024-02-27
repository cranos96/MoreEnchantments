package cn.feng.enchant.mixin.handler;

import cn.feng.enchant.MoreEnchantments;
import cn.feng.enchant.util.EnchantUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

/**
 * @author ChengFeng
 * @since 2024/2/26
 **/
@Mixin(AnvilScreenHandler.class)
public abstract class MixinAnvilScreenHandler extends MixinForgingScreenHandler {
    @Shadow @Final private Property levelCost;

    @Shadow private int repairItemUsage;

    @Shadow private @Nullable String newItemName;

    /**
     * @author ChengFeng
     * @reason For special enchantments
     */
    @Overwrite
    public void updateResult() {
        ItemStack inputStack = this.input.getStack(0);
        this.levelCost.set(1);
        int i = 0;
        int j = 0;
        int k = 0;
        if (inputStack.isEmpty()) {
            this.output.setStack(0, ItemStack.EMPTY);
            this.levelCost.set(0);
            return;
        }
        ItemStack inputCopy = inputStack.copy();
        ItemStack inputStack2 = this.input.getStack(1);
        Map<Enchantment, Integer> inputItemEnchantments = EnchantmentHelper.get(inputCopy);
        j += inputStack.getRepairCost() + (inputStack2.isEmpty() ? 0 : inputStack2.getRepairCost());
        this.repairItemUsage = 0;
        if (!inputStack2.isEmpty()) {
            boolean bl = inputStack2.isOf(Items.ENCHANTED_BOOK) && !EnchantedBookItem.getEnchantmentNbt(inputStack2).isEmpty();
            if (inputCopy.isDamageable() && inputCopy.getItem().canRepair(inputStack, inputStack2)) {
                int m;
                int l = Math.min(inputCopy.getDamage(), inputCopy.getMaxDamage() / 4);
                if (l <= 0) {
                    this.output.setStack(0, ItemStack.EMPTY);
                    this.levelCost.set(0);
                    return;
                }
                for (m = 0; l > 0 && m < inputStack2.getCount(); ++m) {
                    int n = inputCopy.getDamage() - l;
                    inputCopy.setDamage(n);
                    ++i;
                    l = Math.min(inputCopy.getDamage(), inputCopy.getMaxDamage() / 4);
                }
                this.repairItemUsage = m;
            } else {
                if (!(bl || inputCopy.isOf(inputStack2.getItem()) && inputCopy.isDamageable())) {
                    this.output.setStack(0, ItemStack.EMPTY);
                    this.levelCost.set(0);
                    return;
                }
                if (inputCopy.isDamageable() && !bl) {
                    int l = inputStack.getMaxDamage() - inputStack.getDamage();
                    int m = inputStack2.getMaxDamage() - inputStack2.getDamage();
                    int n = m + inputCopy.getMaxDamage() * 12 / 100;
                    int o = l + n;
                    int p = inputCopy.getMaxDamage() - o;
                    if (p < 0) {
                        p = 0;
                    }
                    if (p < inputCopy.getDamage()) {
                        inputCopy.setDamage(p);
                        i += 2;
                    }
                }
                Map<Enchantment, Integer> map2 = EnchantmentHelper.get(inputStack2);
                boolean bl22 = false;
                boolean bl3 = false;
                for (Enchantment newEnchantment : map2.keySet()) {
                    int r;
                    if (newEnchantment == null) continue;
                    int q = inputItemEnchantments.getOrDefault(newEnchantment, 0);
                    r = q == (r = map2.get(newEnchantment)) ? r + 1 : Math.max(r, q);
                    boolean acceptable = newEnchantment.isAcceptableItem(inputStack);
                    if (this.player.getAbilities().creativeMode || inputStack.isOf(Items.ENCHANTED_BOOK)) {
                        acceptable = true;
                    }
                    boolean schrodinger = false;
                    Enchantment toRemove = null;
                    for (Enchantment original : inputItemEnchantments.keySet()) {
                        boolean s = EnchantUtil.canSchrodinger(original, newEnchantment);
                        if (s) {
                            schrodinger = true;
                            toRemove = original;
                        }
                        if (original == newEnchantment || newEnchantment.canCombine(original) || s) continue;
                        acceptable = false;
                        ++i;
                    }
                    if (!acceptable) {
                        bl3 = true;
                        continue;
                    }
                    bl22 = true;
                    if (r > newEnchantment.getMaxLevel()) {
                        r = newEnchantment.getMaxLevel();
                    }
                    if (schrodinger) {
                        inputItemEnchantments.remove(toRemove);
                        inputItemEnchantments.put(MoreEnchantments.SCHRODINGER_CURSE, 1);
                    } else {
                        inputItemEnchantments.put(newEnchantment, r);
                    }
                    int s = 0;
                    switch (newEnchantment.getRarity()) {
                        case COMMON: {
                            s = 1;
                            break;
                        }
                        case UNCOMMON: {
                            s = 2;
                            break;
                        }
                        case RARE: {
                            s = 4;
                            break;
                        }
                        case VERY_RARE: {
                            s = 8;
                        }
                    }
                    if (bl) {
                        s = Math.max(1, s / 2);
                    }
                    i += s * r;
                    if (inputStack.getCount() <= 1) continue;
                    i = 40;
                }
                if (bl3 && !bl22) {
                    this.output.setStack(0, ItemStack.EMPTY);
                    this.levelCost.set(0);
                    return;
                }
            }
        }
        if (this.newItemName == null || Util.isBlank(this.newItemName)) {
            if (inputStack.hasCustomName()) {
                k = 1;
                i += k;
                inputCopy.removeCustomName();
            }
        } else if (!this.newItemName.equals(inputStack.getName().getString())) {
            k = 1;
            i += k;
            inputCopy.setCustomName(Text.literal(this.newItemName));
        }
        this.levelCost.set(j + i);
        if (i <= 0) {
            inputCopy = ItemStack.EMPTY;
        }
        if (k == i && k > 0 && this.levelCost.get() >= 40) {
            this.levelCost.set(39);
        }
        if (this.levelCost.get() >= 40 && !this.player.getAbilities().creativeMode) {
            inputCopy = ItemStack.EMPTY;
        }
        if (!inputCopy.isEmpty()) {
            int t = inputCopy.getRepairCost();
            if (!inputStack2.isEmpty() && t < inputStack2.getRepairCost()) {
                t = inputStack2.getRepairCost();
            }
            if (k != i || k == 0) {
                t = AnvilScreenHandler.getNextCost(t);
            }
            inputCopy.setRepairCost(t);
            EnchantmentHelper.set(inputItemEnchantments, inputCopy);
        }
        this.output.setStack(0, inputCopy);
        this.sendContentUpdates();
    }
}
