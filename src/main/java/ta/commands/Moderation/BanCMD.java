package ta.commands.Moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import ta.Constants;

import ta.config.Config;
import ta.util.IntCommand;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

public class BanCMD implements IntCommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws IOException {

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


        event.getGuild().ban(target, 1)
                .reason(String.format("Banned by: %#s, with reason: %s", event.getAuthor(), reason)).queue();

        channel.sendMessage("Success!").queue();

        Config config = new Config(new File("botconfig.json"));
        try {
            String mod = member.getEffectiveName();
            String id = target.getUser().getId();
            String uname = target.getEffectiveName();
            String guild = event.getGuild().getName();
            Connection con = DriverManager.getConnection(
                    config.getString("host"), config.getString("uname"), config.getString("upass"));
            String sqlCreate = "INSERT INTO " + guild + " (`id`, `name`, `bans`, `rbans`, `bmod`, `bdatetime` ) VALUES (" + id +",'" +uname+"',1,'" + reason + "', '"+ mod +"', CURRENT_TIMESTAMP ) ON DUPLICATE KEY UPDATE `bans` = `bans` + 1,`rbans` = '" +reason+"' , `bmod` = '"+mod+"', `bdatetime` = CURRENT_TIMESTAMP;";

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
        return "Bans a user from the server.\n" +
                "Usage: `" + Constants.prefix + getInvoke() + " <user> <reason>`";
    }

    @Override
    public String getInvoke() {
        return "ban";
    }
}
