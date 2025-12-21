package pl.lordtricker.ltbpvp.core.animation;

public interface TransformSink {
    void translate(double x, double y, double z);
    void scale(float x, float y, float z);
    void rotateX(float degrees);
    void rotateY(float degrees);
    void rotateZ(float degrees);
}
