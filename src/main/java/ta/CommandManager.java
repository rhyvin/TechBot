package ta;

import java.util.*;
import java.util.regex.Pattern;

import com.sun.istack.internal.NotNull;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.commands.*;
import ta.util.IntCommand;

public class CommandManager {

    private final Map<String, IntCommand> commands = new HashMap<>();

    CommandManager(Random random) {
        addCommand(new PingCMD());
        addCommand(new HelpCMD(this));
        addCommand(new CatCMD());
        addCommand(new DogCMD());
        addCommand(new MemeCMD(random));
        addCommand(new UserInfoCMD());
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

}
