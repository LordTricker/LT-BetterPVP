package pl.lordtricker.ltbpvp.core.animation;

import pl.lordtricker.ltbpvp.core.enums.SwingStyle;

public final class HandAnimationLogic {
    private HandAnimationLogic() {}

    public static void applyMainHandSwing(TransformSink sink, float swingProgress, SwingStyle style) {
        float rad = swingProgress * (float) Math.PI;
        float sin = (float) Math.sin(rad);
        switch (style) {
            case BASIC_SWING -> {
                sink.translate(0.4, -0.25, -0.6);
                sink.scale(0.50F, 0.50F, 0.50F);
                sink.rotateX(-95.0F * sin);
                sink.rotateY(35.0F * sin);
            }
            case BASIC_CLAP -> {
                sink.translate(0.4, -0.25, -0.6);
                sink.scale(0.50F, 0.50F, 0.50F);
                sink.rotateX(-85.0F * sin);
                sink.rotateY(-110.0F * sin);
            }
            case SWIPE_IN -> {
                sink.translate(1.3, -0.7, -2.6);
                sink.scale(1.4F, 1.4F, 1.4F);
                sink.rotateY(-60.0F - 60.0F * sin);
                sink.rotateZ(75.0F - 3.0F * sin);
            }
            case SWIPE_OUT -> {
                sink.translate(1.3, -0.7, -2.6);
                sink.scale(1.4F, 1.4F, 1.4F);
                sink.rotateY(-60.0F + 60.0F * sin);
                sink.rotateZ(75.0F + 20.0F * sin);
            }
            case NO_SWING -> {
                sink.translate(0.4, -0.25, -0.6);
                sink.scale(0.50F, 0.50F, 0.50F);
            }
        }
    }

    public static void applyMainHandEat(TransformSink sink, float timeLeft, float maxTime) {
        float progress = 1.0F - timeLeft / maxTime;
        float sin = (float) Math.sin(Math.min(progress * 1.2F, 1.0F) * (float) Math.PI);
        sink.translate(0.56f, -0.5f, -0.8f);
        sink.rotateY(-40.0F);
        sink.translate(0.0f, sin * 0.1f, 0.0f);
        sink.rotateZ(sin * 20.0F);
        sink.rotateX(sin * 10.0F);
        sink.scale(0.7f, 0.7f, 0.7f);
    }

    public static void applyLeftHandStatic(TransformSink sink) {
        sink.translate(-0.62f, -0.5f, -0.95f);
        sink.scale(0.7f, 0.7f, 0.7f);
    }

    public static void applyLeftHandEat(TransformSink sink, float timeLeft, float maxTime) {
        float sin = (float) Math.sin(Math.min((1.0F - timeLeft / maxTime) * 1.5F, 1.0F) * (float) Math.PI);
        sink.translate(-0.62f, -0.6f, -0.8f);
        sink.rotateY(40.0F);
        sink.translate(0.0f, sin * 0.07f, 0.0f);
        sink.rotateZ(-15.0F * sin);
        sink.rotateX(-7.0F * sin);
        sink.scale(0.7f, 0.7f, 0.7f);
    }
}
