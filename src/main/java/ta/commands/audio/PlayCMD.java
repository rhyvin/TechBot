package ta.commands.audio;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import ta.Constants;
import ta.config.Config;
import ta.music.PlayerManager;
import ta.util.IntCommand;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class PlayCMD implements IntCommand {
    private final YouTube youTube;


    public PlayCMD() {
        YouTube temp = null;


        try {
            temp = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null
            )
                    .setApplicationName("Techno Anomaly Bot")
                    .build();
        } catch (Exception e){
            e.printStackTrace();
        }


        youTube = temp;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws ClassNotFoundException, IOException {
        TextChannel channel = event.getChannel();

        if (args.isEmpty()) {
            channel.sendMessage("Please provide some arguments.").queue();

            return;
        }

        String input = String.join(" ", args);

        if (!isUrl(input)) {
            String ytSearched = searchYoutube(input);

            if (ytSearched == null) {
            channel.sendMessage("YouTube returned no results.").queue();

            }

            input = ytSearched;
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

    @Nullable
    private String searchYoutube(String input) {
        try {
            List<SearchResult> results = youTube.search()
                    .list("id,snippet")
                    .setQ(input)
                    .setMaxResults(1L)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(Config.getInstance().getString("youtubekey"))
                    .execute()
                    .getItems();

            if (!results.isEmpty()) {
                String videoId = results.get(0).getId().getVideoId();

                return "https://www.youtube.com/watch?v=" + videoId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


            return null;
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
