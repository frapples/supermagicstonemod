package io.github.frapples.supermagicstonemod.SuperThings;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

/**
 * Created by minecraft on 17-2-28.
 */
public class CommandSuperStone extends CommandBase {
    public String getCommandName() {
        return "setSuperStoneName";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName() + " " + "<name>";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            sender.addChatMessage(new ChatComponentText("Usage: " + getCommandUsage(sender)));
            return;
        }

        EntityPlayerMP entityPlayerMP = CommandBase.getCommandSenderAsPlayer(sender);

        ItemStack stack = entityPlayerMP.getCurrentEquippedItem();
        if (stack != null && stack.getItem() instanceof SuperStone) {
            ((SuperStone)stack.getItem()).setLabel(stack, args[0]);
            sender.addChatMessage(new ChatComponentTranslation(
                    "setSuperStoneNameCommand.success"));

        } else {
            sender.addChatMessage(new ChatComponentTranslation(
                    "setSuperStoneNameCommand.NotSuperStoneInHand"));
        }

    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 1;
    }
}
