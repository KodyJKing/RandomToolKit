package rtk.proxy;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import rtk.ModItems;
import rtk.RTK;
import rtk.entity.EntityEyeOfNether;
import rtk.entity.EntityRtkArrow;
import rtk.entity.EntityRtkTNT;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerItemRenderer(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(RTK.modId + ":" + name, "inventory"));
    }

    @Override
    public void ignoreProperty(Block block, IProperty property) {
        ModelLoader.setCustomStateMapper(block, (new StateMap.Builder()).ignore(property).build());
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

        RenderingRegistry.registerEntityRenderingHandler(EntityEyeOfNether.class, new IRenderFactory<EntityEyeOfNether>() {
            @Override
            public Render<? super EntityEyeOfNether> createRenderFor(RenderManager manager) {
                return new RenderSnowball<EntityEyeOfNether>(manager, ModItems.eyeOfNether, Minecraft.getMinecraft().getRenderItem());
            }
        });

    }
}
