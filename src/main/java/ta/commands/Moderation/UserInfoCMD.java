package ta.commands.Moderation;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import ta.Constants;
import ta.util.IntCommand;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoCMD implements IntCommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (args.isEmpty()) {
            event.getChannel().sendMessage("Missing arguments, check `" + Constants.prefix + "help " + getInvoke()  + "`").queue();
            return;
        }

        String joined = String.join("", args);
        List<User> foundUsers = FinderUtil.findUsers(joined,event.getJDA());

        if(foundUsers.isEmpty()) {

            List<Member> foundMember = FinderUtil.findMembers(joined, event.getGuild());

            if (foundMember.isEmpty()) {
                event.getChannel().sendMessage("No user found for `" + joined + "`").queue();
                return;
            }

            foundUsers = foundMember.stream().map(Member::getUser).collect(Collectors.toList());

        }

        User user = foundUsers.get(0);
        Member member = event.getGuild().getMember(user);

        MessageEmbed embed = EmbedUtils.defaultEmbed()
                .setColor(member.getColor())
                .setThumbnail(user.getEffectiveAvatarUrl().replaceFirst("gif", "png"))
                .addField("Username#Discriminator", String.format("%#s", user), false)
                .addField("Display name", member.getEffectiveName(), false)
                .addField("User Id + Mention", String.format("%s ()%s", user.getId(), member.getAsMention()),false)
                .addField("Account Created", user.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME), false)
                .addField("Guild Joined", member.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME), false)
                .addField("Online Status", member.getOnlineStatus().name().toLowerCase().replaceAll("_", " "), false)
                .addField("Bot Account", user.isBot() ? "Yes" : "No", false)
                .build();

        event.getChannel().sendMessage(embed).queue();

    }

    @Override
    public String getHelp() {
        return "Displays information about a specific user.\n" +
                "Usage: `" + Constants.prefix + getInvoke() +" <user name/@user/user id>`";
    }

    @Override
    public String getInvoke() {
        return "userinfo";
    }
}
