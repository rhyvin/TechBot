package ta.admin_commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import ta.Constants;
import ta.util.IntCommand;

import java.util.List;

public class PingCMD implements IntCommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("Pong!").queue((message) ->
                message.editMessageFormat("Ping is %sms", event.getJDA().getGatewayPing()).queue()
        );

    }

    @Override
    public String getHelp() {
        return "pong!\n Provides round trip time from Discord to bot and back." +
                "Usage: `" + Constants.prefix +getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "ping";
    }
}
