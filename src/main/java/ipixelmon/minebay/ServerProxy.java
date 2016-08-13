package ipixelmon.minebay;

import ipixelmon.CommonProxy;
import ipixelmon.iPixelmon;
import ipixelmon.mysql.CreateForm;
import net.minecraftforge.common.MinecraftForge;

public final class ServerProxy extends CommonProxy {
    @Override
    public final void preInit() {

    }

    @Override
    public final void init() {
        // TODO: Implement search algorithm, and create table, and update PacketSellPokemon to insert into the table
        iPixelmon.mysql.createTable(Minebay.class, new CreateForm(""));
        MinecraftForge.EVENT_BUS.register(new ServerBreakListener());
    }
}
