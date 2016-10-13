package com.ipixelmon.gyms;

import com.google.common.base.Splitter;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.Region;
import com.ipixelmon.mysql.DeleteForm;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.mysql.UpdateForm;
import com.ipixelmon.teams.EnumTeam;
import com.ipixelmon.mysql.InsertForm;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPokeballs;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import net.minecraft.block.BlockCarpet;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Exchanger;

public class Gym {

    private Region region;
    private String name;
    private int power;
    private EnumTeam team;
    private List<EntityGymLeader> gymLeaders;
    private int[] levels = {0, 2000, 4000, 8000, 12000, 16000, 20000, 30000, 40000, 50000};
    private Map<EntityPlayerMP, BattleControllerBase> battles;
    private List<BlockPos> displayBlocks;

    protected Gym(Region region) throws Exception {
        ResultSet result = iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms").add("regionID", region.id().toString()));
        try {
            this.region = region;
            if (result.next()) {
                setName(result.getString("name"));
                setPower(result.getInt("power"));
                setTeam(EnumTeam.valueOf(result.getString("team")));
                setGymLeaders(getGymLeaders());
                setDisplayBlocks(getDisplayBlocks());
            } else {
                throw new Exception("Gym not found there.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getAvailableSlots() {
        return getLevel() - gymLeaders.size();
    }

    public int getFilledSlots() {
        return gymLeaders.size();
    }

    public int getLevel() {
        int count = 0;
        for (int level : levels) {
            count++;
            if (getPower() <= level) {
                break;
            }
        }

        return count;
    }

    public Region getRegion() {
        return region;
    }

    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }

    public EnumTeam getTeam() {
        return team;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setTeam(EnumTeam team) {
        this.team = team;
    }

    public void setGymLeaders(List<EntityGymLeader> gymLeaders) {
        this.gymLeaders = gymLeaders;

        StringBuilder builder = new StringBuilder();
        EntityPixelmon pixelmon;
        BlockPos location;
        int count = 0;
        for (EntityGymLeader gymLeader : gymLeaders) {
            pixelmon = gymLeader.getPokemonStorage().getFirstAblePokemon(region.getWorldServer());

            if (count >= getDisplayBlocks().size()) break;

            location = getDisplayBlocks().get(count);

            builder.append(gymLeader.getPlayerUUID().toString());
            builder.append(",");
            builder.append(pixelmon.getPokemonName());
            builder.append(",");
            builder.append(pixelmon.getIsShiny());
            builder.append(",");
            builder.append(pixelmon.getForm());
            builder.append(",");
            builder.append(pixelmon.getLvl().getLevel());
            builder.append(",");
            builder.append(location.getX());
            builder.append(",");
            builder.append(location.getY());
            builder.append(",");
            builder.append(location.getZ());
            builder.append(";");
            count++;
        }

        if (!builder.toString().isEmpty()) {
            builder.deleteCharAt(builder.length() - 1);
        }

        iPixelmon.mysql.update(Gyms.class, new UpdateForm("Gyms").set("gymLeaders", builder.toString()).where("name", getName()));
    }

    public void setDisplayBlocks(List<BlockPos> displayBlocks) {
        this.displayBlocks = displayBlocks;

        StringBuilder builder = new StringBuilder();

        Iterator<BlockPos> iterator = displayBlocks.listIterator();

        BlockPos pos;
        while (iterator.hasNext()) {
            pos = iterator.next();

            builder.append(pos.getX() + "," + pos.getY() + "," + pos.getZ());
            builder.append(";");
        }

        if (builder.length() != 0) {
            builder.deleteCharAt(builder.length() - 1);

            iPixelmon.mysql.update(Gyms.class, new UpdateForm("Gyms").set("displayblocks", builder.toString()).where("regionID", region.id().toString()));
        }
    }

    public List<BlockPos> getDisplayBlocks() {
        if (displayBlocks == null) {
            displayBlocks = new ArrayList<BlockPos>();
            ResultSet result = iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms").add("regionID", region.id().toString()));

            try {
                if (result.next()) {
                    if (result.getString("displayblocks") != null && !result.getString("displayblocks").isEmpty()) {
                        String[] blockData;
                        for (String s : result.getString("displayblocks").split(";")) {
                            blockData = s.split(",");
                            displayBlocks.add(new BlockPos(Double.valueOf(blockData[0]), Double.valueOf(blockData[1]), Double.valueOf(blockData[2])));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return displayBlocks;
    }

    public List<EntityGymLeader> getGymLeaders() {
        if (gymLeaders == null) {
            gymLeaders = new ArrayList<>();
            ResultSet result = iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms").add("name", name));

            try {
                if (result.next()) {

                    if (result.getString("gymLeaders") == null || result.getString("gymLeaders").isEmpty()) {
                        return gymLeaders;
                    }

                    EntityPixelmon pixelmon;
                    UUID playerUUID;

                    if(!result.getString("gymLeaders").contains(";")) {
                        Iterator<String> pokedataIter = Splitter.on(",").split(pokemonIter.next()).iterator();

                        playerUUID = UUID.fromString(pokedataIter.next());

                        pixelmon = (EntityPixelmon) PixelmonEntityList.createEntityByName(pokedataIter.next(), MinecraftServer.getServer().getEntityWorld());
                        pixelmon.setHealth(pixelmon.getMaxHealth());
                        pixelmon.setIsShiny(Boolean.valueOf(pokedataIter.next()));
                        pixelmon.setForm(Integer.valueOf(pokedataIter.next()));
                        pixelmon.getLvl().setLevel(Integer.valueOf(pokedataIter.next()));
                        pixelmon.caughtBall = EnumPokeballs.PokeBall;
                        pixelmon.friendship.initFromCapture();

                        gymLeaders.add(new EntityGymLeader(region.getWorldServer(), new BlockPos(Integer.valueOf(pokedataIter.next()), Integer.valueOf(pokedataIter.next()), Integer.valueOf(pokedataIter.next())), pixelmon, playerUUID));
                    }

                    Iterator<String> pokemonIter = Splitter.on(";").split(result.getString("gymLeaders")).iterator();
                    while (pokemonIter.hasNext()) {
                        Iterator<String> pokedataIter = Splitter.on(",").split(pokemonIter.next()).iterator();

                        playerUUID = UUID.fromString(pokedataIter.next());

                        pixelmon = (EntityPixelmon) PixelmonEntityList.createEntityByName(pokedataIter.next(), MinecraftServer.getServer().getEntityWorld());
                        pixelmon.setHealth(pixelmon.getMaxHealth());
                        pixelmon.setIsShiny(Boolean.valueOf(pokedataIter.next()));
                        pixelmon.setForm(Integer.valueOf(pokedataIter.next()));
                        pixelmon.getLvl().setLevel(Integer.valueOf(pokedataIter.next()));
                        pixelmon.caughtBall = EnumPokeballs.PokeBall;
                        pixelmon.friendship.initFromCapture();

                        gymLeaders.add(new EntityGymLeader(region.getWorldServer(), new BlockPos(Integer.valueOf(pokedataIter.next()), Integer.valueOf(pokedataIter.next()), Integer.valueOf(pokedataIter.next())), pixelmon, playerUUID));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return gymLeaders;
    }

    @SideOnly(Side.SERVER)
    public void update() {
        try {
            BlockPos pos;
            for (int x = region.getMin().getX(); x <= region.getMax().getX(); x++) {
                for (int y = region.getMin().getY(); y <= region.getWorldServer().getHeight(); y++) {
                    for (int z = region.getMin().getZ(); z <= region.getMax().getZ(); z++) {
                        pos = new BlockPos(x, y, z);
                        if (region.getWorldServer().getBlockState(pos) != null) {
                            if (region.getWorldServer().getBlockState(pos).getBlock() == Blocks.wool) {
                                region.getWorldServer().setBlockState(pos, Blocks.wool.getDefaultState().withProperty(BlockCarpet.COLOR, getTeam().colorDye()));
                            }
                        }
                    }
                }
            }

            World world = region.getWorldServer();

            for (EntityGymLeader gymLeader : getGymLeaders()) {
                    world.spawnEntityInWorld(gymLeader);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: Test it out.

    /**
     * You can be in as many as 10 gyms at once, but you cannot employ more than one of your own Pokémon at a single gym.
     * <p>
     * Adding a Pokemon to a gym: +2,000 Prestige
     * Defeating a Pokemon with CP equal to or less than yours: +100 Prestige
     * Defeating a Pokemon with CP higher than yours: +500 Prestige
     * Defeating all the Pokemon at a gym: +50 Prestige on top of the individual bonuses
     */
    @SideOnly(Side.SERVER)
    public void initiateBattle(EntityPlayerMP player) {
        if (gymLeaders.isEmpty()) {
            player.addChatComponentMessage(new ChatComponentText("There are no gym leaders occupying this gym."));
            return;
        }

        EntityGymLeader gymLeader = gymLeaders.get(gymLeaders.size() - 1);

        if (battles.containsKey(player)) {
            for (int i = 0; i < gymLeaders.size(); i++) {
                TrainerParticipant trainerParticipant = (TrainerParticipant) battles.get(player).participants.get(1);
                if (gymLeaders.get(i).equals(trainerParticipant.trainer))
                    if (i - 1 > -1)
                        gymLeader = gymLeaders.get(i - 1);
            }
        }

        try {
            PlayerParticipant player1;
            TrainerParticipant player2;

            EntityPixelmon e = PixelmonStorage.PokeballManager.getPlayerStorage(player).getFirstAblePokemon(player.worldObj);
            player1 = new PlayerParticipant(player, new EntityPixelmon[]{e});
            player2 = new TrainerParticipant(gymLeader, player, 1);
            player1.startedBattle = true;
            BattleParticipant[] team1 = new BattleParticipant[]{player1};
            BattleParticipant[] team2 = new BattleParticipant[]{player2};
            battles.put(player, new BattleControllerBase(team1, team2));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Defeating a Pokemon at a rival gym: -500 Prestige
     * Defeating all the Pokemon at a rival gym: -1,500 Prestige on top of the individual bonuses
     *
     * If a gym loses enough prestige to drop down a level, the lowest-ranked Pokemon is dropped; if all of a gym's prestige is depleted, the team is kicked out altogether and the gym reverts to a neutral, unclaimed state.
     */

}
