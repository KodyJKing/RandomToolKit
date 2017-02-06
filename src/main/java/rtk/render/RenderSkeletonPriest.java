package rtk.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import rtk.RTK;
import rtk.entity.EntitySkeletonPriest;
import rtk.model.ModelSkeletonPriest;

public class RenderSkeletonPriest extends RenderLiving<EntitySkeletonPriest>{

    private static final ResourceLocation texture = new ResourceLocation(RTK.modId + ":" + "textures/entity/skeletonPriest3.png");

    public RenderSkeletonPriest(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelSkeletonPriest(), 0.4F);
    }

    @Override
    public void doRender(EntitySkeletonPriest entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySkeletonPriest entity) {
        return texture;
    }
}
