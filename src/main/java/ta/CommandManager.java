package ta;

import java.util.*;
import java.util.regex.Pattern;


import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import ta.admin_commands.ServerInfoCMD;
import ta.commands.HelpCMD;
import ta.commands.Moderation.*;
import ta.commands.PingCMD;
import ta.commands.UserInfoCMD;
import ta.commands.Fun.*;
import ta.util.IntCommand;

public class CommandManager {

    private final Map<String, IntCommand> commands = new HashMap<>();

    CommandManager(Random random) {
        addCommand(new PingCMD());
        addCommand(new HelpCMD(this));
        addCommand(new CatCMD());
        addCommand(new DogCMD());
        addCommand(new JokeCMD(random));
        addCommand(new UserInfoCMD());
        addCommand(new BanCMD());
        addCommand(new UnBanCMD());
        addCommand(new ServerInfoCMD());
        addCommand(new PurgeCMD());
        addCommand(new MemeCMD(random));
        addCommand(new M8ballCMD());
        addCommand(new MuteCMD());
        addCommand(new UnMuteCMD());
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
            commands.get(invoke).handle(args, event);
        }
    }
    //TODO History/Rapsheet command.
    //TODO Roles on Join/leave
    //TODO Temp Ban/Mute Commands
    //TODO Warn command
    //TODO Set Mod actions to database
    //TODO Vote Commands
    //TODO Auto Gen Invite code commands
}
