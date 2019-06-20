package ta;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ta.config.Config;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Random;

public class Bot {

    private final Random random = new Random();

    private Bot() throws IOException {
        Config config = new Config(new File("botconfig.json"));
        CommandManager commandManager = new CommandManager(random);
        Listener listener = new Listener(commandManager);
        Logger logger = LoggerFactory.getLogger(Bot.class);

        WebUtils.setUserAgent("Mozilla/5.0 TechnoAnomaly Techno Helper Bot/Rhyvin#1694");
        EmbedUtils.setEmbedBuilder(
                () -> new EmbedBuilder()
                        .setColor(Color.getHSBColor(10,100,56))
                        .setFooter("Techno Anomaly", null)
                        .setTimestamp(Instant.now().atOffset(ZoneOffset.UTC))
        );

        try {
            logger.info("Booting");
            new DefaultShardManagerBuilder()
                    .setToken(config.getString("token"))
                    .setGame(Game.streaming("Follow TechnoAnomaly", "http://technoanomaly.com"))
                    .addEventListeners(listener)
                    .build();
            logger.info("Running");
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    private Color getRandomColor() {
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();

        return new Color(r, g, b);
    }

    public static void main(String[] args) throws IOException {
        new Bot();
    }

}
