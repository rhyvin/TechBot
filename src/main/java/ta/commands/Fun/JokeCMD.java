package ta.commands.Fun;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.util.IntCommand;

import java.util.List;
import java.util.Random;

public class JokeCMD implements IntCommand {

    private final Random random;

    public JokeCMD(Random random) {
        this.random = random;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        WebUtils.ins.getJSONObject("https://sv443.net/jokeapi/category/Any?blacklistFlags=nsfw,dark,religious,political").async( (json) -> {

            if(json.getString("type").equalsIgnoreCase("single")){
                String text = json.getString("joke");
                MessageEmbed embed = EmbedUtils.embedMessage(text).build();
                //TODO: Make a permission check to see if the bot can send embeds if not, send plain text.
                event.getChannel().sendMessage(embed).queue();
            }
            String setup = json.getString("setup");
            String delivery = json.getString("delivery");
            MessageEmbed embed = EmbedUtils.embedMessage(setup + "\n\n||" + delivery + "||" ).build();
            //TODO: Make a permission check to see if the bot can send embeds if not, send plain text.
            event.getChannel().sendMessage(embed).queue();
            MessageEmbed embed1 = EmbedUtils.embedMessage(delivery).build();
        });

    }

    @Override
    public String getHelp() {
        return "Shows you a random meme.";
    }

    @Override
    public String getInvoke() {
        return "joke";
    }
}
