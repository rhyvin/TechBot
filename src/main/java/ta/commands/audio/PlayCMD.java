package ta.commands.audio;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import ta.Constants;
import ta.music.PlayerManager;
import ta.util.IntCommand;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class PlayCMD implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws ClassNotFoundException, IOException {
        TextChannel channel = event.getChannel();

        if (args.isEmpty()) {
            channel.sendMessage("Please provide some arguments.").queue();

            return;
        }

        String input = String.join(" ", args);

        if (!isUrl(input) && input.startsWith("ytsearch:")) {
            // Use the youtube api for search instead, making alot of requests with "ytsearch:" will get you blocked.
            channel.sendMessage("Please provide a valid youtube, soundcloud, or bandcamp link").queue();

            return;
        }

        PlayerManager manager = PlayerManager.getInstance();

        manager.loadAndPlay(event.getChannel(),input);

        manager.getGuildMusicManager(event.getGuild()).player.setVolume(10);

    }

    private boolean isUrl(String input) {
        try {
            new URL(input);

            return true;
        }catch (MalformedURLException ignored) {

            return false;
        }
    }

    @Override
    public String getHelp() {
        return "Plays a song\n" +
                "Usage: '" + Constants.prefix + getInvoke() + " <song url>'";
    }

    @Override
    public String getInvoke() {
        return "play";
    }
}
