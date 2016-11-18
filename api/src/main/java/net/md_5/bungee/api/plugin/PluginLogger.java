package net.md_5.bungee.api.plugin;

import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class PluginLogger extends Logger
{
    public static PluginLogger get(Plugin context) {
        LogManager lm = LogManager.getLogManager();
        Logger logger = lm.getLogger(context.getClass().getCanonicalName());

        if(logger instanceof PluginLogger) {
            return (PluginLogger) logger;
        } else {
            PluginLogger pluginLogger = new PluginLogger(context);

            // Register the logger under the plugin's name, unless some other logger is already using the name
            if(logger == null) {
                lm.addLogger(pluginLogger);
                pluginLogger.setParent(context.getProxy().getLogger()); // addLogger changes this, change it back
            }

            return pluginLogger;
        }
    }

    private final String pluginName;

    protected PluginLogger(Plugin plugin)
    {
        super( plugin.getClass().getCanonicalName(), null );
        pluginName = "[" + plugin.getDescription().getName() + "] ";
        setParent( plugin.getProxy().getLogger() );
    }

    @Override
    public void log(LogRecord logRecord)
    {
        logRecord.setMessage( pluginName + logRecord.getMessage() );
        super.log( logRecord );
    }
}
