package ta.commands.Moderation;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.util.IntCommand;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



public class MuteCMD implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (args.isEmpty() || mentionedMembers.isEmpty()) {
            channel.sendMessage("Missing arguments").queue();
            return;
        }

        Member target = mentionedMembers.get(0);
        String reason = String.join(" ", args.subList(1, args.size()));

        if (!member.hasPermission(Permission.VOICE_MUTE_OTHERS) || !member.canInteract(target)) {
            channel.sendMessage("You don't have permission to use this command").queue();
            return;
        }


        if (!selfMember.hasPermission(Permission.VOICE_MUTE_OTHERS) || !selfMember.canInteract(target)) {
            channel.sendMessage("I can't kick that user or I don't have the kick members permission").queue();
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        event.getGuild().getController().addRolesToMember(mentionedMembers.get(0), event.getGuild().getRolesByName("Muted", true)).queue();
        channel.sendMessage(String.format("Muted by: %#s, with reason: %s on, " + sdf.format(date) + " at " + stf.format(date) + ". ", event.getAuthor(), reason)).queue();

    }

    @Override
    public String getHelp() {
        return "Mute an unruley player";
    }

    @Override
    public String getInvoke() {
        return "mute";
    }
}
