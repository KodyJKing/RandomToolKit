package rtk;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
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

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        if(event.player.worldObj.isRemote)
            return;

        WorldSavedDataPlayers players = WorldSavedDataPlayers.get(event.player.worldObj);

        if(players.hasPlayer(event.player))
            return;
        players.addPlayer(event.player);

        if(!event.player.capabilities.isCreativeMode)
            event.player.inventory.addItemStackToInventory(new ItemStack(ModBlocks.emergencyTent));
    }
}
