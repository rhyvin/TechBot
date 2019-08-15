package ta.util;

import ta.Bot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;



public class FileLogger {


    private final static Logger logger = Logger.getLogger(Bot.class.getPackage().getName());
    private static FileHandler handler = null;

    public class logs
    {
        public logs (String[] args)
        {

            // attempt to create the directory here
            boolean successful = (new File(File.separator + "log")).mkdir();
            if (successful)
            {
                // creating the directory succeeded
                System.out.println("directory was created successfully");
            }
            else
            {
                // creating the directory failed
                System.out.println("failed trying to create the directory");
            }
        }
    }

    public static void initializeLogger() throws IOException {
        handler = new FileHandler(Paths.get("log" + File.separator + "bot.log").toString(), true);
        logger.addHandler(handler);
        handler.setFormatter(new SimpleFormatter());
        logger.setUseParentHandlers(false);
    }

    public static void logWarning(String message) {
        logger.log(Level.WARNING, message);
    }

    public static void logInfo(String message) {
        logger.log(Level.INFO, message);
    }
}