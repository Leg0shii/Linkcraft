package de.legoshi.linkcraft.command.flow;

import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.translator.TranslationProvider;

import java.util.HashMap;
import java.util.Map;

public class LinkcraftTranslationProvider implements TranslationProvider {

    private final Map<String, Messages> translations = new HashMap<>();

    public LinkcraftTranslationProvider() {
        /* this.translations.put("sender.unknown", Messages.ERROR_FATAL);
        this.translations.put("sender.only-player", Messages.ONLY_PLAYER);
        */
        // Default translations
        this.translations.put("command.subcommand.invalid", Messages.INVALID_COMMAND);
        this.translations.put("command.no-permission", Messages.PLAYER_NO_PERMISSION);
        this.translations.put("argument.no-more", Messages.INVALID_ARG_LENGTH);
        this.translations.put("command.tag-usage", Messages.COMMAND_SYNTAX);

        // Getting all the enum's name descriptions of commands
        for (Messages message : Messages.values()) {
            String category = message.getCategory();
            if (category.equals("description")) {
                String name = message.name().toLowerCase().replace("_", ".");
                this.translations.put(name, message);
            }
        }
    }

    @Override
    public String getTranslation(Namespace namespace, String string) {
        Messages message = translations.getOrDefault(string, Messages.ERROR_FATAL);
        return MessageUtils.getMessageTranslated(message);
    }

    public Map<String, Messages> getTranslations() {
        return translations;
    }

}
