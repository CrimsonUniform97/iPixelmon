package com.ipixelmon.gyms;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControl;
import com.ipixelmon.landcontrol.Region;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.mysql.UpdateForm;
import com.ipixelmon.teams.EnumTeam;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import net.minecraft.block.BlockCarpet;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.util.*;

/**
 * Created by colby on 10/13/2016.
 * <p>
 * Defeating a Pokemon at a rival gym: -500 Prestige
 * Defeating all the Pokemon at a rival gym: -1,500 Prestige on top of the individual bonuses
 * <p>
 * If a gym loses enough prestige to drop down a level, the lowest-ranked Pokemon is dropped; if all of a gym's prestige is depleted, the team is kicked out altogether and the gym reverts to a neutral, unclaimed state.
 */

/**
 * Defeating a Pokemon at a rival gym: -500 Prestige
 * Defeating all the Pokemon at a rival gym: -1,500 Prestige on top of the individual bonuses
 *
 * If a gym loses enough prestige to drop down a level, the lowest-ranked Pokemon is dropped; if all of a gym's prestige is depleted, the team is kicked out altogether and the gym reverts to a neutral, unclaimed state.
 */

/**
 * You can be in as many as 10 gyms at once, but you cannot employ more than one of your own Pokémon at a single gym.
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
    private Map<EntityPlayerMP, BattleControllerBase> battles;
    private List<BlockPos> seats;
    private NBTTagCompound tagData, tagSeats, tagGymLeaders;

    protected Gym(UUID region) throws Exception {
        this.region = region;

        ResultSet result = iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms").add("region", region.toString()));

        if (result.next()) {
            tagData = JsonToNBT.getTagFromJson(result.getString("data"));
            tagSeats = JsonToNBT.getTagFromJson(result.getString("seats"));
            tagGymLeaders = JsonToNBT.getTagFromJson(result.getString("gymLeaders"));
        }

        battles = new HashMap<>();
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

    public int getAvailableSlots() {
        return getLevel() - gymLeaders.size();
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
            for (String key : tagGymLeaders.getKeySet()) {
                gymLeaders.add(new EntityGymLeader(getRegion().getWorldServer(), seatIndex < getSeats().size() ? getSeats().get(seatIndex) : new BlockPos(0, 0, 0),
                        (EntityPixelmon) PixelmonEntityList.createEntityFromNBT(tagGymLeaders.getCompoundTag(key), getRegion().getWorldServer()), UUID.fromString(key)));
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

            iPixelmon.mysql.update(Gyms.class, new UpdateForm("Gyms").set("data", tagData.toString()).where("region", region.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SideOnly(Side.SERVER)
    public void initBattle(EntityPlayerMP player) throws Exception {
        if (getGymLeaders().isEmpty()) throw new Exception("There are no gym leaders occupying this gym.");

        EntityGymLeader gymLeader = getGymLeaders().get(getGymLeaders().size() - 1);

        if (battles.containsKey(player)) {
            for (int i = 0; i < gymLeaders.size(); i++) {
                TrainerParticipant trainerParticipant = (TrainerParticipant) battles.get(player).participants.get(1);
                if (gymLeaders.get(i).equals(trainerParticipant.trainer))
                    if (i - 1 > -1) {
                        gymLeader = gymLeaders.get(i - 1);
                    } else {
                        // TODO: Beat the GYM!
                    }
            }
        }


        EntityPixelmon[] pixelmons = new EntityPixelmon[PixelmonStorage.PokeballManager.getPlayerStorage(player).getAllAblePokemonIDs().size()];

        int i = 0;
        for (int[] id : PixelmonStorage.PokeballManager.getPlayerStorage(player).getAllAblePokemonIDs())
            pixelmons[i++] = PixelmonStorage.PokeballManager.getPlayerStorage(player).getPokemon(id, getRegion().getWorldServer());


        PlayerParticipant playerParticipant = new PlayerParticipant(player, pixelmons);
        TrainerParticipant trainerParticipant = new TrainerParticipant(gymLeader, player, 1);

        playerParticipant.startedBattle = true;
        BattleParticipant[] team1 = new BattleParticipant[]{playerParticipant};
        BattleParticipant[] team2 = new BattleParticipant[]{trainerParticipant};
        battles.put(player, new BattleControllerBase(team1, team2));
    }

    @SideOnly(Side.SERVER)
    public void setPower(long power) {
        tagData.setLong("power", power);
        iPixelmon.mysql.update(Gyms.class, new UpdateForm("Gyms").set("data", tagData.toString()).where("region", region.toString()));
    }

    @SideOnly(Side.SERVER)
    public void sync() throws Exception {
        tagGymLeaders = new NBTTagCompound();
        tagSeats = new NBTTagCompound();

        // TODO: Get correct NBT, not working. getPixelmon().getNBTTagCompound() returns null....
        for (EntityGymLeader gymLeader : getGymLeaders())
            tagGymLeaders.setTag(gymLeader.getPlayerUUID().toString(), gymLeader.getPixelmon().getNBTTagCompound());

        int count = 0;
        for (BlockPos seat : getSeats())
            tagSeats.setIntArray(String.valueOf(count++), new int[]{seat.getX(), seat.getY(), seat.getZ()});

        iPixelmon.mysql.update(Gyms.class, new UpdateForm("Gyms").set("data", tagData.toString()).set("seats", tagSeats.toString()).set("gymLeaders", tagGymLeaders.toString()).where("region", region.toString()));
    }

}
