package org.example.logic.interfaces;

import org.bukkit.configuration.file.FileConfiguration;

public interface IFile {

    /**
     * Reloads the file.
     */
    public void reloadFile();

    /**
     * Returns the file.
     * If the file is null, reloads the file before returning.
     *
     * @return the file.
     */
    public FileConfiguration getFile();

    /**
     * Saves the current file.
     * If the file or file is null, the method returns without saving.
     */
    public void saveFile();
}
