package rtk;

import baubles.api.BaublesApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderIronGolem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rtk.common.CNBT;
import rtk.item.ItemToolbelt;

public class ModEvents {
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.LeftClickBlock event){
        EntityPlayer player = event.getEntityPlayer();

        // Check their main inventory.
        for(int i = 0; i < player.inventory.getSizeInventory(); i++){
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(stack != null && stack.getItem() instanceof ItemToolbelt){
                ItemToolbelt.selectBestTool(player, i);
                return;
            }
        }

        if (RTK.baublesLoaded)
            if (BaublesApi.isBaubleEquipped(player, ModItems.toolbelt) > -1)
                ItemToolbelt.selectBestTool(player, -1);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        if(event.player.world.isRemote || event.player.capabilities.isCreativeMode)
            return;

        NBTTagCompound playerData = event.player.getEntityData();
        NBTTagCompound persist = CNBT.ensureCompound(playerData, EntityPlayer.PERSISTED_NBT_TAG);

        if(!persist.getBoolean("GivenTent")){
            event.player.inventory.addItemStackToInventory(new ItemStack(ModBlocks.emergencyTent));
            persist.setBoolean("GivenTent", true);
            playerData.setTag(EntityPlayer.PERSISTED_NBT_TAG, persist);
        }
    }
}
