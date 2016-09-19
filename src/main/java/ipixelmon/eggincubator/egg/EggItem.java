package ipixelmon.eggincubator.egg;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EggItem extends Item
{

    public static final EggItem instance = new EggItem();

    private EggItem()
    {
        setUnlocalizedName("egg");
        setRegistryName("egg");
        setCreativeTab(CreativeTabs.tabBlock);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelResourceLocation normalModel = new ModelResourceLocation(getRegistryName(), "inventory");
        ModelResourceLocation crackedModel = new ModelResourceLocation(getRegistryName() + "_crack1", "inventory");
        ModelResourceLocation cracked2Model = new ModelResourceLocation(getRegistryName() + "_crack2", "inventory");

        ModelBakery.registerItemVariants(this, normalModel, crackedModel, cracked2Model);

        ModelLoader.setCustomMeshDefinition(this, stack -> {
            if (stack.getMetadata() == 0) {
                return normalModel;
            } else if (stack.getMetadata() == 1) {
                return crackedModel;
            } else {
                return cracked2Model;
            }
        });
    }

}
