package ipixelmon.pixelbay;

import ipixelmon.CommonProxy;
import ipixelmon.ItemSerializer;
import ipixelmon.iPixelmon;
import ipixelmon.mysql.CreateForm;
import ipixelmon.mysql.DataType;
import ipixelmon.mysql.InsertForm;
import ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import java.util.Iterator;

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
        pokemonForm.add("price", DataType.INT);

        iPixelmon.mysql.createTable(Pixelbay.class, pokemonForm);

        final CreateForm itemForm = new CreateForm("Item");
        itemForm.add("seller", DataType.TEXT);
        itemForm.add("item", DataType.TEXT);
        itemForm.add("itemName", DataType.TEXT);
        itemForm.add("price", DataType.INT);

        iPixelmon.mysql.createTable(Pixelbay.class, itemForm);

        MinecraftForge.EVENT_BUS.register(new ServerBreakListener());

        Iterator<Item> itemIterator = Item.itemRegistry.iterator();
        for(int i = 0; i < 275; i++) {
            final InsertForm insertForm = new InsertForm("Item");
            ItemStack itemStack = new ItemStack(itemIterator.next());

            insertForm.add("seller", UUIDManager.getUUID("CMcHenry").toString());
            insertForm.add("item", ItemSerializer.itemToString(itemStack));
            insertForm.add("itemName", itemStack.getUnlocalizedName().toLowerCase().replaceAll("item.", "").replaceAll("tile.", ""));
            insertForm.add("price", "" + (int) (Math.random() * 100));

            iPixelmon.mysql.insert(Pixelbay.class, insertForm);
        }
    }
}
