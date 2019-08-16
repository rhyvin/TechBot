package ta.commands.Moderation;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import ta.Constants;
import ta.config.Config;
import ta.util.IntCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

public class KickCMD implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws IOException {

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

        if (!member.hasPermission(Permission.KICK_MEMBERS) || !member.canInteract(target)) {
            channel.sendMessage("You don't have permission to use this command").queue();
            return;
        }


        if (!selfMember.hasPermission(Permission.KICK_MEMBERS) || !selfMember.canInteract(target)) {
            channel.sendMessage("I can't kick that user or I don't have the kick members permission").queue();
            return;
        }


        event.getGuild().kick(target, String.format("Kick by: %#s, with reason: %s",
                event.getAuthor(), reason)).queue();

        channel.sendMessage("Success!").queue();
        Config config = new Config(new File("botconfig.json"));
        try {
            String mod = member.getEffectiveName();
            String id = target.getUser().getId();
            String uname = target.getEffectiveName();
            String guild = event.getGuild().getName();
            Connection con = DriverManager.getConnection(
                    config.getString("host"), config.getString("uname"), config.getString("upass"));
            String sqlCreate = "INSERT INTO " + guild + " (`id`, `name`, `kicks`, `rkicks`, `kmod`, `kdatetime`) VALUES (" + id +",'" +uname+"',1,'" + reason + "','"+mod+"', CURRENT_TIMESTAMP) ON DUPLICATE KEY UPDATE `kicks` = `kicks` + 1,`rkicks` = '" +reason+"', `kmod` = '"+mod+"', `kdatetime` = CURRENT_TIMESTAMP;";

            // create the java statement
            Statement st = con.createStatement();
            st.execute(sqlCreate);
            st.close();
        }        catch (Exception e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

    }

    @Override
    public String getHelp() {
        return "Kicks a user off the server.\n" +
                "Usage: `"  + Constants.prefix + getInvoke() + " <user> <reason>`";
    }

    @Override
    public String getInvoke() {
        return "kick";
    }
}