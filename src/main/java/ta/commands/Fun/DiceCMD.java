package ta.commands.Fun;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import ta.Constants;
import ta.util.IntCommand;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DiceCMD implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws ClassNotFoundException, IOException {
        int sides = 6;
        int dice = 1;

        TextChannel channel = event.getChannel();

        if (!args.isEmpty()) {
            sides = Integer.parseInt(args.get(0));

            if (args.size() > 1 ) {
                dice = Integer.parseInt(args.get(1));
            }
        }

        if (sides > 100){
            channel.sendMessage("The maximum number of sides is 100.").queue();

            return;
        }

        if (dice > 20){
            channel.sendMessage("The maximum number of dice is 20").queue();

            return;
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder builder = new StringBuilder()
                .append("Results:\n");

        for (int d = 0; d < dice; d++){
            builder.append("\uD83C\uDFB2 #")
                    .append(d)
                    .append(": **")
                    .append(random.nextInt(1, sides))
                    .append("**\n");
        }

        channel.sendMessage(builder.toString()).queue();
    }

    @Override
    public String getHelp() {
        return "Rolls dice.\n +" +
                "Usage: `" + Constants.prefix + getInvoke() + " [side] [dice]`";
    }

    @Override
    public String getInvoke() {
        return "roll";
    }
}
