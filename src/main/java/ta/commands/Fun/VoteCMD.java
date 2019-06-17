package ta.commands.Fun;

import me.duncte123.botcommons.messaging.EmbedUtils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.Constants;
import ta.util.IntCommand;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class VoteCMD implements IntCommand {

    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        if (args.isEmpty()) {
            channel.sendMessage("Missing arguments, please use " + Constants.prefix + "vote [args].").queue();
            return;
        }
        else {
            String votes = String.join(" ", args.subList(1, args.size()));

            try {

                String joined = String.join("", args);

                MessageEmbed embed = EmbedUtils.defaultEmbed()
                        .addField(member.getEffectiveName() +" wants to vote on:", votes, false)
                        .addField("","to vote yes :white_check_mark: to vote no :x:", true)
                        .build();

                channel.sendMessage(embed).queue();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getHelp() {
        return Constants.prefix + "vote [args].";
    }

    @Override
    public String getInvoke() {
        return "vote";
    }
}