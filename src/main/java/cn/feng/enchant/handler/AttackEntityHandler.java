package cn.feng.enchant.handler;

import cn.feng.enchant.MoreEnchantments;
import cn.feng.enchant.util.EnchantUtil;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * @author ChengFeng
 * @since 2024/2/25
 **/
public class AttackEntityHandler implements AttackEntityCallback {
    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {

        if (entity instanceof ChickenEntity && !world.isClient()) {
            player.sendMessage(Text.literal("你干嘛~哈哈呦~"));
        }

        if (entity instanceof LivingEntity target) {
            Hand activeHand = player.getActiveHand();
            ItemStack item = player.getStackInHand(activeHand);
            if (!item.isEmpty() && EnchantUtil.has(item, MoreEnchantments.HOT_POTATO, 1)) {
                if (item.getItem() instanceof ArmorItem armor) {
                    switch (armor.getType()) {
                        case HELMET -> {
                            target.equipStack(EquipmentSlot.HEAD, item.copy());
                        }

                        case CHESTPLATE -> {
                            target.equipStack(EquipmentSlot.CHEST, item.copy());
                        }

                        case LEGGINGS -> {
                            target.equipStack(EquipmentSlot.LEGS, item.copy());
                        }

                        case BOOTS -> {
                            target.equipStack(EquipmentSlot.FEET, item.copy());
                        }
                    }
                } else {
                    Hand targetHand = target.getActiveHand();
                    ItemStack targetItem = target.getStackInHand(targetHand);
                    if (!targetItem.isEmpty()) {
                        target.dropStack(targetItem);
                    }
                    target.setStackInHand(targetHand, item.copy());
                }
                player.setStackInHand(activeHand, ItemStack.EMPTY);
            }
        }

        return ActionResult.PASS;
    }
}
