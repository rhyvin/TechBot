package ta.commands.Fun;

import com.fasterxml.jackson.databind.JsonNode;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.util.IntCommand;

import java.util.List;

public class MemeCMD  implements IntCommand {
    @Override
  public void handle(List<String> args, GuildMessageReceivedEvent event) {
        WebUtils.ins.getJSONObject("https://apis.duncte123.me/meme?nsfw=false").async( (json) -> {
            JsonNode data = json.get("data");
            String url = data.get("image").asText();
            MessageEmbed embed = EmbedUtils.embedImage(url)
                    .setTitle(data.get("title").asText(), data.get("url").asText())
                    .build();
            //TODO: Make a permissions check to see if the bot can send embeds if not, send plain text
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

