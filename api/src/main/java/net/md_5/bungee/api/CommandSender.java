package net.md_5.bungee.api;

import java.util.Collection;

public interface CommandSender extends tc.oc.minecraft.api.command.CommandSender
{

    /**
     * Send several messages to this sender. Each message will be sent
     * separately.
     *
     * @param messages the messages to send
     */
    @Deprecated
    public void sendMessages(String... messages);

    /**
     * Get all groups this user is part of. This returns an unmodifiable
     * collection.
     *
     * @return the users groups
     */
    public Collection<String> getGroups();

    /**
     * Adds groups to a this user for the current session only.
     *
     * @param groups the groups to add
     */
    public void addGroups(String... groups);

    /**
     * Remove groups from this user for the current session only.
     *
     * @param groups the groups to remove
     */
    public void removeGroups(String... groups);

    /**
     * Set a permission node for this user.
     *
     * @param permission the node to set
     * @param value the value of the node
     */
    public void setPermission(String permission, boolean value);

    /**
     * Get all Permissions which this CommandSender has
     *
     * @return a unmodifiable Collection of Strings which represent their
     * permissions
     */
    public Collection<String> getPermissions();
}
