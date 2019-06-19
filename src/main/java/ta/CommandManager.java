package ta;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;


import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import ta.admin_commands.InstallCMD;
import ta.admin_commands.RapSheetCMD;
import ta.admin_commands.ServerInfoCMD;
import ta.commands.HelpCMD;
import ta.commands.Moderation.*;
import ta.commands.PingCMD;
import ta.commands.UserInfoCMD;
import ta.commands.Fun.*;
import ta.commands.audio.*;
import ta.tickets.ticketCreate;
import ta.tickets.ticketUpdate;
import ta.util.IntCommand;

public class CommandManager {

    private final Map<String, IntCommand> commands = new HashMap<>();

    CommandManager(Random random) {
        addCommand(new PingCMD());
        addCommand(new HelpCMD(this));
        addCommand(new CatCMD());
        addCommand(new UserInfoCMD());
        addCommand(new BanCMD());
        addCommand(new KickCMD());
        addCommand(new UnBanCMD());
        addCommand(new ServerInfoCMD());
        addCommand(new PurgeCMD());
        addCommand(new M8ballCMD());
        addCommand(new MuteCMD());
        addCommand(new UnMuteCMD());
        addCommand(new RapSheetCMD());
        addCommand(new WarnCMD());
        addCommand(new InstallCMD());
        addCommand(new JoinCMD());
        addCommand(new LeaveCMD());
        addCommand(new PlayCMD());
        addCommand(new QueueCMD());
        addCommand(new SkipCMD());
        addCommand(new StopCMD());
        addCommand(new NowPlayingCMD());
        addCommand(new VoteCMD());
        addCommand(new ticketCreate());
        addCommand(new ticketUpdate());
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
        final String[] split = event.getMessage().getContentRaw().replaceFirst(
                "(?i)" + Pattern.quote(Constants.prefix), "").split("\\s+");
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
