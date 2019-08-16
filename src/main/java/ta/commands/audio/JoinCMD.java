package ta.commands.audio;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import ta.util.IntCommand;

import java.io.IOException;
import java.util.List;

public class JoinCMD implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws ClassNotFoundException, IOException {

        TextChannel channel =event.getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();

        if (audioManager.isConnected()) {
            channel.sendMessage("I am all ready connected to an audio channel").queue();
            return;
        }

        GuildVoiceState memberVoiceState = event.getMember().getVoiceState();

        if (!memberVoiceState.inVoiceChannel()){
            channel.sendMessage("Please join a voice channel first.").queue();
            return;
        }

        VoiceChannel voiceChannel = memberVoiceState.getChannel();
        Member selfmember = event.getGuild().getSelfMember();

        if (!selfmember.hasPermission(voiceChannel, Permission.VOICE_CONNECT)) {
            channel.sendMessageFormat("I am missing permissions to join %s", voiceChannel).queue();
            return;
        }

        audioManager.openAudioConnection(voiceChannel);
        channel.sendMessage("Joining your voice channel").queue();
    }

    @Override
    public String getHelp() {
        return "Make bot join your audio channel.";
    }

    @Override
    public String getInvoke() {
        return "join";
    }
}
