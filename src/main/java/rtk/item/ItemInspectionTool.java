package rtk.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtk.common.CNBT;
import rtk.common.Common;

public class ItemInspectionTool extends ItemBase {
    public ItemInspectionTool(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null) {
            String side = world.isRemote ? "Client" : "Server";
            String state = Common.prettifyJSON(te.writeToNBT(new NBTTagCompound()).toString());
            String msg = side + ": " + state;
            Common.message(player, msg);
            System.out.println("\n" + msg);
        }
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

}
