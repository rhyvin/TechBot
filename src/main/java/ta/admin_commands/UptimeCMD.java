package ta.admin_commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import ta.util.IntCommand;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

public class UptimeCMD implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws ClassNotFoundException, IOException {

        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        long uptime = runtimeMXBean.getUptime();
        long uptimeInSeconds = uptime / 1000;
        long numberOfHours = uptimeInSeconds / (60 * 60);
        long numberOfMinutes = (uptimeInSeconds / 60) - (numberOfHours * 60);
        long numberOfSeconds = uptimeInSeconds % 60;

        event.getChannel().sendMessageFormat(
                "My uptime is `%s hours, %s minues, %s seconds.`",
                numberOfHours,numberOfMinutes, numberOfSeconds
        ).queue();

    }

    @Override
    public String getHelp() {
        return "Shows how long bot has been active.";
    }

    @Override
    public String getInvoke() {
        return "uptime";
    }
}
