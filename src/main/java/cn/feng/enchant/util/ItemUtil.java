package cn.feng.enchant.util;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

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

    public static Item randomItem() {
        return allItems.get(RandomUtil.randomInt(0, allItems.size() - 1));
    }
}
