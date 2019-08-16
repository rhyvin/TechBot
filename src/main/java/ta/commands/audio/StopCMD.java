package ta.commands.audio;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import ta.music.GuildMusicManager;
import ta.music.PlayerManager;
import ta.util.IntCommand;

import java.io.IOException;
import java.util.List;

public class StopCMD implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws ClassNotFoundException, IOException {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());

        musicManager.scheduler.getQueue().clear();
        musicManager.player.stopTrack();
        musicManager.player.setPaused(false);

        event.getChannel().sendMessage("Stopping the player and clearing the queue.").queue();
    }

    @Override
    public String getHelp() {
        return "Stops playback of audio. ";
    }

    @Override
    public String getInvoke() {
        return "stop";
    }
}
