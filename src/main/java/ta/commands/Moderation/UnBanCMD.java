package ta.commands.Moderation;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.Constants;
import ta.util.IntCommand;

import java.util.List;
import java.util.stream.Collectors;

public class UnBanCMD implements IntCommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (mentionedMembers.isEmpty()) {
            channel.sendMessage("Please specify a user by <username/user id/username#disc>").queue();
            return;
        }
        String argsJoined = String.join(" ", args);

        event.getGuild().getBanList().queue( (bans) ->{

            List<User> goodUsers = bans.stream().filter((ban) -> isCorrectUser(ban, argsJoined))
                    .map(Guild.Ban::getUser).collect(Collectors.toList());

            if (goodUsers.isEmpty()) {
                channel.sendMessage("This user is not banned.").queue();
                return;
            }
            User target = goodUsers.get(0);

            String mod = String.format("%#s", event.getAuthor());
            String bannedUser = String.format("%#s", target);

            event.getGuild().getController().unban(target)
                    .reason("Unbanned by:" + mod).queue();
            channel.sendMessage("User: " + bannedUser + " unbanned.");

                });


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
        return "Unbans a user from the server.\n" +
                "Usage: `" + Constants.prefix + getInvoke() + "<username/user id/username#disc>`";
    }

    @Override
    public String getInvoke() {
        return "unban";
    }

    private boolean isCorrectUser(Guild.Ban ban, String arg) {
        User bannedUser = ban.getUser();

        return bannedUser.getName().equalsIgnoreCase(arg)  || bannedUser.getId().equals(arg)
            || String.format("%#s", bannedUser).equalsIgnoreCase(arg);
    }
}
