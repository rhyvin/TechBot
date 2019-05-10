package ta.commands;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.core.entities.Message;
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
        WebUtils.ins.getJSONObject("https://api-to.get-a.life/meme").async( (json) -> {

            if(random.nextInt(2) == 1){
                String url = json.getString("url");
                MessageEmbed embed = EmbedUtils.embedImage(url);
                //TODO: Make a permission check to see if the bot can send embeds if not, send plain text.
                event.getChannel().sendMessage(embed).queue();
            }
            String text = json.getString("text");
            MessageEmbed embed = EmbedUtils.embedMessage(text);
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
