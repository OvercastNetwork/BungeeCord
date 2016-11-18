package net.md_5.bungee.api;

import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.Collection;
import java.util.Map;

/**
 * Core configuration adaptor for the proxy api.
 *
 * @deprecated This class is subject to rapid change between releases
 */
@Deprecated
public interface ProxyConfig
{

    /**
     * Time before users are disconnected due to no network activity.
     */
    int getTimeout();

    /**
     * UUID used for metrics.
     */
    String getUuid();

    /**
     * Set of all listeners.
     */
    Collection<ListenerInfo> getListeners();

    /**
     * Set of all servers.
     *
     * @deprecated The returned map may be modified concurrently by the proxy.
     *             The safe alternative is {@link #getServersCopy()}.
     */
    @Deprecated
    Map<String, ServerInfo> getServers();

    /**
     * Return all servers registered to this proxy, keyed by name. The returned map
     * is an immutable snapshot of the actual server collection. It cannot be modified,
     * and it will not change.
     *
     * @return all registered remote server destinations
     */
    Map<String, ServerInfo> getServersCopy();

    /**
     * Gets the server info of a server.
     *
     * @param name the name of the configured server
     * @return the server info belonging to the specified server
     */
    ServerInfo getServerInfo(String name);

    /**
     * Register the given server to the proxy.
     * Any currently registered server with the same name will be replaced.
     *
     * @return the previously registered server with the same name, or null if there was no such server.
     */
    ServerInfo addServer(ServerInfo server);

    /**
     * Register all of the given servers to the proxy.
     *
     * @return true if any servers were added or replaced.
     */
    boolean addServers(Collection<ServerInfo> servers);

    /**
     * Un-register the server with the given name from the proxy.
     *
     * @return the server that was removed, or null if there is no server with the given name.
     */
    ServerInfo removeServerNamed(String name);

    /**
     * Un-register the given server from the proxy.
     * The server is matched by name only, other fields in the given {@link ServerInfo} are ignored.
     *
     * @return the server that was removed, or null if there is no server with a matching name.
     */
    ServerInfo removeServer(ServerInfo server);

    /**
     * Un-register servers with any of the given names from the proxy.
     *
     * @return true if any servers were removed.
     */
    boolean removeServersNamed(Collection<String> names);

    /**
     * Un-register all of the given servers from the proxy.
     * The servers are matched by name only, other fields in the given {@link ServerInfo} are ignored.
     *
     * @return true if any servers were removed.
     */
    boolean removeServers(Collection<ServerInfo> servers);

    /**
     * Does the server authenticate with mojang
     */
    boolean isOnlineMode();

    /**
     * Whether proxy commands are logged to the proxy log
     */
    boolean isLogCommands();

    /**
     * Returns the player max.
     */
    int getPlayerLimit();

    /**
     * A collection of disabled commands.
     */
    Collection<String> getDisabledCommands();

    /**
     * The connection throttle delay.
     */
    @Deprecated
    int getThrottle();

    /**
     * Whether the proxy will parse IPs with spigot or not
     */
    @Deprecated
    boolean isIpForward();

    /**
     * The encoded favicon.
     *
     * @deprecated Use #getFaviconObject instead.
     */
    @Deprecated
    String getFavicon();

    /**
     * The favicon used for the server ping list.
     */
    Favicon getFaviconObject();

    /**
     * Shutdown immediately if any plugins fail to load
     */
    boolean isRequireAllPlugins();
}
