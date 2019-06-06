package ta.commands.Fun;
/*
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.util.IntCommand;

import java.util.List;
import java.util.Random;

public class MemeCMD  implements IntCommand {

    private final Random random;

    public MemeCMD(Random random) {
        this.random = random;
    }

    @Override
  public void handle(List<String> args, GuildMessageReceivedEvent event) {
        WebUtils.ins.getJSONObject("https://api.memeload.us/v1/random").async( (json) -> {

                String image = json.getString("image");
                MessageEmbed embed = EmbedUtils.embedImage(image).build();
                //TODO: Make a permission check to see if the bot can send embeds if not, send plain text.
                event.getChannel().sendMessage(embed).queue();
            });

    }

    @Override
    public String getHelp() {
        return "Shows you a random meme.";
    }

    @Override
    public String getInvoke() {
        return "meme";
    }
}

 */