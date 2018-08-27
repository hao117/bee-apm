package net.beeapm.server.core.store;

public interface IStore {
    void init();
    void save(Object ... stream);
}
