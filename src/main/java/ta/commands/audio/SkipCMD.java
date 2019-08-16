package ta.commands.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import ta.music.GuildMusicManager;
import ta.music.PlayerManager;
import ta.music.TrackScheduler;
import ta.util.IntCommand;

import java.io.IOException;
import java.util.List;

public class SkipCMD implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws ClassNotFoundException, IOException {

        TextChannel channel = event.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;
        AudioPlayer player = musicManager.player;

        if (player.getPlayingTrack() == null) {
            channel.sendMessage("The player isn't playing anything at this time.").queue();

            return;
        }

        scheduler.nextTrack();

        channel.sendMessage("Skipping the current Track").queue();

    }

    @Override
    public String getHelp() {
        return "Skips current song/audio track.";
    }

    @Override
    public String getInvoke() {
        return "skip";
    }
}
