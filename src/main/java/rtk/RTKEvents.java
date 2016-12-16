package rtk;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rtk.item.ItemToolbelt;

public class RTKEvents {
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.LeftClickBlock event){
        EntityPlayer player = event.getEntityPlayer();
        for(int i = 0; i < player.inventory.getSizeInventory(); i++){
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(stack != null && stack.getItem() instanceof ItemToolbelt){
                ItemToolbelt.selectBestTool(player, i);
                return;
            }
        }
    }
}
