package cn.feng.enchant.mixin.render;

import cn.feng.enchant.MoreEnchantments;
import cn.feng.enchant.util.EnchantUtil;
import cn.feng.enchant.util.EntityUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.*;

import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/3/3
 **/
@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>>
        extends MixinEntityRenderer<T>
        implements FeatureRendererContext<T, M> {
    @Shadow
    protected M model;
    @Shadow
    @Final
    protected List<FeatureRenderer<T, M>> features;

    @Shadow
    protected abstract float getHandSwingProgress(T entity, float tickDelta);

    @Shadow
    protected abstract boolean isVisible(T entity);

    @Shadow
    protected abstract @Nullable RenderLayer getRenderLayer(T entity, boolean showBody, boolean translucent, boolean showOutline);

    @Shadow
    protected abstract float getAnimationCounter(T entity, float tickDelta);

    @Shadow
    protected abstract float getAnimationProgress(T entity, float tickDelta);

    @Shadow
    protected abstract void setupTransforms(T entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta);

    @Shadow
    protected abstract void scale(T entity, MatrixStack matrices, float amount);

    @Shadow
    protected abstract boolean hasLabel(T livingEntity);

    @Unique
    private void renderAge(T entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        double d = this.dispatcher.getSquaredDistanceToCamera(entity);
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (d > 128.0 || !(entity instanceof LivingEntity) || player == null || !EnchantUtil.shouldRenderAge(player)) {
            return;
        }
        MutableText ageText = Text.translatable("entity.age", EntityUtil.getAge(entity));
        float f = entity.getNameLabelHeight() + 0.3f;
        matrices.push();
        matrices.translate(0.0f, f, 0.0f);
        matrices.multiply(this.dispatcher.getRotation());
        matrices.scale(-0.025f, -0.025f, 0.025f);
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f);
        int j = (int) (g * 255.0f) << 24;
        TextRenderer textRenderer = this.getTextRenderer();
        float h = -textRenderer.getWidth(ageText) / 2f;
        textRenderer.draw(ageText, h, 0, 0x20FFFFFF, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, j, light);
        textRenderer.draw(ageText, h, 0, Colors.LIGHT_YELLOW, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
        matrices.pop();
    }

    /**
     * @author ChengFeng
     * @reason Baby
     */
    @Overwrite
    public void render(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        float n;
        Direction direction;
        Entity entity;
        matrixStack.push();
        this.model.handSwingProgress = this.getHandSwingProgress(livingEntity, g);
        this.model.riding = livingEntity.hasVehicle();
        this.model.child = EntityUtil.shouldBeBaby(livingEntity);
        float h = MathHelper.lerpAngleDegrees(g, livingEntity.prevBodyYaw, livingEntity.bodyYaw);
        float j = MathHelper.lerpAngleDegrees(g, livingEntity.prevHeadYaw, livingEntity.headYaw);
        float k = j - h;
        if (livingEntity.hasVehicle() && (entity = livingEntity.getVehicle()) instanceof LivingEntity) {
            LivingEntity livingEntity2 = (LivingEntity) entity;
            h = MathHelper.lerpAngleDegrees(g, livingEntity2.prevBodyYaw, livingEntity2.bodyYaw);
            k = j - h;
            float l = MathHelper.wrapDegrees(k);
            if (l < -85.0f) {
                l = -85.0f;
            }
            if (l >= 85.0f) {
                l = 85.0f;
            }
            h = j - l;
            if (l * l > 2500.0f) {
                h += l * 0.2f;
            }
            k = j - h;
        }
        float m = MathHelper.lerp(g, livingEntity.prevPitch, livingEntity.getPitch());
        if (LivingEntityRenderer.shouldFlipUpsideDown(livingEntity)) {
            m *= -1.0f;
            k *= -1.0f;
        }
        if (livingEntity.isInPose(EntityPose.SLEEPING) && (direction = livingEntity.getSleepingDirection()) != null) {
            n = livingEntity.getEyeHeight(EntityPose.STANDING) - 0.1f;
            matrixStack.translate((float) (-direction.getOffsetX()) * n, 0.0f, (float) (-direction.getOffsetZ()) * n);
        }
        float l = this.getAnimationProgress(livingEntity, g);
        this.setupTransforms(livingEntity, matrixStack, l, h, g);
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        this.scale(livingEntity, matrixStack, g);
        matrixStack.translate(0.0f, -1.501f, 0.0f);
        n = 0.0f;
        float o = 0.0f;
        if (!livingEntity.hasVehicle() && livingEntity.isAlive()) {
            n = livingEntity.limbAnimator.getSpeed(g);
            o = livingEntity.limbAnimator.getPos(g);
            if (EntityUtil.shouldBeBaby(livingEntity)) {
                o *= 3.0f;
            }
            if (n > 1.0f) {
                n = 1.0f;
            }
        }
        this.model.animateModel(livingEntity, o, n, g);
        this.model.setAngles(livingEntity, o, n, l, k, m);
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        boolean bl = this.isVisible(livingEntity);
        boolean bl2 = !bl && !livingEntity.isInvisibleTo(minecraftClient.player);
        boolean bl3 = minecraftClient.hasOutline(livingEntity);
        RenderLayer renderLayer = this.getRenderLayer(livingEntity, bl, bl2, bl3);
        if (renderLayer != null) {
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
            int p = LivingEntityRenderer.getOverlay(livingEntity, this.getAnimationCounter(livingEntity, g));
            this.model.render(matrixStack, vertexConsumer, light, p, 1.0f, 1.0f, 1.0f, bl2 ? 0.15f : 1.0f);
        }
        if (!livingEntity.isSpectator()) {
            for (FeatureRenderer<T, M> featureRenderer : this.features) {
                featureRenderer.render(matrixStack, vertexConsumerProvider, light, livingEntity, o, n, g, l, k, m);
            }
        }
        matrixStack.pop();
        renderAge(livingEntity, matrixStack, vertexConsumerProvider, light);
        renderLabel(livingEntity, f, matrixStack, vertexConsumerProvider, light);
    }

    @Unique
    private void renderLabel(T entity, float yaw, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (!this.hasLabel(entity)) {
            return;
        }
        this.renderLabelIfPresent(entity, entity.getDisplayName(), matrices, vertexConsumers, light);
    }
}
