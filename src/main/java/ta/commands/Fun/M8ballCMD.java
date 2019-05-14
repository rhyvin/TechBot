package ta.commands.Fun;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.util.IntCommand;

import java.util.List;
import java.util.Random;

public class M8ballCMD implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String[] eightBallResponses = {
                "It is certain.",
                "It is decidedly so.",
                "Without a doubt.",
                "Yes - definitely.",
                "You may rely on it.",
                "As I see it, yes.",
                "Most likely.",
                "Outlook good.",
                "Yes.",
                "Signs point to yes.",
                "Reply hazy, try again.",
                "Ask again later.",
                "Better not tell you now.",
                "Cannot predict now.",
                "Concentrate and ask again.",
                "Don't count on it.",
                "My reply is no.",
                "My sources say no.",
                "Outlook not so good.",
                "Very doubtful."
        };
        Random rand = new Random();
        int number = rand.nextInt(eightBallResponses.length);
        EmbedBuilder mball = new EmbedBuilder();
        mball.setColor(0x66d8ff);
        mball.setDescription("Magic 8 Ball says: `" + eightBallResponses[number] + "`");

        event.getGuild().getDefaultChannel().sendMessage(mball.build()).queue();

    }


    @Override
    public String getHelp() {
        return "Its just like shaking your own Magic 8 ball!";
    }

    @Override
    public String getInvoke() {
        return "8ball";
    }
}
