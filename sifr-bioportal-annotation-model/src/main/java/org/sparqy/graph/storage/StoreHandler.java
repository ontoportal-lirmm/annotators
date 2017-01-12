package org.sparqy.graph.storage;


import org.sparqy.api.graph.store.Store;

/**
 * A static register class for a triple store
 */
public final class StoreHandler {

    public static boolean DEBUG_ON = false;
    private static Store store;

    private StoreHandler() {
    }

    @SuppressWarnings("all")
    public static Store getStore() {
        return store;
    }

    public static void registerStoreInstance(Store s) {
        store = s;
    }

    public static void release() {
        store.close();
    }


}
