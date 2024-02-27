package cn.feng.enchant.mixin.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ForgingScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author ChengFeng
 * @since 2024/2/26
 **/
@Mixin(ForgingScreenHandler.class)
public abstract class MixinForgingScreenHandler extends MixinScreenHandler {

    @Shadow @Final protected PlayerEntity player;
    @Shadow @Final protected CraftingResultInventory output;
    @Shadow @Final protected Inventory input;
}
