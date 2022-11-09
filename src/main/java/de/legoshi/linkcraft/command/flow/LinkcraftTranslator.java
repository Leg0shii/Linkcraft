package de.legoshi.linkcraft.command.flow;

import de.legoshi.linkcraft.util.message.MessageUtils;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.translator.TranslationProvider;
import me.fixeddev.commandflow.translator.Translator;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;

import java.util.function.Function;

public class LinkcraftTranslator implements Translator {

    private TranslationProvider provider;

    public LinkcraftTranslator() {
        this.provider = new LinkcraftTranslationProvider();
    }

    @Override
    public Component translate(Component component, Namespace namespace) {
        if (!(component instanceof TranslatableComponent)) {
            return component;
        }
        TranslatableComponent translatable = (TranslatableComponent) component;
        String translation = provider.getTranslation(namespace, translatable.key());
        return MessageUtils.composeComponent(translation, translatable.args(), false);
    }

    @Override
    public void setProvider(TranslationProvider provider) {
        this.provider = provider;
    }

    @Override
    public void setConverterFunction(Function<String, TextComponent> stringToComponent) {

    }

}
