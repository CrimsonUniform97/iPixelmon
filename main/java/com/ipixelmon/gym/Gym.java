package com.ipixelmon.gym;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControl;
import com.ipixelmon.landcontrol.Region;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.mysql.UpdateForm;
import com.ipixelmon.team.EnumTeam;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumBattleType;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumPokeballs;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import net.minecraft.block.BlockCarpet;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
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
//Objects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).toString();

public class Gym {

    private static final int[] levels = {0, 2000, 4000, 8000, 12000, 16000, 20000, 30000, 40000, 50000};

    private UUID region;
    private List<EntityGymLeader> gymLeaders;
    private List<BlockPos> seats;
    private NBTTagCompound tagData, tagSeats, tagGymLeaders;

    protected Gym(UUID region) throws Exception {
        this.region = region;

        ResultSet result = iPixelmon.mysql.selectAllFrom(GymMod.class, new SelectionForm("Gyms").add("region", region.toString()));

        if (result.next()) {
            tagData = JsonToNBT.getTagFromJson(result.getString("data"));
            tagSeats = JsonToNBT.getTagFromJson(result.getString("seats"));
            tagGymLeaders = JsonToNBT.getTagFromJson(result.getString("gymLeaders"));
        }

    }

    protected Gym(Region region) throws Exception {
        this(region.id());
    }

    public long getPower() {
        return tagData.getLong("power");
    }

    public EnumTeam getTeam() {
        return EnumTeam.valueOf(tagData.getString("team"));
    }

    public Region getRegion() throws Exception {
        return LandControl.getRegion(region);
    }

    public int getAvailableSlots() throws Exception {
        System.out.println(getLevel() + "," + getGymLeaders().size());
        return getLevel() - getGymLeaders().size();
    }

    public int getLevel() {
        int count = 1;
        for (int level : levels) {
            if (getPower() <= level) {
                return count;
            }
            count++;
        }

        return 0;
    }

    public List<BlockPos> getSeats() {
        if (seats == null) {
            seats = new ArrayList<>();

            int[] array;
            for (String key : tagSeats.getKeySet()) {
                array = tagSeats.getIntArray(key);
                seats.add(new BlockPos(array[0], array[1], array[2]));
            }
        }

        return seats;
    }

    public List<EntityGymLeader> getGymLeaders() throws Exception {
        if (gymLeaders == null) {
            gymLeaders = new ArrayList<>();

            int seatIndex = 0;
            NBTTagCompound tagCompound;
            EntityPixelmon pixelmon;
            for (String key : tagGymLeaders.getKeySet()) {
                tagCompound = tagGymLeaders.getCompoundTag(key);
                pixelmon = (EntityPixelmon) PixelmonEntityList.createEntityByName(tagCompound.getString("name"), getRegion().getWorldServer());
                pixelmon.setHealth(pixelmon.getMaxHealth());
                pixelmon.setIsShiny(tagCompound.getBoolean("shiny"));
                pixelmon.setForm(tagCompound.getInteger("form"));
                pixelmon.getLvl().setLevel(tagCompound.getInteger("lvl"));
                pixelmon.setGrowth(EnumGrowth.getGrowthFromIndex(tagCompound.getInteger("growth")));
                pixelmon.caughtBall = EnumPokeballs.PokeBall;
                pixelmon.friendship.initFromCapture();

                gymLeaders.add(new EntityGymLeader(getRegion().getWorldServer(),
                        seatIndex < getSeats().size() ? getSeats().get(seatIndex) : new BlockPos(0, 0, 0),
                        pixelmon, UUID.fromString(key)));
                seatIndex++;
            }
        }

        return gymLeaders;
    }

    @SideOnly(Side.SERVER)
    public void setTeam(EnumTeam team) {
        tagData.setString("team", team.name());

        try {
            Region region = getRegion();

            BlockPos pos;
            for (int x = region.getMax().getX(); x >= region.getMin().getX(); x--) {
                for (int y = region.getWorldServer().getHeight(); y > -1; y--) {
                    for (int z = region.getMax().getZ(); z >= region.getMin().getZ(); z--) {
                        pos = new BlockPos(x, y, z);
                        if (region.getWorldServer().getBlockState(pos) != null && region.getWorldServer().getBlockState(pos).getBlock() == Blocks.wool)
                            region.getWorldServer().setBlockState(pos, Blocks.wool.getDefaultState().withProperty(BlockCarpet.COLOR, getTeam().colorDye()));
                    }
                }
            }

            iPixelmon.mysql.update(GymMod.class, new UpdateForm("Gyms").set("data", tagData.toString()).where("region", region.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SideOnly(Side.SERVER)
    public void setPower(long power) {
        try {
            if (power < 0) {
                power = 0;
                setTeam(EnumTeam.None);
                getGymLeaders().remove(getGymLeaders().size() - 1);
            }

            tagData.setLong("power", power);

            sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SideOnly(Side.SERVER)
    public void sync() throws Exception {
        tagGymLeaders = new NBTTagCompound();
        tagSeats = new NBTTagCompound();

        NBTTagCompound tagCompound;
        for (EntityGymLeader gymLeader : getGymLeaders()) {
            tagCompound = new NBTTagCompound();
            tagCompound.setString("name", gymLeader.getPixelmon().getName());
            tagCompound.setInteger("lvl", gymLeader.getPixelmon().getLvl().getLevel());
            tagCompound.setBoolean("shiny", gymLeader.getPixelmon().getIsShiny());
            tagCompound.setInteger("form", gymLeader.getPixelmon().getForm());
            tagCompound.setInteger("growth", gymLeader.getPixelmon().getGrowth().index);
            tagGymLeaders.setTag(gymLeader.getPlayerUUID().toString(), tagCompound);
        }

        int count = 0;
        for (BlockPos seat : getSeats())
            tagSeats.setIntArray(String.valueOf(count++), new int[]{seat.getX(), seat.getY(), seat.getZ()});

        iPixelmon.mysql.update(GymMod.class, new UpdateForm("Gyms").set("data", tagData.toString()).set("seats", tagSeats.toString()).set("gymLeaders", tagGymLeaders.toString()).where("region", region.toString()));
    }

    @SideOnly(Side.SERVER)
    public void spawnGymLeaders() {
        try {
            for (EntityLiving e : getRegion().getWorldServer().getEntitiesWithinAABB(EntityGymLeader.class,
                    new AxisAlignedBB(getRegion().getMin().getX(), 0, getRegion().getMin().getZ(),
                            getRegion().getMax().getX(), getRegion().getWorldServer().getHeight(), getRegion().getMax().getZ()))) {
                getRegion().getWorldServer().removeEntity(e);

            }

            gymLeaders = null;

            for (EntityGymLeader gymLeader : getGymLeaders())
                getRegion().getWorldServer().spawnEntityInWorld(gymLeader);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startBattle(EntityPlayerMP player) throws Exception {
        if(getGymLeaders().isEmpty()) throw new Exception("There are no gym leaders occupying this gym.");

        EntityPixelmon e = PixelmonStorage.PokeballManager.getPlayerStorage(player).getFirstAblePokemon(player.worldObj);
        PlayerParticipant playerParticipant = new PlayerParticipant(player, new EntityPixelmon[]{e});

        // TODO: add parties

        List<TrainerParticipant> trainerParticipants = new ArrayList<>();
        for(EntityGymLeader gymLeader : getGymLeaders()) {
            trainerParticipants.add(new TrainerParticipant(gymLeader, player, 1));
        }

        playerParticipant.startedBattle = true;
        BattleParticipant[] team11 = new BattleParticipant[]{playerParticipant};
        new CustomBattleController(team11, trainerParticipants.toArray(new BattleParticipant[trainerParticipants.size()]), EnumBattleType.Single);
    }

}
