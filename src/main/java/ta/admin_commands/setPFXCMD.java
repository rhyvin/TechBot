package ta.admin_commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.Constants;
import ta.util.IntCommand;

import java.io.IOException;
import java.util.List;

public class setPFXCMD implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws ClassNotFoundException, IOException {

        Member member = event.getMember();
        TextChannel channel = event.getChannel();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            channel.sendMessage("You need the Manage Server permission to use this command.").queue();
            return;
        }

        if (args.isEmpty()){
            channel.sendMessage("Usage: `" + Constants.prefix + getInvoke() + " <prefix>`").queue();
            return;
        }

        String newPrefix = args.get(0);

        Constants.PREFIXES.put(event.getGuild().getIdLong(), newPrefix);

        channel.sendMessage("The new prefix has been set to `" + newPrefix + "`").queue();
    }

    @Override
    public String getHelp() {
        return "Sets the prefix for this server.\n" +
                "Usage: `" + Constants.prefix +getInvoke() + "<prefix>`";
    }

    @Override
    public String getInvoke() {
        return "setprefix";
    }
}
