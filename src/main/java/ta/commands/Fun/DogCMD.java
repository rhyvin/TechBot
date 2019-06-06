package ta.commands.Fun;
/*
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.util.IntCommand;

import java.util.List;

public class DogCMD implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        WebUtils.ins.getJSONObject("https://random.dog/woof.json").async( (json) -> {
            String url = json.getString("url");
            MessageEmbed embed = EmbedUtils.embedImage(url).build();
            //TODO: Make a permission check to see if the bot can send embeds if not, send plain text.
            event.getChannel().sendMessage(embed).queue();
        });

    }

    @Override
    public String getHelp() {
        return "Shows you a random dog";
    }

    @Override
    public String getInvoke() {
        return "dog";
    }
}

*/