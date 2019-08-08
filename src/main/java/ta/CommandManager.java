package ta;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;


import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import ta.admin_commands.*;
import ta.commands.Moderation.RapSheetCMD;
import ta.commands.HelpCMD;
import ta.commands.Moderation.*;
import ta.commands.Moderation.UserInfoCMD;
import ta.commands.Fun.*;
import ta.commands.audio.*;
import ta.util.IntCommand;

public class CommandManager {

    private final Map<String, IntCommand> commands = new HashMap<>();

    CommandManager() {
        //Fun Commands
        addCommand(new PingCMD());
        addCommand(new HelpCMD(this));
        addCommand(new M8ballCMD());
        addCommand(new CatCMD());
        addCommand(new MemeCMD());
        addCommand(new DogCMD());

        //Moderation commands
        addCommand(new UserInfoCMD());
        addCommand(new BanCMD());
        addCommand(new KickCMD());
        addCommand(new UnBanCMD());
        addCommand(new PurgeCMD());
        addCommand(new MuteCMD());
        addCommand(new UnMuteCMD());
        addCommand(new RapSheetCMD());
        addCommand(new WarnCMD());

        //Administrator Commands
        addCommand(new InstallCMD());
        addCommand(new ServerInfoCMD());
        addCommand(new UptimeCMD());

        //Music player Commands
        addCommand(new JoinCMD());
        addCommand(new LeaveCMD());
        addCommand(new PlayCMD());
        addCommand(new QueueCMD());
        addCommand(new SkipCMD());
        addCommand(new StopCMD());
        addCommand(new NowPlayingCMD());
    }

    private void addCommand(IntCommand command) {
        if(!commands.containsKey(command.getInvoke())) {
            commands.put(command.getInvoke(), command);

        }
    }

    public Collection<IntCommand> getCommands(){
        return commands.values();
    }

    public IntCommand getCommand(@NotNull String name){
        return commands.get(name);
    }

    void handleCommand(GuildMessageReceivedEvent event) {
        final String prefix = Constants.PREFIXES.get(event.getGuild().getIdLong());

        final String[] split = event.getMessage().getContentRaw().replaceFirst(
                "(?i)" + Pattern.quote(prefix), "").split("\\s+");
        final String invoke = split[0].toLowerCase();

        if(commands.containsKey(invoke)) {
            final List<String> args = Arrays.asList(split).subList(1, split.length);

            event.getChannel().sendTyping().queue();
            try {
                commands.get(invoke).handle(args, event);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    //TODO History/Rapsheet command.
    //TODO Roles on Join/leave
    //TODO Temp Ban
    //TODO Warn command
    //TODO Vote Commands
    //TODO Auto Gen Invite code commands
}
