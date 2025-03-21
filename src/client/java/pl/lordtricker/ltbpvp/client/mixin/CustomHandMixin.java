package pl.lordtricker.ltbpvp.client.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lordtricker.ltbpvp.client.config.ModSettings;
import pl.lordtricker.ltbpvp.client.config.ModSettings.AnimationOffsets;
import pl.lordtricker.ltbpvp.client.enums.SwingStyle;

@Mixin(HeldItemRenderer.class)
public abstract class CustomHandMixin {

    @Inject(
            method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderFirstPersonItem(
            AbstractClientPlayerEntity player,
            float tickDelta,
            float pitch,
            Hand hand,
            float swingProgress,
            ItemStack stack,
            float equipProgress,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            CallbackInfo ci
    ) {

        if (stack.getItem() instanceof BowItem || stack.getItem() instanceof CrossbowItem || stack.getItem() instanceof TridentItem) {
            return;
        }

        if (!ModSettings.animationsEnabled) {
            return;
        }
        ci.cancel();

        matrices.push();

        HeldItemRenderer self = (HeldItemRenderer)(Object) this;

        if (hand == Hand.MAIN_HAND) {
            if (player.isUsingItem() && player.getActiveHand() == Hand.MAIN_HAND) {
                applyMainHandEatTransform(matrices, stack, player);
            } else {
                applyCustomMainHandSwing(matrices, swingProgress, ModSettings.swingStyle);
                AnimationOffsets offsets = ModSettings.styleOffsets.get(ModSettings.swingStyle);
                if (offsets != null) {
                    matrices.translate(offsets.offsetX, offsets.offsetY, offsets.offsetZ);
                }
            }
            boolean isRightDominant = player.getMainArm() == Arm.RIGHT;
            ModelTransformation.Mode mode = isRightDominant
                    ? ModelTransformation.Mode.FIRST_PERSON_RIGHT_HAND
                    : ModelTransformation.Mode.FIRST_PERSON_LEFT_HAND;
            self.renderItem(player, stack, mode, !isRightDominant, matrices, vertexConsumers, light);
        } else if (hand == Hand.OFF_HAND) {
            if (player.isUsingItem() && player.getActiveHand() == Hand.OFF_HAND) {
                applyLeftHandEatTransform(matrices, stack, player);
            } else {
                applyLeftHandStaticTransform(matrices);
            }
            boolean isRightDominant = player.getMainArm() == Arm.RIGHT;
            ModelTransformation.Mode mode = isRightDominant
                    ? ModelTransformation.Mode.FIRST_PERSON_LEFT_HAND
                    : ModelTransformation.Mode.FIRST_PERSON_RIGHT_HAND;
            self.renderItem(player, stack, mode, isRightDominant, matrices, vertexConsumers, light);
        }

        matrices.pop();
    }

    private void applyCustomMainHandSwing(MatrixStack matrices, float swingProgress, SwingStyle style) {
        float rad = swingProgress * (float) Math.PI;
        float sin = MathHelper.sin(rad);
        switch (style) {
            case BASIC_SWING -> {
                matrices.translate(0.4, -0.25, -0.6);
                matrices.scale(0.50F, 0.50F, 0.50F);
                float angleX = -95.0F * sin;
                float angleY = 35.0F * sin;
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(angleX));
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(angleY));
            }
            case BASIC_CLAP -> {
                matrices.translate(0.4, -0.25, -0.6);
                matrices.scale(0.50F, 0.50F, 0.50F);
                float angleX = -85.0F * sin;
                float angleY = -110.0F * sin;
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(angleX));
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(angleY));
            }
            case SWIPE_IN -> {
                matrices.translate(1.3, -0.7, -2.6);
                matrices.scale(1.4F, 1.4F, 1.4F);
                float angleY = -60.0F - 60.0F * sin;
                float angleZ = 75.0F - 0.3F * sin * 10f;
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(angleY));
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(angleZ));
            }
            case SWIPE_OUT -> {
                matrices.translate(1.3, -0.7, -2.6);
                matrices.scale(1.4F, 1.4F, 1.4F);
                float angleY = -60.0F + 60.0F * sin;
                float angleZ = 75.0F + 2F * sin * 10f;
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(angleY));
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(angleZ));
            }
            case NO_SWING -> {
                matrices.translate(0.4, -0.25, -0.6);
                matrices.scale(0.50F, 0.50F, 0.50F);
            }
        }
    }

    private void applyMainHandEatTransform(MatrixStack matrices, ItemStack item, AbstractClientPlayerEntity player) {
        float timeLeft = player.getItemUseTimeLeft();
        float maxTime = item.getMaxUseTime();
        float progress = 1.0F - timeLeft / maxTime;
        float speedFactor = 1.2F;
        float adjustedProgress = Math.min(progress * speedFactor, 1.0F);
        float sin = MathHelper.sin(adjustedProgress * (float) Math.PI);

        matrices.translate(0.56f, -0.5f, -0.8f);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-40.0F));
        matrices.translate(0.0f, sin * 0.1f, 0.0f);
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(sin * 20.0F));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(sin * 10.0F));
        matrices.scale(0.7f, 0.7f, 0.7f);
    }

    private void applyLeftHandStaticTransform(MatrixStack matrices) {
        matrices.translate(-0.62f, -0.5f, -0.95f);
        matrices.scale(0.7f, 0.7f, 0.7f);
    }

    private void applyLeftHandEatTransform(MatrixStack matrices, ItemStack item, AbstractClientPlayerEntity player) {
        float timeLeft = player.getItemUseTimeLeft();
        float maxTime = item.getMaxUseTime();
        float progress = 1.0F - timeLeft / maxTime;
        float speedFactor = 1.5F;
        float adjustedProgress = Math.min(progress * speedFactor, 1.0F);
        float sin = MathHelper.sin(adjustedProgress * (float) Math.PI);

        matrices.translate(-0.62f, -0.6f, -0.8f);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(40.0F));
        matrices.translate(0.0f, sin * 0.07f, 0.0f);
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(sin * -15.0F));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(sin * -7.0F));
        matrices.scale(0.7f, 0.7f, 0.7f);
    }
}
