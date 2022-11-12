package de.legoshi.linkcraft.command.map;


import de.legoshi.linkcraft.command.tag.*;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.ArgOrSub;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// /maps
// /maps help
// /maps add <name> <type> <difficulty> <builders>
// /maps setstart
// /maps settag <tag_id>
// /maps setend <sign:plate>
// /maps edit <name:type:difficulty:builders> <value>
// /maps remove <map-id>
// /maps setvictor <map-id> <user-name>
// /maps unsetvictor <map-id> <user-name:all>
@Command(names = "maps", desc = "%translatable:maps.desc%")
@SubCommandClasses({
        MapsAddCommand.class,
        MapsEditCommand.class,
        MapsHelpCommand.class,
        MapsRemoveCommand.class,
        MapsSetVictorCommand.class,
        MapsUnSetVictorCommand.class
})
@ArgOrSub(value = true)
public class MapsCommand implements CommandClass {

    @Command(names = "")
    public boolean maps(@Sender CommandSender sender, CommandContext commandContext) {
        if (commandContext.getArguments().size() > 1) {
            // send help
            return false;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage("no player");
            return false;
        }

        // open maps gui
        return true;
    }

}
