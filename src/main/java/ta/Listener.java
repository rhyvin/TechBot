package ta;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;


class Listener extends ListenerAdapter {
    String[] wmessages = {
            "[member] joined. You must construct additional pylons.",
            "Never gonna give [member] up. Never let [member] down!",
            "Hey! Listen! [member] has joined!",
            "Ha! [member] has joined! You activated my trap card!",
            "We've been expecting you, [member].",
            "It's dangerous to go alone, take [member]!",
            "Swoooosh. [member] just landed.",
            "Brace yourselves. [member] just joined the server.",
            "A wild [member] appeared."
    };
    String[] lmessages = {
            "[member]goes bye, bye.",
            "l8r g8r",
            "May the force be with you [member]."
    };

    private final CommandManager manager;
    private final Logger logger = LoggerFactory.getLogger(Listener.class);

    Listener(CommandManager manager){
        this.manager = manager;
    }

    @Override
    public void onReady (ReadyEvent event) {
        logger.info(String.format("Logged in as %#s", event.getJDA().getSelfUser()));
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        User author = event.getAuthor();
        Message message = event.getMessage();
        String content = message.getContentDisplay();

        if (event.isFromType(ChannelType.TEXT)) {

            Guild guild = event.getGuild();
            TextChannel textchannel = event.getTextChannel();

            logger.info(String.format("(%s)[%s]<%#s>: %s", guild.getName(), textchannel.getName(), author, content));
        } else if (event.isFromType(ChannelType.PRIVATE)) {
            logger.info(String.format("[PRIV]<%#s>: %s", author, content));
        }
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        if(event.getMessage().getContentRaw().equalsIgnoreCase(Constants.prefix + "shutdown") &&
                event.getAuthor().getIdLong() == Constants.OWNER) {
            shutdown(event.getJDA());
            return;
        }

        if(!event.getAuthor().isBot() && !event.getMessage().isWebhookMessage() &&
        event.getMessage().getContentRaw().startsWith(Constants.prefix)) {
            manager.handleCommand(event);
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Random rand = new Random();
        int number = rand.nextInt(wmessages.length);

        EmbedBuilder join = new EmbedBuilder();
        join.setColor(0x66d8ff);
        join.setDescription(wmessages[number].replace("[member]", event.getMember().getAsMention()));

        event.getGuild().getDefaultChannel().sendMessage(join.build()).queue();

        // Add role
        event.getGuild().getController().addRolesToMember(event.getMember(), event.getGuild().getRolesByName("Member", true)).complete();

        //send private message
        User user = event.getMember().getUser();
        {
            user.openPrivateChannel().queue((channel) ->
            {
                channel.sendMessage("Welcome to the Techno Anomaly Discord Server "+ event.getMember().getUser().getName() + ", these are the rules. \n 🔅-No Spamming Messages \n 🔅-No Racist comments/images \n 🔅-Put messages in the apporpriate channels \n 🔅 -Respect Server staff \n 🔅 -Do not post files that could be malicious \n 🔅-Follow Discord TOS and overlyiung rules \n 🔅-Utilize @Everyone and @here sparingly and only as needed \n 🔅-Please refrain from Political and/or Religious discussions \n 🔅-Please no Advertizing of other Discord servers. \n Thank you, and I look forward to chatting with you! \n Rhyvin").queue();
            });
        }
    }
    public void onGuildMemberLeave(GuildMemberLeaveEvent event){
        Random rand = new Random();
        int number = rand.nextInt(lmessages.length);

        EmbedBuilder join = new EmbedBuilder();
        join.setColor(0x66d8ff);
        join.setDescription(lmessages[number].replace("[member]", event.getMember().getAsMention()));

        event.getGuild().getDefaultChannel().sendMessage(join.build()).queue();

        // Add role
        event.getGuild().getController().addRolesToMember(event.getMember(), event.getGuild().getRolesByName("Member", true)).complete();
    }

    private void shutdown(JDA jda){
        jda.shutdown();
        System.exit(0);
    }
}
