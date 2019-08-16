package ta.util;

import java.io.IOException;
import java.util.List;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface IntCommand {

    void handle(List<String> args, GuildMessageReceivedEvent event) throws ClassNotFoundException, IOException;
    String getHelp();
    String getInvoke();

}

