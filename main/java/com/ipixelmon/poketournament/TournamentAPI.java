package com.ipixelmon.poketournament;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.landcontrol.regions.Region;
import com.ipixelmon.mysql.DeleteForm;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.mysql.SelectionForm;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class TournamentAPI {

    @SideOnly(Side.CLIENT)
    public static class Client {

    }

    @SideOnly(Side.SERVER)
    public static class Server {

        public static final Set<Arena> ARENAS = new TreeSet<>();

        /* Creates an arena with a name and region */
        public static void createArena(Region region, String name) throws Exception {
            if (name == null || name.isEmpty()) throw new Exception("Invalid name.");

            if (getArena(region) != null) throw new Exception("There is already an arena in this region.");

            if (getArena(name) != null) throw new Exception("There is already an arena with that name.");

            InsertForm insertForm = new InsertForm("Arenas");
            insertForm.add("region", region.getID().toString());
            insertForm.add("name", name);

            iPixelmon.mysql.insert(PokeTournamentMod.class, insertForm);

            ARENAS.add(new Arena(region));
        }

        /* Deletes arena by region */
        public static void deleteArena(Region region) throws Exception {
            deleteArena(getArena(region));
        }

        /* Deletes arena by name */
        public static void deleteArena(String name) throws Exception {
            deleteArena(getArena(name));
        }

        /* Deletes arena by arena */
        public static void deleteArena(Arena arena) throws Exception {
            if(arena == null) throw new Exception("Arena not found.");

            DeleteForm deleteForm = new DeleteForm("Arenas");
            deleteForm.add("region", arena.getRegion().getID().toString());
            deleteForm.add("name", arena.getName());
            iPixelmon.mysql.delete(PokeTournamentMod.class, deleteForm);
            ARENAS.remove(arena);
        }

        /* Get arena from a region/or location */
        public static Arena getArena(Region region) {
            if(region == null) return null;

            for (Arena a : ARENAS)
                if (a.getRegion().getID().equals(region.getID()))
                    return a;

            return null;
        }

        /* Get arena from BattleController */
        public static Arena getArena(BattleControllerBase controller) {
            for(Arena arena : ARENAS) {
                if(arena.getTournament() != null) {
                    if(!arena.getTournament().getMatches().isEmpty()) {
                        for(Match match : arena.getTournament().getMatches()) {
                            if(match.battleController != null) {
                                if(match.battleController.equals(controller)) return arena;
                            }
                        }
                    }
                }
            }

            return null;
        }

        /* Gets arena with given name */
        public static Arena getArena(String name) {
            if (name == null || name.isEmpty()) return null;

            for (Arena a : ARENAS)
                if (a.getName().equalsIgnoreCase(name)) return a;

            return null;
        }

        /* Clear all arenas and setup new ones */
        public static void initArenas() {
            ResultSet result = iPixelmon.mysql.selectAllFrom(PokeTournamentMod.class, new SelectionForm("Arenas"));

            ARENAS.clear();

            try {
                while (result.next())
                    ARENAS.add(new Arena(LandControlAPI.Server.getRegion(UUID.fromString(result.getString("region")))));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}
