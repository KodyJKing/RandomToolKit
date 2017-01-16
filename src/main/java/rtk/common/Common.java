package rtk.common;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class Common {

    public static void formatToolTip(String key, List<String> target){
        if(!I18n.hasKey(key))
            return;
        String[] lines = I18n.format(key).split(";");
        for(String line : lines)
            target.add(line);
    }

    public static int findExactStack(IInventory inventory, ItemStack stack){
        for(int i = 0; i < inventory.getSizeInventory(); i++){
            if(inventory.getStackInSlot(i) == stack)
                return i;
        }
        return -1;
    }

    public static Vec3d getTrueCenter(Entity entity){
        AxisAlignedBB box = entity.getEntityBoundingBox();
        return new Vec3d(box.minX + 0.5 * (box.maxX - box.minX), box.minY + 0.5 * (box.maxY - box.minY), box.minZ + 0.5 * (box.maxZ - box.minZ));
    }

    public static ItemStack getRefill(EntityPlayer player, ItemStack stack, int amount){
        IInventory inv = player.inventory;
        for(int i = 0; i < inv.getSizeInventory(); i++){
            ItemStack other = inv.getStackInSlot(i);
            if(other != null && other.isItemEqual(stack)){
                player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1, 2);
                return inv.decrStackSize(i, amount);
            }
        }
        return null;
    }

    public static RayTraceResult traceLook(Entity entity, Float distance){
        Vec3d eyePos = new Vec3d(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ);
        Vec3d look = entity.getLookVec();
        Vec3d endPoint = eyePos.add(look.scale(distance));
        return entity.worldObj.rayTraceBlocks(eyePos, endPoint, false, false, true);
    }

}
