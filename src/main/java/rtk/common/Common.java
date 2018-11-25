package rtk.common;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.List;

public class Common {

    public static String localize(String key, Object... parameters) {
        return I18n.format(key, parameters);
    }

    public static void formatToolTip(String key, List<String> target) {
        if (!I18n.hasKey(key))
            return;
        String[] lines = localize(key).split(";");
        for (String line : lines)
            target.add(line);
    }

    public static void message(EntityPlayer player, String msg) {
        player.sendMessage( new TextComponentString(msg) );
    }

    public static int findExactStack(IInventory inventory, ItemStack stack) {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (inventory.getStackInSlot(i) == stack)
                return i;
        }
        return -1;
    }

    // BaublesApi.isBaubleEquipped is too new to rely on.
    // It is not present in the latest version of the
    // DW20 pack which is what I usually play.
    public static int isBaubleEquipped(EntityPlayer player, Item bauble) {
        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        for (int a=0;a<handler.getSlots();a++)
            if (!handler.getStackInSlot(a).isEmpty() && handler.getStackInSlot(a).getItem()==bauble)
                return a;
        return -1;
    }

    public static Vec3d getTrueCenter(Entity entity) {
        AxisAlignedBB box = entity.getEntityBoundingBox();
        return new Vec3d(box.minX + 0.5 * (box.maxX - box.minX), box.minY + 0.5 * (box.maxY - box.minY), box.minZ + 0.5 * (box.maxZ - box.minZ));
    }

    public static ItemStack getRefill(EntityPlayer player, ItemStack stack, int amount) {
        IInventory inv = player.inventory;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack other = inv.getStackInSlot(i);
            if (other != null && other.isItemEqual(stack)) {
                player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1, 2);
                return inv.decrStackSize(i, amount);
            }
        }
        return null;
    }

    static Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
    public static String prettifyJSON(String json) {
        Object gsonObj = prettyGson.fromJson(json, Object.class);
        return prettyGson.toJson(gsonObj);
    }

}
