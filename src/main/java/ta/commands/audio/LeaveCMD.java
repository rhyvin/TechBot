package ta.commands.audio;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import ta.util.IntCommand;

import java.io.IOException;
import java.util.List;

public class LeaveCMD implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws ClassNotFoundException, IOException {

        TextChannel channel =event.getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();

        if (!audioManager.isConnected()) {
            channel.sendMessage("I am not connected to an audio channel").queue();
            return;
        }

        VoiceChannel voiceChannel = audioManager.getConnectedChannel();

        if (!voiceChannel.getMembers().contains(event.getMember())) {
            channel.sendMessage("You have to be in the same voice channel as me to use this").queue();
            return;
        }

        audioManager.closeAudioConnection();
        channel.sendMessage("Disconnected from your channel.").queue();

    }

    @Override
    public String getHelp() {
        return "Removes bot from audio channel.";
    }

    @Override
    public String getInvoke() {
        return "leave";
    }
}
