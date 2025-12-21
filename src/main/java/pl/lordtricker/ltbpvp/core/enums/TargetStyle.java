package pl.lordtricker.ltbpvp.core.enums;

public enum TargetStyle {
    CIRCLE_DASHED("ltbpvp", "textures/target/cross_circle_dashed.png"),
    CIRCLE_FULL("ltbpvp", "textures/target/cross_circle_full.png"),
    CIRCLE_GAP("ltbpvp", "textures/target/cross_circle_gap.png"),
    RHOMBUS_CORNER("ltbpvp", "textures/target/cross_rhombus_corner.png"),
    RHOMBUS_FULL("ltbpvp", "textures/target/cross_rhombus_full.png"),
    SIDE("ltbpvp", "textures/target/cross_side.png"),
    SIDE_SHARP("ltbpvp", "textures/target/cross_side_sharp.png"),
    SQUARE_ADDED("ltbpvp", "textures/target/cross_square_added.png"),
    SQUARE_CORNER1("ltbpvp", "textures/target/cross_square_corner1.png"),
    SQUARE_CORNER1_1("ltbpvp", "textures/target/cross_square_corner1-1.png"),
    SQUARE_DASHED("ltbpvp", "textures/target/cross_square_dashed.png"),
    SQUARE_THRU("ltbpvp", "textures/target/cross_square_thru.png"),
    TRIANGLE_CORNER("ltbpvp", "textures/target/cross_triangle_corner.png"),
    TRIANGLE_FULL("ltbpvp", "textures/target/cross_triangle_full.png"),
    X("ltbpvp", "textures/target/cross_x.png"),
    X_OUTLINE("ltbpvp", "textures/target/cross_x_outline.png");

    private final String namespace;
    private final String path;

    TargetStyle(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getPath() {
        return path;
    }
}
