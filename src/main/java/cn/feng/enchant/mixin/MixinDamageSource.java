package cn.feng.enchant.mixin;

import cn.feng.enchant.MoreEnchantments;
import cn.feng.enchant.util.EnchantUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ChengFeng
 * @since 2024/3/9
 **/
@Mixin(DamageSource.class)
public abstract class MixinDamageSource {

    @Unique
    private static final Pattern pattern = Pattern.compile("(?<!%)%(?:([123])\\$)?s");

    @Inject(at = @At("HEAD"), method = "getDeathMessage(Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/text/Text;", cancellable = true)
    public void getDeathMessage(LivingEntity target, CallbackInfoReturnable<Text> rtrn) {
        Entity attacker = target.getPrimeAdversary();
        if (attacker == null) return;

        Iterator<ItemStack> iter = attacker.getHandItems().iterator();
        ItemStack held;
        if (iter.hasNext()) {
            held = iter.next();
        } else {
            held = ItemStack.EMPTY;
        }

        String msg;

        if (EnchantUtil.has(held, MoreEnchantments.BABY, 1)) {
            msg = Text.translatable("death.attack.tooYoung").getString();
        } else if (EnchantUtil.has(held, MoreEnchantments.LIFE_SHORTENING, 1)) {
            msg = Text.translatable("death.attack.tooOld").getString();
        } else return;


        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            matcher.reset(msg);
            MutableText base = Text.empty();
            int prev = 0;
            int defIdx = 0;
            while (matcher.find()) {
                if (prev < msg.length()) {
                    base.append(msg.substring(prev, matcher.start()));
                }
                int idx = defIdx;
                if (matcher.group(1) != null) {
                    idx = Integer.parseInt(matcher.group(1)) - 1;
                } else {
                    defIdx++;
                }
                if (idx == 0) {
                    base.append(target.getDisplayName());
                } else if (idx == 1) {
                    base.append(attacker.getDisplayName());
                } else if (idx == 2) {
                    base.append(held.toHoverableText());
                } else {
                    base.append(matcher.group());
                }
                prev = matcher.end();
            }
            if (prev < msg.length()) {
                base.append(msg.substring(prev));
            }
            rtrn.setReturnValue(base);
        } else {
            rtrn.setReturnValue(Text.literal(msg));
        }
    }
}
