package pl.lordtricker.ltbpvp.client.enums;

import net.minecraft.util.Identifier;

public enum TargetStyle {
    SHURIKEN("ltbpvp", "textures/target/cross_shuriken.png"),
    SHURIKEN_EXPAND("ltbpvp", "textures/target/cross_shuriken_2.png");

    private final Identifier texture;

    TargetStyle(String namespace, String path) {
        this.texture = Identifier.of(namespace, path);
    }

    public Identifier getTexture() {
        return texture;
    }
}
