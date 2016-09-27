package ipixelmon.gyms.server;

import com.mojang.authlib.GameProfile;
import ipixelmon.gyms.Gym;
import ipixelmon.landcontrol.Region;
import ipixelmon.teams.EnumTeam;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

public class CommandGym implements ICommand {
    @Override
    public String getCommandName() {
        return "gym";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/gym create|delete <name>";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<String>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        EntityPlayer player = (EntityPlayer) sender;

        if (!MinecraftServer.getServer().getConfigurationManager().canSendCommands(new GameProfile(player.getUniqueID(), player.getName()))) {
            player.addChatComponentMessage(new ChatComponentText("You do not have permission for that command."));
            return;
        }

        if (args.length == 0) {
            player.addChatComponentMessage(new ChatComponentText(getCommandUsage(sender)));
            return;
        }

        String cmd = args[0].toLowerCase();

        if (cmd.equalsIgnoreCase("create")) {
            try {
                if (args.length != 2) {
                    player.addChatComponentMessage(new ChatComponentText(getCommandUsage(sender)));
                }

                Gym.createGym(player.worldObj, player.getPosition(), 0, EnumTeam.None, args[1]);
            } catch (Exception e) {
                player.addChatComponentMessage(new ChatComponentText(e.getMessage()));
            }

            player.addChatComponentMessage(new ChatComponentText("Gym created."));

        } else if (cmd.equalsIgnoreCase("delete")) {
            try {
                Region region = new Region(player.worldObj, player.getPosition());
                Gym gym = new Gym(region.getUUID());
                Gym.deleteGym(gym);
            } catch (Exception e) {
                player.addChatComponentMessage(new ChatComponentText(e.getMessage()));
                e.printStackTrace();
            }

            player.addChatComponentMessage(new ChatComponentText("Gym deleted."));
        } else {
            player.addChatComponentMessage(new ChatComponentText(getCommandUsage(sender)));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender instanceof EntityPlayer;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
