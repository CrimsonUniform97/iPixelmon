package com.ipixelmon.gym;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.landcontrol.regions.Region;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.mysql.UpdateForm;
import com.ipixelmon.team.EnumTeam;
import com.ipixelmon.team.TeamMod;
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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
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

public class Gym implements Comparable<Gym> {

    public static final int[] LEVELS = {0, 2000, 4000, 8000, 12000, 16000, 20000, 30000, 40000, 50000};

    private UUID id, regionID;
    private EnumTeam team = EnumTeam.None;
    private Map<UUID, EntityPixelmon> trainers = Maps.newHashMap();
    private Map<BlockPos, Float> seats = Maps.newHashMap();
    private int prestige = 0;
    private Set<UUID> que = new TreeSet<>();
    private BlockPos teleportPos;

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
                String[] array = ArrayUtil.fromString(result.getString("teleportPos"));
                if (array.length == 3)
                    teleportPos = new BlockPos(Integer.parseInt(array[0]), Integer.parseInt(array[1]),
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
        for (int i = LEVELS.length - 1; i > 0; i--) {
            if (getPrestige() >= LEVELS[i]) return i;
        }

        return 0;
    }

    public BlockPos getTeleportPos() {
        return teleportPos;
    }

    public void setTeleportPos(BlockPos pos) {
        String[] s = new String[]{String.valueOf(pos.getX()), String.valueOf(pos.getY()), String.valueOf(pos.getZ())};
        teleportPos = pos;
        setViaMySQL("teleportPos", ArrayUtil.toString(s));
    }

    public int getAvailableSeats() {
        if(getSeats().size() > getLevel()) {
            return getLevel() + 1 - getTrainers().size();
        } else {
            return getSeats().size() - getTrainers().size();
        }
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

        if (this.trainers.isEmpty()) {
            reset();
            setTeam(TeamMod.getPlayerTeam(player));
            updateColoredBlocks();
        }

        this.trainers.put(player, pixelmon);
        for (UUID p : this.trainers.keySet()) {
            trainers.add(p.toString() + ";" + PixelmonAPI.pixelmonToString(this.trainers.get(p)));
        }

        setPrestige(getPrestige() + 2000);

        setViaMySQL("trainers", ArrayUtil.toString(trainers.toArray(new String[trainers.size()])));
    }

    public void removeTrainer(UUID player) {
        List<String> trainers = Lists.newArrayList();

        this.trainers.remove(player);
        for (UUID p : this.trainers.keySet()) {
            trainers.add(p.toString() + ";" + PixelmonAPI.pixelmonToString(this.trainers.get(p)));
        }

        setViaMySQL("trainers", ArrayUtil.toString(trainers.toArray(new String[trainers.size()])));
    }

    public Map<BlockPos, Float> getSeats() {
        return seats;
    }

    public void addSeat(BlockPos pos, float yaw) {
        List<String> seats = Lists.newArrayList();

        int direction = MathHelper.floor_double((double) (yaw * 4.0F / 360.0F) + 0.5D) & 3;

        float[] numbers = new float[]{0, 90, 180, -90};

        this.seats.put(pos, numbers[direction]);
        for (BlockPos b : this.seats.keySet()) {
            seats.add(b.getX() + ";" + b.getY() + ";" + b.getZ() + ";" + numbers[direction]);
        }

        setViaMySQL("seats", ArrayUtil.toString(seats.toArray(new String[seats.size()])));
    }

    public void removeSeat(BlockPos pos) {
        List<String> seats = Lists.newArrayList();
        this.seats.remove(pos);
        for (BlockPos b : this.seats.keySet()) {
            seats.add(b.getX() + ";" + b.getY() + ";" + b.getZ() + ";" + this.seats.get(b));
        }

        setViaMySQL("seats", ArrayUtil.toString(seats.toArray(new String[seats.size()])));
    }

    public Set<EntityTrainer> getTrainerEntities() {
        return trainerEntities;
    }

    public int getPrestige() {
        return prestige;
    }

    // TODO: Test batteling two trainers at gym

    public void setPrestige(int prestige) {
        int prevLevel = getLevel();

        if(prestige <= 0) {
            reset();
            return;
        }

        this.prestige = prestige;
        this.prestige = this.prestige < 0 ? 0 : this.prestige > LEVELS[LEVELS.length - 1] ? LEVELS[LEVELS.length - 1] : this.prestige;

        int postLevel = getLevel();

        if (postLevel < prevLevel && postLevel != 0) {
            removeTrainer((UUID) trainers.keySet().toArray()[trainers.size() - 1]);
        }

        if (trainers.isEmpty()) {
            setTeam(EnumTeam.None);
        }

        setViaMySQL("prestige", String.valueOf(this.prestige));
    }

    public void reset() {
        for(EntityTrainer trainer : getTrainerEntities()) getRegion().getWorld().removeEntity(trainer);
        getTrainerEntities().clear();

        for(UUID id : getTrainers().keySet()) removeTrainer(id);

        this.prestige = 0;

        setTeam(EnumTeam.None);

        updateColoredBlocks();
    }

    public Set<UUID> getQue() {
        return que;
    }

    public UUID getNextInQue() {
        if (que.isEmpty()) {
            return null;
        }
        return (UUID) que.toArray()[0];
    }

    protected void setViaMySQL(String column, String value) {
        iPixelmon.mysql.update(GymMod.class, new UpdateForm("Gyms").set(column, value).where("id", id.toString()));
    }

    private void parseSeats(String str) {
        String[] array = ArrayUtil.fromString(str);

        for (String s : array) {
            if (!s.isEmpty()) {
                String[] data = s.split(";");
                seats.put(new BlockPos(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2])),
                        Float.parseFloat(data[3]));
            }
        }
    }

    private void parseTrainers(String str) {
        String[] array = ArrayUtil.fromString(str);

        for (String s : array) {
            if (!s.isEmpty()) {
                String[] data = s.split(";");
                UUID player = UUID.fromString(data[0]);
                trainers.put(player, PixelmonAPI.pixelmonFromString(data[1], getRegion().getWorld()));
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
                    if (world.getBlockState(pos) != null) {
                        block = world.getBlockState(pos).getBlock();

                        if (block == Blocks.WOOL) {
                            world.setBlockState(pos, Blocks.WOOL.getDefaultState().withProperty(BlockCarpet.COLOR,
                                    getTeam().colorDye()));
                        } else if (block == Blocks.STAINED_GLASS) {
                            world.setBlockState(pos, Blocks.STAINED_GLASS.getDefaultState().withProperty(BlockStainedGlass.COLOR,
                                    getTeam().colorDye()));
                        }
                    }
                }
            }
        }
    }

    public void reloadLivingEntities() {
        World world = getRegion().getWorld();

        for (EntityTrainer trainer : world.getEntitiesWithinAABB(EntityTrainer.class, getRegion().getBounds())) {
            world.removeEntity(trainer);
        }

        trainerEntities.clear();

        int i = 0;
        for (UUID trainer : trainers.keySet()) {
            if (seats.size() > i) {
                EntityTrainer entityTrainer = new EntityTrainer(world, (BlockPos) seats.keySet().toArray()[i], trainer, trainers.get(trainer));
                world.spawnEntityInWorld(entityTrainer);
                entityTrainer.rotationYaw = (float) seats.values().toArray()[i];
                trainerEntities.add(entityTrainer);
                i++;
            }
        }
    }

    public void battle(EntityPlayerMP player) {
        if (getTeleportPos() == null) {
            player.addChatComponentMessage(new TextComponentString("TeleportPos not set! Tell server admin."));
            return;
        }

        if (!getQue().isEmpty()) {
            if (!getQue().contains(player.getUniqueID())) {
                getQue().add(player.getUniqueID());
                player.addChatComponentMessage(new TextComponentString("You are " + (getQue().size() - 1) + " in que."));
                return;
            }
        } else {
            getQue().add(player.getUniqueID());
        }

        player.setPosition(getTeleportPos().getX(), getTeleportPos().getY(), getTeleportPos().getZ());

        List<TrainerParticipant> trainers = Lists.newArrayList();
        List<PlayerParticipant> players = Lists.newArrayList();

        try {
            /**
             * Setup player pokemons
             */
            players.add(new PlayerParticipant(player, PixelmonStorage.pokeBallManager.getPlayerStorage(player).get().getFirstAblePokemon(player.worldObj)));

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
        gym.team = EnumTeam.valueOf(ByteBufUtils.readUTF8String(buf));
        gym.prestige = buf.readInt();

        int trainersSize = buf.readInt();

        for (int i = 0; i < trainersSize; i++) {
            String[] data = ByteBufUtils.readUTF8String(buf).split(";");
            gym.trainers.put(UUID.fromString(data[0]),
                    PixelmonAPI.pixelmonFromString(data[1], iPixelmon.proxy.getDefaultWorld()));
        }

        int seatsSize = buf.readInt();
        for (int i = 0; i < seatsSize; i++) {
            String[] data = ByteBufUtils.readUTF8String(buf).split(";");
            gym.seats.put(new BlockPos(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2])), 0f);
        }

        int entitySize = buf.readInt();
        for(int i = 0; i < entitySize; i++) {
            gym.getTrainerEntities().add((EntityTrainer) iPixelmon.proxy.getDefaultWorld().getEntityByID(buf.readInt()));
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

        for (UUID id : trainers.keySet()) {
            ByteBufUtils.writeUTF8String(buf, id.toString() + ";" + PixelmonAPI.pixelmonToString(trainers.get(id)));
        }

        buf.writeInt(getSeats().size());
        for (BlockPos pos : getSeats().keySet()) {
            ByteBufUtils.writeUTF8String(buf, pos.getX() + ";" + pos.getY() + ";" + pos.getZ());
        }

        buf.writeInt(getTrainerEntities().size());

        for(EntityTrainer trainer : getTrainerEntities()) {
            buf.writeInt(trainer.getEntityId());
        }
    }

    @Override
    public int compareTo(Gym o) {
        return id.equals(o.id) || regionID.equals(o.regionID) ? 0 : -999;
    }
}
