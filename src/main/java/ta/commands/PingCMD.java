package ta.commands;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.Constants;
import ta.util.IntCommand;

import java.util.List;

public class PingCMD implements IntCommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("Pong!").queue((message) ->
                message.editMessageFormat("Ping is %sms", event.getJDA().getPing()).queue()
        );

    }

    @Override
    public String getHelp() {
        return "pong!\n" +
                "Usage: `" + Constants.prefix +getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "ping";
    }
}
