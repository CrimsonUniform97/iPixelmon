package com.ipixelmon.gym;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.landcontrol.regions.Region;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.mysql.UpdateForm;
import com.ipixelmon.team.EnumTeam;
import com.ipixelmon.util.ArrayUtil;
import com.ipixelmon.util.PixelmonAPI;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumBattleType;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Defeating a Pokemon at a rival gym: -500 Prestige
 * Defeating all the Pokemon at a rival gym: -1,500 Prestige on top of the individual bonuses
 * <p>
 * If a gym loses enough prestige to drop down a level, the lowest-ranked Pokemon is dropped; if all of a gym's prestige is depleted, the team is kicked out altogether and the gym reverts to a neutral, unclaimed state.
 */

/**
 * You can be in as many as 10 gym at once, but you cannot employ more than one of your own Pokémon at a single gym.
 * Adding a Pokemon to a gym: +2,000 Prestige
 * Defeating a Pokemon with CP equal to or less than yours: +100 Prestige
 * Defeating a Pokemon with CP higher than yours: +500 Prestige
 * Defeating all the Pokemon at a gym: +50 Prestige on top of the individual bonuses
 */

public class Gym implements Comparable<Gym>{

    private static final int[] LEVELS = {0, 2000, 4000, 8000, 12000, 16000, 20000, 30000, 40000, 50000};

    private UUID id, regionID;
    private EnumTeam team = EnumTeam.None;
    private Map<UUID, EntityPixelmon> trainers = Maps.newHashMap();
    private List<BlockPos> seats = Lists.newArrayList();
    private int prestige = 0;
    private Map<UUID, Integer> que = Maps.newHashMap();
    private BlockPos startBattlePlate;

    private Set<EntityTrainer> trainerEntities = new TreeSet<>();

    public Gym(UUID id) {
        this.id = id;

        ResultSet result = iPixelmon.mysql.selectAllFrom(GymMod.class, new SelectionForm("Gyms")
                .where("id", id.toString()));

        try {
            if (result.next()) {
                this.regionID = UUID.fromString(result.getString("region"));
                parseSeats(result.getString("seats"));
                parseTrainers(result.getString("trainers"));
                team = EnumTeam.valueOf(result.getString("team"));
                prestige = result.getInt("prestige");
                String[] array = ArrayUtil.fromString(result.getString("startBattlePlate"));
                if (array.length == 3)
                    startBattlePlate = new BlockPos(Integer.parseInt(array[0]), Integer.parseInt(array[1]),
                            Integer.parseInt(array[2]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Gym(UUID id, UUID regionID) {
        this.id = id;
        this.regionID = regionID;
    }

    public UUID getID() {
        return id;
    }

    public int getLevel() {
        for (int i = LEVELS.length; i > 0; i--) {
            if (getPrestige() > LEVELS[i]) return i;
        }

        return 0;
    }

    public BlockPos getStartBattlePlate() {
        return startBattlePlate;
    }

    public void setStartBattlePlate(BlockPos pos) {
        String[] s = new String[]{String.valueOf(pos.getX()), String.valueOf(pos.getY()), String.valueOf(pos.getZ())};
        startBattlePlate = pos;
        setViaMySQL("startBattlePlate", ArrayUtil.toString(s));
    }

    public int getAvailableSeats() {
        return getLevel() - getTrainers().size();
    }

    public Region getRegion() {
        return LandControlAPI.Server.getRegion(regionID);
    }

    public EnumTeam getTeam() {
        return team;
    }

    public void setTeam(EnumTeam team) {
        this.team = team;
        setViaMySQL("team", team.name());
        updateColoredBlocks();
    }

    public Map<UUID, EntityPixelmon> getTrainers() {
        return trainers;
    }

    public void addTrainer(UUID player, EntityPixelmon pixelmon) {
        List<String> trainers = Lists.newArrayList();

        this.trainers.put(player, pixelmon);
        for (UUID p : this.trainers.keySet()) {
            trainers.add(p.toString() + ";" + PixelmonAPI.entityPixelmonToString(this.trainers.get(p)));
        }

        setViaMySQL("trainers", ArrayUtil.toString(trainers.toArray(new String[trainers.size()])));
    }

    public void removeTrainer(UUID player) {
        List<String> trainers = Lists.newArrayList();

        this.trainers.remove(player);
        for (UUID p : this.trainers.keySet()) {
            trainers.add(p.toString() + ";" + PixelmonAPI.entityPixelmonToString(this.trainers.get(p)));
        }

        setViaMySQL("trainers", ArrayUtil.toString(trainers.toArray(new String[trainers.size()])));
    }

    public List<BlockPos> getSeats() {
        return seats;
    }

    public void addSeat(BlockPos pos) {
        List<String> seats = Lists.newArrayList();
        this.seats.add(pos);
        for (BlockPos b : this.seats) {
            seats.add(b.getX() + ";" + b.getY() + ";" + b.getZ());
        }

        setViaMySQL("seats", ArrayUtil.toString(seats.toArray(new String[seats.size()])));
    }

    public void removeSeat(BlockPos pos) {
        List<String> seats = Lists.newArrayList();
        this.seats.remove(pos);
        for (BlockPos b : this.seats) {
            seats.add(b.getX() + ";" + b.getY() + ";" + b.getZ());
        }

        setViaMySQL("seats", ArrayUtil.toString(seats.toArray(new String[seats.size()])));
    }

    public Set<EntityTrainer> getTrainerEntities() {
        return trainerEntities;
    }

    public int getPrestige() {
        return prestige;
    }

    public void setPrestige(int prestige) {
        this.prestige = prestige;

        if (this.prestige < 0) {
            this.prestige = 0;
            setTeam(EnumTeam.None);
            removeTrainer((UUID) trainers.keySet().toArray()[trainers.size() - 1]);
        }

        setViaMySQL("prestige", String.valueOf(prestige));
    }

    public Map<UUID, Integer> getQue() {
        return que;
    }

    protected void setViaMySQL(String column, String value) {
        iPixelmon.mysql.update(GymMod.class, new UpdateForm("Gyms").set(column, value).where("id", id.toString()));
    }

    private void parseSeats(String str) {
        String[] array = ArrayUtil.fromString(str);

        for (String s : array) {
            if (!s.isEmpty()) {
                String[] data = s.split(";");
                seats.add(new BlockPos(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2])));
            }
        }
    }

    private void parseTrainers(String str) {
        String[] array = ArrayUtil.fromString(str);

        for (String s : array) {
            if (!s.isEmpty()) {
                String[] data = s.split(";");
                UUID player = UUID.fromString(data[0]);
                EntityPixelmon pixelmon = PixelmonAPI.entityPixelmonFromString(data[1], getRegion().getWorld());
                trainers.put(player, pixelmon);
            }
        }
    }

    public void updateColoredBlocks() {
        World world = getRegion().getWorld();
        AxisAlignedBB bounds = getRegion().getBounds();
        BlockPos pos;
        Block block;
        for (int x = (int) bounds.minX; x < bounds.maxX; x++) {
            for (int y = (int) bounds.minY; y < bounds.maxY; y++) {
                for (int z = (int) bounds.minZ; z < bounds.maxZ; z++) {
                    pos = new BlockPos(x, y, z);
                    if(world.getBlockState(pos) != null) {
                        block = world.getBlockState(pos).getBlock();

                        if(block == Blocks.wool) {
                            world.setBlockState(pos, Blocks.wool.getDefaultState().withProperty(BlockCarpet.COLOR,
                                    getTeam().colorDye()));
                        }else if (block == Blocks.stained_glass) {
                            world.setBlockState(pos, Blocks.stained_glass.getDefaultState().withProperty(BlockStainedGlass.COLOR,
                                    getTeam().colorDye()));
                        }
                    }
                }
            }
        }
    }

    public void loadEntities(WorldEvent.Load event) {
        if (event.world != getRegion().getWorld()) return;

        World world = event.world;
        for (EntityTrainer trainer : world.getEntitiesWithinAABB(EntityTrainer.class, getRegion().getBounds())) {
            world.removeEntity(trainer);
        }

        int i = 0;
        for (UUID trainer : trainers.keySet()) {
            trainerEntities.add(new EntityTrainer(world, seats.get(i), trainer, trainers.get(trainer)));
            i++;
        }

        for (EntityTrainer trainer : trainerEntities) world.spawnEntityInWorld(trainer);
    }

    public void battle(EntityPlayerMP player) {
        List<TrainerParticipant> trainers = Lists.newArrayList();
        List<PlayerParticipant> players = Lists.newArrayList();
// TODO: Make it to where player can claim gym...
        try {
            /**
             * Setup player pokemons
             */
            List<int[]> ablePlayerPixelmons = PixelmonStorage.PokeballManager.getPlayerStorage(player).getAllAblePokemonIDs();
            EntityPixelmon[] playerPixelmons = new EntityPixelmon[ablePlayerPixelmons.size()];
            for (int i = 0; i < ablePlayerPixelmons.size(); i++) {
                int[] id = ablePlayerPixelmons.get(i);
                playerPixelmons[i] = PixelmonStorage.PokeballManager.getPlayerStorage(player).getPokemon(id, player.worldObj);
            }
            players.add(new PlayerParticipant(player, playerPixelmons));

            /**
             * Setup trainer pokemons
             */
            for (EntityTrainer trainer : trainerEntities) {
                trainers.add(new TrainerParticipant(trainer, player, 1));
            }

            /**
             * Start battle
             */
            for (PlayerParticipant p : players) p.startedBattle = true;

            new BattleController(players.toArray(new PlayerParticipant[players.size()]),
                    trainers.toArray(new TrainerParticipant[trainers.size()]), EnumBattleType.Single);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SideOnly(Side.CLIENT)
    public static Gym fromBytes(ByteBuf buf) {
        UUID id = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        UUID region = UUID.fromString(ByteBufUtils.readUTF8String(buf));

        Gym gym = new Gym(id, region);
        gym.team =  EnumTeam.valueOf(ByteBufUtils.readUTF8String(buf));
        gym.prestige = buf.readInt();

        int trainersSize = buf.readInt();

        for(int i = 0; i < trainersSize; i++) {
            String[] data = ByteBufUtils.readUTF8String(buf).split(",");
            gym.trainers.put(UUID.fromString(data[0]),
                    PixelmonAPI.entityPixelmonFromString(data[1], Minecraft.getMinecraft().theWorld));
        }

        return gym;
    }

    @SideOnly(Side.SERVER)
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, id.toString());
        ByteBufUtils.writeUTF8String(buf, regionID.toString());
        ByteBufUtils.writeUTF8String(buf, team.name());
        buf.writeInt(prestige);
        buf.writeInt(trainers.size());

        for(UUID id : trainers.keySet()) {
            ByteBufUtils.writeUTF8String(buf, id.toString() + "," + PixelmonAPI.entityPixelmonToString(trainers.get(id)));
        }
    }

    @Override
    public int compareTo(Gym o) {
        return id.equals(o.id) || regionID.equals(o.regionID) ? 0 : -999;
    }
}
