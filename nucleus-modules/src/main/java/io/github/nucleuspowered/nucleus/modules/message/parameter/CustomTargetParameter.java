package io.github.nucleuspowered.nucleus.modules.message.parameter;

import io.github.nucleuspowered.nucleus.api.module.message.target.MessageTarget;
import io.github.nucleuspowered.nucleus.modules.message.services.MessageHandler;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.exception.ArgumentParseException;
import org.spongepowered.api.command.parameter.ArgumentReader;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class CustomTargetParameter implements ValueParameter<MessageTarget> {

    private final MessageHandler messageHandler;

    public CustomTargetParameter(final MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public List<String> complete(final CommandContext context, final String currentInput) {
        return this.messageHandler.getCustomTargets().stream().filter(x -> x.startsWith(currentInput)).collect(Collectors.toList());
    }

    @Override
    public Optional<? extends MessageTarget> getValue(final Parameter.Key<? super MessageTarget> parameterKey, final ArgumentReader.Mutable reader,
            final CommandContext.Builder context) throws ArgumentParseException {
        final Optional<MessageTarget> messageTarget = this.messageHandler.getTarget(reader.parseString());
        if (messageTarget.isPresent()) {
            return messageTarget;
        }
        throw reader.createException(Component.text("Target does not exist."));
    }
}
