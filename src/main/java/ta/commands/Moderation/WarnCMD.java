package ta.commands.Moderation;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.Constants;
import ta.config.Config;
import ta.util.IntCommand;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class WarnCMD implements IntCommand {
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

        if (!member.hasPermission(Permission.MESSAGE_MANAGE) || !member.canInteract(target)) {
            channel.sendMessage("You don't have permission to use this command").queue();
            return;
        }


        if (!selfMember.hasPermission(Permission.MESSAGE_MANAGE) || !selfMember.canInteract(target)) {
            channel.sendMessage("I can't warn that user or I don't have the MESSAGE_MANAGE permission").queue();
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        event.getGuild().getController().addRolesToMember(mentionedMembers.get(0), event.getGuild().getRolesByName("Muted", true)).queue();
        channel.sendMessage(String.format("Warned by: %#s, with reason: %s on, " + sdf.format(date) + " at " + stf.format(date) + ". ", event.getAuthor(), reason)).queue();
        Config config = new Config(new File("botconfig.json"));
        try {
            String mod = member.getEffectiveName();
            String id = target.getUser().getId();
            String uname = target.getEffectiveName();
            String guild = event.getGuild().getName();
            long datetime = date.getTime();
            Connection con = DriverManager.getConnection(
                    config.getString("host"), config.getString("uname"), config.getString("upass"));
            String sqlCreate = "INSERT INTO " + guild + " (`id`, `name`, `warnings`, `rwarnings`, `wmod`, `wdatetime`) VALUES (" + id +",'" +uname+"',1,'" + reason + "', '"+ mod +"',CURRENT_TIMESTAMP) ON DUPLICATE KEY UPDATE `warnings` = `warnings` + 1,`rwarnings` = '" +reason+"',`wmod`= '"+mod+"',`wdatetime` = CURRENT_TIMESTAMP;";

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
        return "Warn a member against certain actions."+
                "Usage: `" + Constants.prefix + getInvoke() + " warn <user> <reason>`";
    }

    @Override
    public String getInvoke() {
        return "warn";
    }
}
