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
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.comm.SetTrainerData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumEncounterMode;
import com.pixelmonmod.pixelmon.enums.EnumPokeballs;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import net.minecraft.block.BlockCarpet;
import net.minecraft.client.renderer.entity.layers.LayerSheepWool;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Gym {

    private UUID regionID;
    private String name;
    private int power;
    private EnumTeam team;
    private Map<UUID, EntityPixelmon> pokemon;
    private List<BlockPos> displayBlocks;
    private int[] levels = {0, 2000, 4000, 8000, 12000, 16000, 20000, 30000, 40000, 50000};
    private Map<UUID, UUID> battles;

    public Gym(UUID regionID) throws Exception {
        ResultSet result = iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms").add("regionID", regionID.toString()));

        if (!result.next()) {
            throw new Exception("Gym not found.");
        }

        this.regionID = regionID;

        setName(result.getString("name"));
        setPower(result.getInt("power"));
        setTeam(EnumTeam.valueOf(result.getString("team")));
        setPokemon(getPokemon());
        setDisplayBlocks(getDisplayBlocks());
        battles = new HashMap<UUID, UUID>();
    }

    public UUID getRegionID() {
        return regionID;
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

    public void setPokemon(Map<UUID, EntityPixelmon> pokemon) {
        this.pokemon = pokemon;

        StringBuilder builder = new StringBuilder();
        EntityPixelmon pixelmon;
        for (UUID uuid : pokemon.keySet()) {
            pixelmon = pokemon.get(uuid);

            builder.append(uuid.toString());
            builder.append(",");
            builder.append(pixelmon.getPokemonName());
            builder.append(",");
            builder.append(pixelmon.getIsShiny());
            builder.append(",");
            builder.append(pixelmon.getForm());
            builder.append(",");
            builder.append(pixelmon.getLvl().getLevel());
            builder.append(";");
        }

        if (!builder.toString().isEmpty()) {
            builder.deleteCharAt(builder.length() - 1);
        }

        iPixelmon.mysql.update(Gyms.class, new UpdateForm("Gyms").set("pokemon", builder.toString()).where("name", getName()));
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

            iPixelmon.mysql.update(Gyms.class, new UpdateForm("Gyms").set("displayblocks", builder.toString()).where("regionID", regionID.toString()));
        }
    }

    public List<BlockPos> getDisplayBlocks() {
        if (displayBlocks == null) {
            displayBlocks = new ArrayList<BlockPos>();
            ResultSet result = iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms").add("regionID", regionID.toString()));

            try {
                if (result.next()) {
                    if (result.getString("displayblocks") != null && !result.getString("displayblocks").isEmpty()) {
                        String[] blockData;
                        for (String s : result.getString("displayblocks").split(";")) {
                            blockData = s.split(",");
                            displayBlocks.add(new BlockPos(Integer.valueOf(blockData[0]), Integer.valueOf(blockData[1]), Integer.valueOf(blockData[2])));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return displayBlocks;
    }

    public Map<UUID, EntityPixelmon> getPokemon() {
        if (pokemon == null) {
            pokemon = new HashMap<UUID, EntityPixelmon>();
            ResultSet result = iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms").add("name", name));

            try {
                if (result.next()) {

                    if (result.getString("pokemon") == null || result.getString("pokemon").isEmpty()) {
                        return pokemon;
                    }

                    EntityPixelmon pixelmon;
                    UUID playerUUID;
                    Iterator<String> pokemonIter = Splitter.on(";").split(result.getString("pokemon")).iterator();
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
                        pokemon.put(playerUUID, pixelmon);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return pokemon;
    }

    @SideOnly(Side.SERVER)
    public static Gym createGym(World world, BlockPos pos, int power, EnumTeam team, String name) throws Exception {
        Region region = new Region(world, pos);
        if (name == null || name.isEmpty()) {
            throw new Exception("Name is invalid.");
        }

        ResultSet result = iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms").add("regionID", region.getUUID().toString()));

        if (result.next()) {
            throw new Exception("There is already a gym here.");
        }

        result = iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms").add("name", name));

        if (result.next()) {
            throw new Exception("There is already a gym with that name.");
        }

        InsertForm gymForm = new InsertForm("Gyms");
        gymForm.add("name", name);
        gymForm.add("regionID", region.getUUID().toString());
        gymForm.add("power", power);
        gymForm.add("team", team.name());
        gymForm.add("pokemon", "");
        iPixelmon.mysql.insert(Gyms.class, gymForm);

        return new Gym(region.getUUID());
    }

    @SideOnly(Side.SERVER)
    public boolean delete() {
        iPixelmon.mysql.delete(Gyms.class, new DeleteForm("Gyms").add("regionID", regionID.toString()));

        try {
            ResultSet result = iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms").add("regionID", regionID.toString()));

            if (!result.next()) {
                throw new Exception("Gym not found.");
            }
        } catch (Exception e) {
            return true;
        }

        return false;
    }

    @SideOnly(Side.SERVER)
    public void updateWool() {
        try {
            Region region = new Region(regionID);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * You can be in as many as 10 gyms at once, but you cannot employ more than one of your own Pokémon at a single gym.
     *
     * Adding a Pokemon to a gym: +2,000 Prestige
     * Defeating a Pokemon with CP equal to or less than yours: +100 Prestige
     * Defeating a Pokemon with CP higher than yours: +500 Prestige
     * Defeating all the Pokemon at a gym: +50 Prestige on top of the individual bonuses
     */
    @SideOnly(Side.SERVER)
    public void initiateBattle(EntityPlayerMP player) {
        UUID gymLeader = (UUID) pokemon.keySet().toArray()[pokemon.keySet().size() - 1];

        if(battles.containsKey(player.getUniqueID())) {
            for(int i = 0; i < pokemon.keySet().size(); i++) {
                UUID playerUUID = (UUID) pokemon.keySet().toArray()[i];

                if(playerUUID.equals(battles.get(player.getUniqueID()))) {
                    battles.put(player.getUniqueID(), gymLeader = (UUID) pokemon.keySet().toArray()[i - 1]);
                }
            }
        } else {
            battles.put(player.getUniqueID(), gymLeader);
        }

        EntityGymLeader trainer = new EntityGymLeader(player.getEntityWorld());
        trainer.setPlayerUUID(gymLeader);
        trainer.update(new SetTrainerData("Name", "Greeting", "Win", "Loss", 12, new ItemStack[]{}));
        trainer.setPosition(player.posX, player.posY, player.posZ);
        trainer.setEncounterMode(EnumEncounterMode.Unlimited);
        trainer.loadPokemon(pokemon.get(gymLeader));

        player.getEntityWorld().spawnEntityInWorld(trainer);

        try {
            PlayerParticipant player1;
            TrainerParticipant player2;

            EntityPixelmon e = PixelmonStorage.PokeballManager.getPlayerStorage(player).getFirstAblePokemon(player.worldObj);
            player1 = new PlayerParticipant(player, new EntityPixelmon[]{e});
            player2 = new TrainerParticipant(trainer, player, 1);
            player1.startedBattle = true;
            BattleParticipant[] team1 = new BattleParticipant[]{player1};
            BattleParticipant[] team2 = new BattleParticipant[]{player2};
            new BattleControllerBase(team1, team2);

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
