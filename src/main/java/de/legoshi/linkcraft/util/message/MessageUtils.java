package de.legoshi.linkcraft.util.message;

import de.legoshi.linkcraft.Linkcraft;
import de.legoshi.linkcraft.util.CommonsUtils;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import net.kyori.text.adapter.bukkit.TextAdapter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Utils related to messages.
 */
public interface MessageUtils {

    /**
     * Send a String to the server.
     *
     * @param message String to send
     * @param prefix  Prefix use.
     */
    static void broadcast(Object message, boolean prefix) {
        broadcast(message, prefix, ArrayUtils.EMPTY_OBJECT_ARRAY);
    }

    /**
     * Send a String to the server.
     *
     * @param message String to send
     * @param prefix  Prefix use.
     * @param format  Placeholders to format.
     */
    static void broadcast(Object message, boolean prefix, Object... format) {
        String send = composeMessage(message, prefix, format);
        sendBroadcast(send);
    }

    /**
     * Send a String to the console.
     *
     * @param message String to send
     * @param prefix  Prefix use.
     */
    static void console(Object message, boolean prefix) {
        console(message, prefix, ArrayUtils.EMPTY_OBJECT_ARRAY);
    }

    /**
     * Send a String to the console.
     *
     * @param message String to send
     * @param prefix  Prefix use.
     * @param format  Placeholders to format.
     */
    static void console(Object message, boolean prefix, Object... format) {
        String send = composeMessage(message, prefix, format);
        sendConsole(send);
    }

    /**
     * Send a message formatted to sender.
     *
     * @param sender  Sender to send.
     * @param message Message to format and send.
     * @param prefix  Prefix use.
     */
    static void compose(CommandSender sender, Object message, boolean prefix, Object... format) {
        String send = composeMessage(message, prefix, format);
        sendMessage(sender, send);
    }

    /**
     * Compose a colorized message to send with a concatenated string
     * without color.
     *
     * @param sender  - Sender to send.
     * @param message - Format message.
     * @param prefix  - Use or not prefix.
     * @param format  - Placeholders to format.
     */
    @Deprecated
    static void composeConcat(CommandSender sender, Object message, Object concat, boolean prefix, Object... format) {
        String send = composeMessage(message, prefix, format);
        sendMessage(sender, send + concat);
    }

    static void sendMessage(CommandSender sender, String send) {
        sender.sendMessage(send);
    }

    static void sendMessage(Player player, String send) {
        player.sendMessage(send);
    }


    static void sendConsole(TextComponent send) {
        Bukkit.getConsoleSender().sendMessage(toLegacy(send));
    }

    static void sendConsole(String send) {
        Bukkit.getConsoleSender().sendMessage(send);
    }

    static void sendBroadcast(TextComponent send) {
        Bukkit.broadcastMessage(toLegacy(send));
    }

    static String toLegacy(Component textComponent) {
        return LegacyComponentSerializer.INSTANCE.serialize(textComponent);
    }

    static void sendBroadcast(String send) {
        Bukkit.broadcastMessage(send);
    }

    static TextComponent messageOf(Messages message) {
        return TextComponent.of(getMessageTranslated(message));
    }

    static TextComponent messageOf(Messages message, TextColor color) {
        return TextComponent.of(getMessageTranslated(message)).color(color);
    }

    static TextComponent messageOf(Messages message, TextColor color, Messages showText) {
        return messageOf(message, color).hoverEvent(HoverEvent.showText(messageOf(showText)));
    }

    static TextComponent messageOf(Messages message, TextColor color, HoverEvent hoverEvent) {
        return messageOf(message, color).hoverEvent(hoverEvent);
    }

    static TextComponent messageOf(Messages message, TextColor color, Messages showText, String executeCommand) {
        return messageOf(message, color, showText).clickEvent(ClickEvent.runCommand(executeCommand));
    }

    static TextComponent messageOf(Messages message, TextColor color, HoverEvent showText, String executeCommand) {
        return messageOf(message, color, showText).clickEvent(ClickEvent.runCommand(executeCommand));
    }

    static TextComponent.Builder messageBuilder(Messages message) {
        return TextComponent.builder(getMessageTranslated(message));
    }

    static TextComponent.Builder messageBuilder(Messages message, TextColor color) {
        return messageBuilder(message).color(color);
    }

    static HoverEvent hoverTextOf(Messages message, Object... objects) {
        String text = getMessageTranslated(message);
        text = CommonsUtils.formatPlaceholder(text, objects);
        return HoverEvent.showText(TextComponent.of(text));
    }

    static void warning(String string) {
        getLogger().warning(string);
    }

    static void warning(String... string) {
        for (String s : string) {
            warning(s);
        }
    }

    static void info(String string) {
        getLogger().info(string);
    }

    static void info(String... string) {
        for (String s : string) {
            getLogger().info(s);
        }
    }

    /**
     * Get the logger of the main class.
     *
     * @return Logger
     */
    static Logger getLogger() {
        return Linkcraft.getPlugin().getLogger();
    }

    static String translateChatColor(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    /**
     * Compose all the components of message.
     *
     * @param message - Message to translate
     * @param prefix  - Using prefix.
     * @param objects - The objects used to replace the placeholders
     * @return Message as String
     */
    static String composeMessage(Object message, boolean prefix, Object... objects) {
        TextComponent.Builder builder = newBuilder(prefix);
        String send = processMessage(message);
        builder.append(objects.length > 0 ? CommonsUtils.formatPlaceholder(send, objects) : send);
        String m = toLegacy(builder.build());
        for (int i = objects.length; i < 10; i++) m = m.replace("{"+i+"} ", "").replace("{"+i+"}", "");
        return translateChatColor(m);
    }

    static Component composeComponent(String base, TextComponent.Builder[] builders, boolean prefix) {
        Component[] components = new Component[builders.length];
        for (int i = 0; i < builders.length; i++) {
            components[i] = builders[i].build();
        }
        return composeComponent(base, components, prefix);
    }

    static Component composeComponent(String base, List<? extends Component> components, boolean prefix) {
        TextComponent component = formatComponent(base, components);
        return composeComponent(component, prefix);
    }

    static Component composeComponent(String base, Component[] components, boolean prefix) {
        TextComponent component = formatComponent(base, components);
        return composeComponent(component, prefix);
    }

    static Component composeComponent(Component message, boolean prefix) {
        return newBuilder(prefix).append(message).build();
    }

    static TextComponent.Builder newBuilder(boolean prefix) {
        TextComponent.Builder builder = TextComponent.builder();
        if (prefix) {
            builder.append(processPrefix());
        }
        return builder;
    }

    static TextComponent formatComponent(String base, Component... components) {
        return formatComponent(base, Arrays.asList(components));
    }

    static TextComponent formatComponent(String base, List<? extends Component> components) {
        TextComponent.Builder builder = TextComponent.builder();
        String format = "{%s}";
        for (int i = 0; i < components.size(); i++) {
            Component component = components.get(i);
            String placeholder = String.format(format, i);
            if (base.isEmpty() || !base.contains(placeholder)) {
                break;
            }

            int position = base.indexOf(placeholder);
            String start = base.substring(0, position);
            // Sum +3 to pass placeholder
            base = base.substring(position + 3);
            builder.append(start).append(component);
        }
        return builder.append(base).build();
    }

    static String composeMessageStripped(Object message, boolean prefix, Object... objects) {
        return ChatColor.stripColor(composeMessage(message, prefix, objects));
    }

    static String processPrefix() {
        return getMessageTranslated(Messages.INFO);
    }

    static String processMessage(Object send) {
        if (send == null) {
            warning("A message is null and trying to be process.");
            return MessageUtils.getMessageTranslated(Messages.ERROR_FATAL);
        }

        if (send instanceof Component) {
            return toLegacy((Component) send);
        }

        String message;
        if (send instanceof String) {
            message = (String) send;
        } else if (send instanceof Messages) {
            message = getMessageTranslated((Messages) send);
        } else {
            message = String.valueOf(send);
        }

        return message;
    }

    static boolean componentIsEmpty(Component component) {
        return toLegacy(component).isEmpty();
    }

    static boolean isNullOrEmpty(Component component) {
        return component == null || componentIsEmpty(component);
    }

    /**
     * Get the message translated by the messages YamlFile.
     *
     * @param message - Message to get in yaml
     * @return Message translated or default.
     */
    static String getMessageTranslated(Messages message) {
        return message.getMessage();
    }

    static TextColor asTextColor(ChatColor chatColor) {
        return TextColor.valueOf(chatColor.name());
    }

    static void sendMessage(CommandSender sender, Component send) {
        TextAdapter.sendComponent(sender, send);
    }

    /**
     * Send a {@link Component} to sender.
     *
     * @param sender    Sender.
     * @param component Component to send.
     * @param prefix    Use prefix.
     */
    static void composeInteractive(CommandSender sender, Component component, boolean prefix) {
        component = composeComponent(component, prefix);
        sendMessage(sender, component);
    }

    /**
     * Send a {@link Component} to sender.
     *
     * @param sender     Sender.
     * @param base       String base to compose components.
     * @param prefix     Use prefix.
     * @param components components of message.
     */
    static void composeInteractive(CommandSender sender, Messages base, boolean prefix, Component... components) {
        Component component = composeComponent(getMessageTranslated(base), components, prefix);
        sendMessage(sender, component);
    }

    /**
     * Send a {@link Component} to sender.
     *
     * @param sender     Sender.
     * @param base       String base to compose components.
     * @param prefix     Use prefix.
     * @param components Components of message.
     */
    static void composeInteractive(CommandSender sender, String base, boolean prefix, Component... components) {
        Component component = composeComponent(base, components, prefix);
        sendMessage(sender, component);
    }

    /**
     * Send a {@link Component} to sender.
     *
     * @param sender     Sender.
     * @param base       String base to compose components.
     * @param prefix     Use prefix.
     * @param components components of message.
     */
    static void composeInteractive(CommandSender sender, Messages base, boolean prefix, TextComponent.Builder... components) {
        Component component = composeComponent(getMessageTranslated(base), components, prefix);
        sendMessage(sender, component);
    }

    /**
     * Send a {@link Component} to sender.
     *
     * @param sender     Sender.
     * @param base       String base to compose components.
     * @param prefix     Use prefix.
     * @param components components of message.
     */
    static void composeInteractive(CommandSender sender, String base, boolean prefix, TextComponent.Builder... components) {
        Component component = composeComponent(base, components, prefix);
        sendMessage(sender, component);
    }

    static void composeInteractive(Player sender, Messages base, boolean prefix, Component... components) {
        Component component = composeComponent(getMessageTranslated(base), components, prefix);
        sendMessage(sender, component);
    }

}
