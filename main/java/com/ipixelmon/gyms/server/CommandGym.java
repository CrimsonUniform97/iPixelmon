package com.ipixelmon.gyms.server;

import com.ipixelmon.gyms.EntityGymLeader;
import com.ipixelmon.landcontrol.Region;
import com.ipixelmon.teams.EnumTeam;
import com.mojang.authlib.GameProfile;
import com.ipixelmon.gyms.Gym;
import com.ipixelmon.landcontrol.Region;
import com.ipixelmon.teams.EnumTeam;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.achievement.PixelmonAchievements;
import com.pixelmonmod.pixelmon.api.enums.ReceiveType;
import com.pixelmonmod.pixelmon.api.events.PixelmonRecievedEvent;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.comm.SetTrainerData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.GymNPCData;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumBattleAIMode;
import com.pixelmonmod.pixelmon.enums.EnumEncounterMode;
import com.pixelmonmod.pixelmon.enums.EnumPokeballs;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.worldGeneration.structure.gyms.GymInfo;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        EntityPlayerMP player = (EntityPlayerMP) sender;

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
                new Gym(region.getUUID()).delete();
            } catch (Exception e) {
                player.addChatComponentMessage(new ChatComponentText(e.getMessage()));
                e.printStackTrace();
            }

            player.addChatComponentMessage(new ChatComponentText("Gym deleted."));
        } else if (cmd.equalsIgnoreCase("battle")) {
            EntityGymLeader trainer = new EntityGymLeader(player.getEntityWorld());
            trainer.setPlayerUUID(player.getUniqueID());
            trainer.update(new SetTrainerData("Name", "Greeting", "Win", "Loss", 12, new ItemStack[]{}));
            trainer.setPosition(player.posX, player.posY, player.posZ);
            trainer.setEncounterMode(EnumEncounterMode.Unlimited);
            ArrayList<EnumPokemon> pokeList = new ArrayList<EnumPokemon>();
            pokeList.add(EnumPokemon.Abra);
            pokeList.add(EnumPokemon.Aggron);
            trainer.loadPokemon(pokeList);

            player.getEntityWorld().spawnEntityInWorld(trainer);

            try {
                PlayerParticipant player1;
                TrainerParticipant player2;

                EntityPixelmon e = PixelmonStorage.PokeballManager.getPlayerStorage(player).getFirstAblePokemon(player.worldObj);
                player1 = new PlayerParticipant(player, new EntityPixelmon[]{e});
                player2 = new TrainerParticipant(trainer, player, pokeList.size() - 1);
                player1.startedBattle = true;
                BattleParticipant[] team1 = new BattleParticipant[]{player1};
                BattleParticipant[] team2 = new BattleParticipant[]{player2};
                new BattleControllerBase(team1, team2);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (cmd.equalsIgnoreCase("give")) {
            try {
                Region region = new Region(player.getEntityWorld(), player.getPosition());

                Gym gym = new Gym(region.getUUID());

                EntityPixelmon pixelmon = (EntityPixelmon) PixelmonEntityList.createEntityByName(EnumPokemon.Rayquaza.name, MinecraftServer.getServer().getEntityWorld());
                pixelmon.setHealth(100);
                pixelmon.setIsShiny(true);
                pixelmon.setForm(80);
                pixelmon.getLvl().setLevel(65);
                pixelmon.caughtBall = EnumPokeballs.PokeBall;
                pixelmon.friendship.initFromCapture();

                Map<UUID, EntityPixelmon> pokemon = gym.getPokemon();
                pokemon.put(player.getUniqueID(), pixelmon);

                gym.setPokemon(pokemon);
            } catch (Exception e) {
                player.addChatComponentMessage(new ChatComponentText(e.getMessage()));
            }
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
