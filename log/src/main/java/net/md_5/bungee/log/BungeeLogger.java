package net.md_5.bungee.log;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import jline.console.ConsoleReader;

public class BungeeLogger extends Logger
{
    public static BungeeLogger get(String loggerName, String filePattern, ConsoleReader reader) {
        final LogManager lm = LogManager.getLogManager();
        Logger logger = lm.getLogger(loggerName);
        if(!(logger instanceof BungeeLogger)) {
            logger = new BungeeLogger(loggerName, filePattern, reader);
            lm.addLogger(logger);
        }
        return (BungeeLogger) logger;
    }

    private final Formatter formatter = new ConciseFormatter();
    private final LogDispatcher dispatcher = new LogDispatcher( this );

    @SuppressWarnings(
            {
                "CallToPrintStackTrace", "CallToThreadStartDuringObjectConstruction"
            })
    @SuppressFBWarnings("SC_START_IN_CTOR")
    private BungeeLogger(String loggerName, String filePattern, ConsoleReader reader)
    {
        super( loggerName, null );
        setParent(Logger.getLogger(""));
        setUseParentHandlers(false);

        try
        {
            FileHandler fileHandler = new FileHandler( filePattern, 1 << 24, 8, true );
            fileHandler.setFormatter( formatter );
            addHandler( fileHandler );

            ColouredWriter consoleHandler = new ColouredWriter( reader );
            consoleHandler.setFormatter( formatter );
            addHandler( consoleHandler );
        } catch ( IOException ex )
        {
            System.err.println( "Could not register logger!" );
            ex.printStackTrace();
        }

        dispatcher.start();
    }

    @Override
    public void log(LogRecord record)
    {
        dispatcher.queue( record );
    }

    void doLog(LogRecord record)
    {
        super.log( record );
    }
}
