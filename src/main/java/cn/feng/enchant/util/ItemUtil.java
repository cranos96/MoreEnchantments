package cn.feng.enchant.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/2/26
 **/
public class ItemUtil {
    private static final List<Item> allItems = new ArrayList<>();

    public static void init() {
        Arrays.stream(Items.class.getDeclaredFields()).filter(field -> field.getType() == Item.class).forEach(it -> {
            try {
                allItems.add((Item) it.get(null));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static int randomEmptySlot(Inventory inventory) {
        List<Integer> slots = new ArrayList<>();

        for (int i = 0; i < 36; i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isEmpty()) slots.add(i);
        }

        if (slots.isEmpty()) return -1;

        return slots.get(RandomUtil.randomInt(0, slots.size() -1));
    }

    public static Item randomItem() {
        return allItems.get(RandomUtil.randomInt(0, allItems.size() - 1));
    }
}
