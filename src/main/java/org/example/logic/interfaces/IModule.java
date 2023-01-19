package org.example.logic.interfaces;

public interface IModule {

    /**
     * Called when the module is loaded by the server.
     */
    public void onLoad();

    /**
     * Called when the module is enabled by the server.
     * @throws Exception if an error occurs while enabling the module.
     */
    public void onEnable() throws Exception;

    /**
     * Called when the module is disabled by the server.
     */
    public void onDisable();
}
