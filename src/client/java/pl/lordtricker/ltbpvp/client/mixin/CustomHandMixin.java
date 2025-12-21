package pl.lordtricker.ltbpvp.client.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lordtricker.ltbpvp.core.animation.HandAnimationLogic;
import pl.lordtricker.ltbpvp.core.animation.TransformSink;
import pl.lordtricker.ltbpvp.core.config.CoreSettings;
import pl.lordtricker.ltbpvp.core.config.CoreSettings.AnimationOffsets;
import pl.lordtricker.ltbpvp.core.enums.SwingStyle;

@Mixin(HeldItemRenderer.class)
public abstract class CustomHandMixin {

    @Inject(
            method = "renderFirstPersonItem",
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
            OrderedRenderCommandQueue renderCommandQueue,
            int light,
            CallbackInfo ci
    ) {
        if (stack.getItem() instanceof BowItem || stack.getItem() instanceof CrossbowItem || stack.getItem() instanceof TridentItem) {
            return;
        }
        if (stack.isOf(Items.FILLED_MAP) || stack.isOf(Items.MAP) || stack.isOf(Items.PAPER)) {
            return;
        }
        if (!CoreSettings.animationsEnabled) {
            return;
        }
        if (hand == Hand.MAIN_HAND && stack.isEmpty()) {
            return;
        }
        if (player.isUsingItem() && player.getActiveHand() == hand &&
                (stack.getUseAction() == UseAction.EAT || stack.getUseAction() == UseAction.DRINK)) {
            return;
        }
        ci.cancel();
        matrices.push();
        HeldItemRenderer self = (HeldItemRenderer)(Object) this;
        TransformSink sink = new MatrixTransformSink(matrices);

        if (hand == Hand.MAIN_HAND) {
            if (player.isUsingItem() && player.getActiveHand() == Hand.MAIN_HAND) {
                HandAnimationLogic.applyMainHandEat(sink, player.getItemUseTimeLeft(), stack.getMaxUseTime(player));
            } else {
                HandAnimationLogic.applyMainHandSwing(sink, swingProgress, CoreSettings.swingStyle);
                AnimationOffsets offsets = CoreSettings.styleOffsets.get(CoreSettings.swingStyle);
                if (offsets != null) {
                    matrices.translate(offsets.offsetX, offsets.offsetY, offsets.offsetZ);
                }
            }
            boolean isRight = player.getMainArm() == Arm.RIGHT;
            ItemDisplayContext mode = isRight
                    ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                    : ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
            self.renderItem(player, stack, mode, matrices, renderCommandQueue, light);

        } else if (hand == Hand.OFF_HAND) {
            if (player.isUsingItem() && player.getActiveHand() == Hand.OFF_HAND) {
                HandAnimationLogic.applyLeftHandEat(sink, player.getItemUseTimeLeft(), stack.getMaxUseTime(player));
            } else {
                HandAnimationLogic.applyLeftHandStatic(sink);
            }
            if (CoreSettings.offhandAnimationEnabled) {
                AnimationOffsets off = CoreSettings.offhandOffsets;
                matrices.translate(off.offsetX, off.offsetY, off.offsetZ);
            }
            boolean isRight = player.getMainArm() == Arm.RIGHT;
            ItemDisplayContext mode = isRight
                    ? ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                    : ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
            self.renderItem(player, stack, mode, matrices, renderCommandQueue, light);
        }
        matrices.pop();
    }

    private static final class MatrixTransformSink implements TransformSink {
        private final MatrixStack matrices;

        private MatrixTransformSink(MatrixStack matrices) {
            this.matrices = matrices;
        }

        @Override
        public void translate(double x, double y, double z) {
            matrices.translate(x, y, z);
        }

        @Override
        public void scale(float x, float y, float z) {
            matrices.scale(x, y, z);
        }

        @Override
        public void rotateX(float degrees) {
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(degrees));
        }

        @Override
        public void rotateY(float degrees) {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(degrees));
        }

        @Override
        public void rotateZ(float degrees) {
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(degrees));
        }
    }
}

