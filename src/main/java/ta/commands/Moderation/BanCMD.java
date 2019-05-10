package ta.commands.Moderation;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.Constants;
import ta.util.IntCommand;

import java.util.List;

public class BanCMD implements IntCommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (mentionedMembers.isEmpty() || args.size() < 2) {
            channel.sendMessage("Missing Arguments").queue();
            return;
        }
        Member target = mentionedMembers.get(0);
        String reason = String.join(" ", args.subList(1, args.size()));

        if (!member.hasPermission(Permission.BAN_MEMBERS) || !member.canInteract(target)) {
            channel.sendMessage("You don't have permission to use this command").queue();
            return;
        }


        if (!selfMember.hasPermission(Permission.BAN_MEMBERS) || !selfMember.canInteract(target)) {
            channel.sendMessage("I can't ban that user or I don't have the ban members permission").queue();
            return;
        }


        event.getGuild().getController().ban(target, 1)
                .reason(String.format("Banned by: %#s, with reason: %s", event.getAuthor(), reason)).queue();

        channel.sendMessage("Success!").queue();

    }

    @Override
    public String getHelp() {
        return "Bans a user from the server.\n" +
                "Usage: `" + Constants.prefix + getInvoke() + "<user> <reason>`";
    }

    @Override
    public String getInvoke() {
        return "ban";
    }
}
