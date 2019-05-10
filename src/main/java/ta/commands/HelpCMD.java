package ta.commands;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import ta.CommandManager;
import ta.Constants;
import ta.util.IntCommand;

import java.util.List;


public class HelpCMD implements IntCommand {


    private final CommandManager manager;

    public HelpCMD(CommandManager manager) {
        this.manager = manager;
    }



    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (args.isEmpty()) {
            generateAndSendEmbeds(event);
            return;
        }

        String joined = String.join("",args);

        IntCommand command = manager.getCommand(String.join("", args));

        if(command == null) {
            event.getChannel().sendMessage("The command `" + joined + "` does not exist\n" +
                    "Use `" + Constants.prefix + getInvoke() + "` for a list of commands.").queue();
            return;
        }

        String message = "Command help for `" + command.getInvoke() + "`\n" + command.getHelp();

        event.getChannel().sendMessage(message).queue();
    }

    private void  generateAndSendEmbeds(GuildMessageReceivedEvent event) {

        EmbedBuilder builder = EmbedUtils.defaultEmbed().setTitle("A list of all my commands:");

        StringBuilder descriptionBuilder = builder.getDescriptionBuilder();

        manager.getCommands().forEach(
                (command) -> descriptionBuilder.append('`').append(command.getInvoke()).append("`\n")
        );

        //TODO: Make a permissions check to see if the bot can send embeds if not, send plain text
        event.getChannel().sendMessage(builder.build()).queue();

    }


    @Override
    public String getHelp() {
        return "SHows a list of all the commands.\n" +
                "Usage: `" + Constants.prefix + getInvoke() + " [command`";
    }

    @Override
    public String getInvoke() {
        return "help";
    }
}
