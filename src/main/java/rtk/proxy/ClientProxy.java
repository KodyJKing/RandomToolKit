package rtk.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderTNTPrimed;
import net.minecraft.client.renderer.entity.RenderTippedArrow;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import rtk.RTK;
import rtk.entity.EntityRtkArrow;
import rtk.entity.EntityRtkTNT;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerItemRenderer(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(RTK.modId + ":" + name, "inventory"));
    }

    @Override
    public void setCustomStateMap(Block block, Object stateMap){
        ModelLoader.setCustomStateMapper(block, (StateMap)stateMap);
    }

    @Override
    public void registerEntityRendering() {
        RenderingRegistry.registerEntityRenderingHandler(EntityRtkTNT.class, new IRenderFactory<EntityRtkTNT>() {
            @Override
            public Render<? super EntityRtkTNT> createRenderFor(RenderManager manager) {
                return new RenderTNTPrimed(manager);
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(EntityRtkArrow.class, new IRenderFactory<EntityRtkArrow>() {
            @Override
            public Render<? super EntityRtkArrow> createRenderFor(RenderManager manager) {
                return new RenderTippedArrow(manager);
            }
        });
    }
}
