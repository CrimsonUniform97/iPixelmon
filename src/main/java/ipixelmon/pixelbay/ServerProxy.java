package ipixelmon.pixelbay;

import ipixelmon.CommonProxy;
import ipixelmon.iPixelmon;
import ipixelmon.mysql.CreateForm;
import ipixelmon.mysql.DataType;
import net.minecraftforge.common.MinecraftForge;

public final class ServerProxy extends CommonProxy {
    @Override
    public final void preInit() {

    }

    @Override
    public final void init() {
        final CreateForm pokemonForm = new CreateForm("Pokemon");
        pokemonForm.add("seller", DataType.TEXT);
        pokemonForm.add("name", DataType.TEXT);
        pokemonForm.add("isShiny", DataType.BOOLEAN);
        pokemonForm.add("lvl", DataType.INT);
        pokemonForm.add("xp", DataType.INT);
        pokemonForm.add("price", DataType.LONG);

        iPixelmon.mysql.createTable(Pixelbay.class, pokemonForm);

        final CreateForm itemForm = new CreateForm("Item");
        itemForm.add("seller", DataType.TEXT);
        itemForm.add("item", DataType.TEXT);
        itemForm.add("itemName", DataType.TEXT);
        itemForm.add("price", DataType.LONG);

        iPixelmon.mysql.createTable(Pixelbay.class, itemForm);

        MinecraftForge.EVENT_BUS.register(new ServerBreakListener());
    }
}
