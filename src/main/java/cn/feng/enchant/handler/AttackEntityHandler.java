package cn.feng.enchant.handler;

import cn.feng.enchant.MoreEnchantments;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
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

        if (entity instanceof LivingEntity livingEntity) {
            Hand activeHand = player.getActiveHand();
            ItemStack item = player.getStackInHand(activeHand);
            if (!item.isEmpty() && EnchantmentHelper.getLevel(MoreEnchantments.HOT_POTATO, item) > 0) {
                //TODO: 对象手里拿不到东西
                Hand targetHand = livingEntity.getActiveHand();
                ItemStack targetItem = livingEntity.getStackInHand(targetHand);
                if (!targetItem.isEmpty()) {
                    livingEntity.dropStack(targetItem);
                }
                livingEntity.setStackInHand(targetHand, item.copy());
                player.setStackInHand(activeHand, ItemStack.EMPTY);
            }
        }

        return ActionResult.PASS;
    }
}
