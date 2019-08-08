package ta.commands.audio;


import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.music.GuildMusicManager;
import ta.music.PlayerManager;
import ta.util.IntCommand;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class NowPlayingCMD implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        AudioPlayer player = musicManager.player;

        if (player.getPlayingTrack() == null) {
            channel.sendMessage("The player is not playing any song.").queue();

            return;
        }

        AudioTrackInfo info = player.getPlayingTrack().getInfo();

       MessageEmbed embed = EmbedUtils.defaultEmbed()
               .setTitle("**Now Playing** ")
               .addField(info.title,"", false)
               .addField(info.uri,"",false)
               .addField(formatTime(player.getPlayingTrack().getPosition())+"-"+ formatTime(player.getPlayingTrack().getDuration()),"",false )
               .build();
        event.getChannel().sendMessage(embed).queue();
    }

    private String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public String getHelp() {
        return "Shows the currently playing track";
    }

    @Override
    public String getInvoke() {
        return "np";
    }


}
