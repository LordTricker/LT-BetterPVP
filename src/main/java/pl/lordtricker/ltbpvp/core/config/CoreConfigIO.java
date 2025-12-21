package pl.lordtricker.ltbpvp.core.config;

public interface CoreConfigIO {
    CoreConfig load();
    void save(CoreConfig config);
}

