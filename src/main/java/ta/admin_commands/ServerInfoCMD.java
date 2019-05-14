package ta.admin_commands;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.util.IntCommand;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ServerInfoCMD implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        Guild guild = event.getGuild();

        String genInfo = String.format(
                "**Owner**: <@%s>\n **Region**: %s\n**Creation Date**: %s\n**Verification Level**: %s",
                guild.getOwnerId(),
                guild.getRegion().getName(),
                guild.getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME),
                converVerificationLevel(guild.getVerificationLevel())
        );

        String memberInfo = String.format(
                "**Total Roles**: %s\n**Total Members**: %s\n**Online Members**: %s\n **Offline Members**: %s\n**Bot Count**: %s",
                guild.getRoleCache().size(),
                guild.getMemberCache().size(),
                guild.getMemberCache().stream().filter(member -> member.getOnlineStatus() == OnlineStatus.ONLINE).count(),
                guild.getMemberCache().stream().filter(member -> member.getOnlineStatus() == OnlineStatus.OFFLINE).count(),
                guild.getMemberCache().stream().filter(member -> member.getUser().isBot()).count()
        );

        EmbedBuilder embed = EmbedUtils.defaultEmbed()
            .setTitle("Server info for" + guild.getName())
            .setThumbnail(guild.getIconUrl())
                .addField("General Info", "genInfo", false )
                .addField("Role and Member Counts",memberInfo, false)
            ;
        event.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getHelp() {
        return "Displays information about the server.";
    }

    @Override
    public String getInvoke() {
        return "serverinfo";
    }

    private String converVerificationLevel(Guild.VerificationLevel lvl){
        String[] names =lvl.name().toLowerCase().split("_");
        StringBuilder out = new StringBuilder();

        for (String name : names){
            out.append(Character.toUpperCase(name.charAt(0))).append(name.substring(1)).append(" ");
        }
        return out.toString().trim();
    }
}
