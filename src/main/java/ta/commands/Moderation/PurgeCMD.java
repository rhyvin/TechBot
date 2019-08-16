package ta.commands.Moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import ta.Constants;
import ta.util.IntCommand;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PurgeCMD implements IntCommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfmember = event.getGuild().getSelfMember();

        if (!member.hasPermission(Permission.MESSAGE_MANAGE)){
            channel.sendMessage("You need the Manage Messages permissions to manage this command.").queue();

            return;
        }

        if (!selfmember.hasPermission(Permission.MESSAGE_MANAGE)){
            channel.sendMessage("I need the Manage Messages permissions to manage this command.").queue();
        }

        if (args.isEmpty()) {
            channel.sendMessage("Correct usage is `" + Constants.prefix + getInvoke() + "<amount>`").queue();

            return;
        }

        int amount;

        String arg = args.get(0);

        try {
            amount = Integer.parseInt(args.get(0));
        }
        catch (NumberFormatException ignored){

            channel.sendMessageFormat("`%s` is not a valid number", arg).queue();

            return;
        }

        if (amount <2 || amount > 100){
            channel.sendMessage("Amount must be set to at lest 2 and no more than 100 messge").queue();

            return;
        }

        channel.getIterableHistory().takeAsync(amount).thenApplyAsync(messages -> {
            List<Message> goodMessages = messages.stream().filter((m) -> !m.getTimeCreated().isAfter(
                    OffsetDateTime.now().plusWeeks(2))).collect(Collectors.toList());

            channel.purgeMessages(goodMessages);

            return goodMessages.size();

        }).whenCompleteAsync(
                (count, thr) -> channel.sendMessageFormat("Deleted '%d' messages", count).queue(
                     message -> message.delete().queueAfter(10, TimeUnit.SECONDS)
                )
        ).exceptionally(thr -> {
            String cause = "";

            if(thr.getCause() != null){
                cause = "Caused by: " + thr.getCause().getMessage();
            }

            channel.sendMessageFormat("Error: %s%s", thr.getMessage(), cause).queue();

            return 0;

            });

    }

    @Override
    public String getHelp() {
        return "Clears the chat in specified current channel with specified amount of messages.\n Max of 100 messages at a time."+
                "Usage: `" + Constants.prefix + getInvoke() + " <amount>`";
    }

    @Override
    public String getInvoke() {
        return "purge";
    }
}
