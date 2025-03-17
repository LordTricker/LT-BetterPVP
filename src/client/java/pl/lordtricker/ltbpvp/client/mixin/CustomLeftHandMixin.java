package pl.lordtricker.ltbpvp.client.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lordtricker.ltbpvp.client.config.ModSettings;

@Mixin(value = HeldItemRenderer.class, priority = 500)
public abstract class CustomLeftHandMixin {

    @Inject(
            method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderLeftHand(
            AbstractClientPlayerEntity player,
            float tickDelta,
            float pitch,
            Hand hand,
            float swingProgress,
            ItemStack item,
            float equipProgress,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            CallbackInfo ci
    ) {
        if (!ModSettings.animationsEnabled) {
            return;
        }
        boolean isPhysicalLeft =
                (player.getMainArm() == Arm.LEFT && hand == Hand.MAIN_HAND)
                        || (player.getMainArm() == Arm.RIGHT && hand == Hand.OFF_HAND);
        if (!isPhysicalLeft) {
            return;
        }
        ci.cancel();

        HeldItemRenderer self = (HeldItemRenderer) (Object) this;

        if (player.isUsingItem() && player.getActiveHand() == Hand.OFF_HAND) {
            applyLeftHandEatTransform(matrices, item, player);
        } else {
            applyLeftHandStaticTransform(matrices);
        }

        self.renderItem(
                player,
                item,
                ModelTransformationMode.FIRST_PERSON_LEFT_HAND,
                true,
                matrices,
                vertexConsumers,
                light
        );
    }

    private void applyLeftHandStaticTransform(MatrixStack matrices) {
        matrices.translate(-2.0f, -0.4f, -0.9f);
        matrices.scale(0.8f, 0.8f, 0.8f);
    }

    private void applyLeftHandEatTransform(MatrixStack matrices, ItemStack item, AbstractClientPlayerEntity player) {
        float timeLeft = player.getItemUseTimeLeft();
        float maxTime = item.getMaxUseTime(player);
        float progress = 1.0F - timeLeft / maxTime;
        float sin = MathHelper.sin(progress * (float) Math.PI);
        matrices.translate(-1.7f, -0.5f, -0.8f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(40.0F));
        matrices.translate(0.0f, sin * 0.1f, 0.0f);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(sin * -20.0F));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(sin * -10.0F));
        matrices.scale(0.9f, 0.9f, 0.9f);
    }
}
