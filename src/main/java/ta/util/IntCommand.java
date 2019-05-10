package ta.util;

import java.util.List;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public interface IntCommand {

    void handle(List<String> args, GuildMessageReceivedEvent event);
    String getHelp();
    String getInvoke();

}

