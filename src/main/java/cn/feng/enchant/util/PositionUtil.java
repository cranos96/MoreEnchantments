package cn.feng.enchant.util;

import net.minecraft.util.math.BlockPos;

/**
 * @author ChengFeng
 * @since 2024/2/25
 **/
public class PositionUtil {
    public static BlockPos randomNearbyPos(BlockPos center, int radius) {
        BlockPos result;
        int ew = RandomUtil.randomInt(5, radius);
        int ns = RandomUtil.randomInt(5, radius);
        result = RandomUtil.randomBoolean()? center.east(ew) : center.west(ew);
        result = RandomUtil.randomBoolean()? result.north(ns) : result.south(ns);
        return result;
    }
}
