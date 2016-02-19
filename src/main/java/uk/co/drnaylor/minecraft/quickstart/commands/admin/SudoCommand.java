package uk.co.drnaylor.minecraft.quickstart.commands.admin;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MutableMessageChannel;
import org.spongepowered.api.text.format.TextColors;
import uk.co.drnaylor.minecraft.quickstart.Util;
import uk.co.drnaylor.minecraft.quickstart.api.PluginModule;
import uk.co.drnaylor.minecraft.quickstart.internal.CommandBase;
import uk.co.drnaylor.minecraft.quickstart.internal.annotations.Modules;
import uk.co.drnaylor.minecraft.quickstart.internal.annotations.Permissions;

@Modules(PluginModule.ADMIN)
@Permissions
public class SudoCommand extends CommandBase {
    private final String playerKey = "player";
    private final String commandKey = "command";

    @Override
    public CommandSpec createSpec() {
        return CommandSpec.builder().arguments(
                GenericArguments.onlyOne(GenericArguments.player(Text.of(playerKey))),
                GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Text.of(commandKey)))
        ).executor(this).build();
    }

    @Override
    public String[] getAliases() {
        return new String[] { "sudo"};
    }

    @Override
    public CommandResult executeCommand(CommandSource src, CommandContext args) throws Exception {
        Player pl = args.<Player>getOne(playerKey).get();
        String cmd = args.<String>getOne(commandKey).get();

        if (cmd.startsWith("c:")) {
            if (cmd.equals("c:")) {
                src.sendMessage(Text.of(Util.getMessageWithFormat("command.sudo.chatfail")));
                return CommandResult.empty();
            }

            MutableMessageChannel mmc = MessageChannel.TO_ALL.asMutable();
            mmc.send(pl, Text.of(cmd.split(":", 2)[1]));
            return CommandResult.success();
        }

        src.sendMessage(Text.of(TextColors.GREEN, Util.getMessageWithFormat("command.sudo.force", pl.getName(), cmd)));
        Sponge.getCommandManager().process(pl, cmd);
        return CommandResult.success();
    }
}
