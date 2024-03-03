package cn.feng.enchant.mixin.render;

import cn.feng.enchant.util.EntityUtil;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author ChengFeng
 * @since 2024/3/3
 **/
@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher {
    @Unique
    private static LivingEntity entity;
    @Inject(method = "renderHitbox", at = @At("HEAD"))
    private static void getEntity(MatrixStack matrices, VertexConsumer vertices, Entity entity, float tickDelta, CallbackInfo ci) {
        if (entity instanceof LivingEntity livingEntity) MixinEntityRenderDispatcher.entity = livingEntity;
    }

    @ModifyArg(method = "renderHitbox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawBox(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/math/Box;FFFF)V", ordinal = 0), index = 2, require = 1, allow = 1)
    private static Box updateBoundingBox(Box box) {
        return EntityUtil.shouldBeBaby(entity)? box.contract(0.5) : box;
    }
}
