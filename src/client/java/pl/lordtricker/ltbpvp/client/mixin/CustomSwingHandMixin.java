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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lordtricker.ltbpvp.client.config.ModSettings;
import pl.lordtricker.ltbpvp.client.enums.SwingStyle;

@Mixin(HeldItemRenderer.class)
public abstract class CustomSwingHandMixin {

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
        ci.cancel();

        SwingStyle style = ModSettings.swingStyle;

        Arm arm = (hand == Hand.MAIN_HAND)
                ? player.getMainArm()
                : player.getMainArm().getOpposite();

        applySwingTransformation(style, matrices, swingProgress, arm);

        ModSettings.AnimationOffsets offsets = ModSettings.styleOffsets.get(style);
        if (offsets != null) {
            float sideFactor = (arm == Arm.RIGHT) ? 1.0F : -1.0F;
            matrices.translate(sideFactor * offsets.offsetX, offsets.offsetY, offsets.offsetZ);
        }

        HeldItemRenderer self = (HeldItemRenderer)(Object)this;
        boolean isRightArm = (arm == Arm.RIGHT);
        self.renderItem(
                player,
                item,
                isRightArm
                        ? ModelTransformationMode.FIRST_PERSON_RIGHT_HAND
                        : ModelTransformationMode.FIRST_PERSON_LEFT_HAND,
                !isRightArm,
                matrices,
                vertexConsumers,
                light
        );
    }

    @Unique
    private void applySwingTransformation(SwingStyle style, MatrixStack matrices, float swingProgress, Arm arm) {
        float side = (arm == Arm.RIGHT) ? 1.0F : -1.0F;
        float swingRadians = swingProgress * (float)Math.PI;
        float swingSin = MathHelper.sin(swingRadians);

        switch (style) {
            case BASIC_SWING -> {
                matrices.translate(side * 0.4, -0.25, -0.6);
                matrices.scale(0.50F, 0.50F, 0.50F);

                float angleX = -95.0F * swingSin;
                float angleY = side * 35.0F * swingSin;
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(angleX));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angleY));
            }
            case BASIC_CLAP -> {
                matrices.translate(side * 0.4, -0.25, -0.6);
                matrices.scale(0.50F, 0.50F, 0.50F);

                float angleX = -85.0F * swingSin;
                float angleY = side * -110.0F * swingSin;
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(angleX));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angleY));
            }
            case SWIPE_IN -> {
                matrices.translate(side * 1.3, -0.7, -2.6);
                matrices.scale(1.4F, 1.4F, 1.4F);

                float angleY = -60.0F - side * 60.0F * swingSin;
                float angleZ = 75.0F + side * -0.3F * swingSin * 10f;
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angleY));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angleZ));
            }
            case SWIPE_OUT -> {
                matrices.translate(side * 1.3, -0.7, -2.6);
                matrices.scale(1.4F, 1.4F, 1.4F);

                float angleY = -60.0F + side * 60.0F * swingSin;
                float angleZ = 75.0F + side * 2F * swingSin * 10f;
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angleY));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angleZ));
            }
        }
    }
}
