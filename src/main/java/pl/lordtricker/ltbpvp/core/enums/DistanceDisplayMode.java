package pl.lordtricker.ltbpvp.core.enums;

public enum DistanceDisplayMode {
    ENTITY_ONLY("Entity"),
    ALL("All");

    private final String label;

    DistanceDisplayMode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public DistanceDisplayMode next() {
        DistanceDisplayMode[] values = values();
        return values[(ordinal() + 1) % values.length];
    }
}
