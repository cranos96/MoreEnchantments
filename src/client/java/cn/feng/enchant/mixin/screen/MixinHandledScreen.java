package cn.feng.enchant.mixin.screen;

import cn.feng.enchant.MoreEnchantments;
import cn.feng.enchant.util.EnchantUtil;
import cn.feng.enchant.util.ItemUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author ChengFeng
 * @since 2024/3/16
 **/
@Mixin(HandledScreen.class)
public class MixinHandledScreen {
    @Inject(method = "onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V", at = @At("HEAD"), cancellable = true)
    private void click(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci) {
        if (slot == null ||!slot.hasStack() || !(MinecraftClient.getInstance().currentScreen instanceof InventoryScreen)) return;
        ItemStack stack = slot.getStack();
        if (!EnchantUtil.has(stack, MoreEnchantments.SOAP, 1)) return;

        int newSlot = ItemUtil.randomEmptySlot(slot.inventory);
        if (newSlot == -1) return;

        slot.inventory.setStack(newSlot, stack);
        slot.setStack(ItemStack.EMPTY);

        ci.cancel();
    }
}
