package rtk.proxy;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import rtk.ModItems;
import rtk.RTK;
import rtk.entity.EntityEyeOfNether;
import rtk.entity.EntitySkeletonPriest;
import rtk.render.RenderSkeletonPriest;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerItemRenderer(Item item, int meta, String name) {
//        System.out.println("Registering rendering for ITEM: " + item.getUnlocalizedName() + " with NAME: " + name);
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(RTK.modId + ":" + name, "inventory"));
    }

    @Override
    public void ignoreProperty(Block block, IProperty property) {
        ModelLoader.setCustomStateMapper(block, (new StateMap.Builder()).ignore(property).build());
    }

    @Override
    public void preInit() {
        RenderingRegistry.registerEntityRenderingHandler(
            EntityEyeOfNether.class,
            (RenderManager manager) ->
                    new RenderSnowball<>(manager, ModItems.eyeOfNether, Minecraft.getMinecraft().getRenderItem())
        );

        RenderingRegistry.registerEntityRenderingHandler(
            EntitySkeletonPriest.class,
            (RenderManager manager) ->
                    new RenderSkeletonPriest(manager)
        );
    }
}
