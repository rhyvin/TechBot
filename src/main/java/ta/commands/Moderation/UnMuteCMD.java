package ta.commands.Moderation;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.util.IntCommand;

import java.util.List;


public class UnMuteCMD implements IntCommand {
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

        Member target = mentionedMembers.get(0);


        if (!member.hasPermission(Permission.VOICE_MUTE_OTHERS) || !member.canInteract(target)) {
            channel.sendMessage("You don't have permission to use this command").queue();
            return;
        }


        if (!selfMember.hasPermission(Permission.VOICE_MUTE_OTHERS) || !selfMember.canInteract(target)) {
            channel.sendMessage("I can't Unmute that user or I don't have the mute others permission permission").queue();
            return;
        }

        Role muted = event.getGuild().getRolesByName("Muted", true).get(0);

        event.getGuild().getController().removeSingleRoleFromMember(target, muted).queue();

        channel.sendMessage("Success!").queue();

    }

    @Override
    public String getHelp() {
        return "Allow a player to communicate again.";
    }

    @Override
    public String getInvoke() {
        return "unmute";
    }
}
