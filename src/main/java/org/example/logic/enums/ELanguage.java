package org.example.logic.enums;

import org.bukkit.ChatColor;

public enum ELanguage {

    defaultLanguage("&cPlease choose a language message. The current message is a default message."),
    noPermission("&xYou do not have permissions to perform this action. Please contact your system administrator for assistance.");

    private final String message;

    ELanguage(String message) {
        this.message = message;
    }

    /**
     * Retrieves the message on the enum name.
     *
     * @return the message of the enum in color codes
     */
    public String getMessage() {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Returns the {@link ELanguage} object that corresponds to the given name.
     *
     * @param name the name of the ELanguage object to return
     * @return the ELanguage object that corresponds to the given name, otherwise null.
     */
    public ELanguage getELanguage(String name) {
        ELanguage eLanguage = null;
        for (ELanguage obj : ELanguage.values()) {
            if (name.equalsIgnoreCase(obj.name())) {
                eLanguage = obj;
                break;
            }
        }
        return eLanguage;
    }
}
