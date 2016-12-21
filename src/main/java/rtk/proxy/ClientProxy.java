package rtk.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import rtk.RTK;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerItemRenderer(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(RTK.modId + ":" + name, "inventory"));
    }

    @Override
    public void setCustomStateMap(Block block, Object stateMap){
        ModelLoader.setCustomStateMapper(block, (StateMap)stateMap);
    }
}
