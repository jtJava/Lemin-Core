package us.lemin.core.discord.listeners;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import us.lemin.core.discord.embeds.BanEmbed;

import java.util.HashMap;

public class MessageListener extends ListenerAdapter {

    private HashMap<String, Long> commandCooldowns = new HashMap<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            String[] message = event.getMessage().getContentRaw().trim().split("\\s+");
            String command = message[0];
            switch (command) {
                case "ban":
                    String bannedId = new StringBuilder(message[1]).delete(0, 1).deleteCharAt(message[1].length()).toString();
                    String bannedIdMention = event.getJDA().getUserById(bannedId).getAsMention();
                    String bannedReason = message[2];
                    event.getChannel().sendMessage(new BanEmbed(event.getJDA().getUserById(bannedId).getAsMention(),
                            event.getAuthor().getAsMention()).build()).queue();
                    event.getJDA().getUserById(bannedId).openPrivateChannel().queue((channel) ->
                    {
                        channel.sendMessage("You were banned by " + event.getAuthor().getName()
                                + "##" + event.getAuthor().getDiscriminator() + " for " + bannedReason).queue();
                    });
                    event.getGuild().getController().ban(bannedId, 1, message[2]).queue();
                    break;
                case "mute":
                    Role muted = event.getGuild().getRoleById("474776391678689280");
                    String mutedId = new StringBuilder(message[1]).delete(0, 1).deleteCharAt(message[1].length()).toString();
                    String mutedReason = message[2];
                    event.getChannel().sendMessage(new BanEmbed(event.getJDA().getUserById(mutedId).getAsMention(),
                            event.getAuthor().getAsMention()).build()).queue();
                    event.getGuild().getMember(event.getJDA().getUserById(mutedId)).getRoles().add(muted);
                    break;
            }
        }
    }

    private boolean onCooldown(String user) {
        return System.currentTimeMillis() - commandCooldowns.get(user) > 2000;
    }
}
