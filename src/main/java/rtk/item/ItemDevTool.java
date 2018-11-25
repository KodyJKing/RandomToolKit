package rtk.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import rtk.common.CWorld;

public class ItemDevTool extends ItemBase {
    public ItemDevTool(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
//        return super.onItemRightClick(world, player, hand);

        int slot = player.inventory.currentItem;
        if (slot < 0 && slot > 8)
            return super.onItemRightClick(world, player, hand);

        ItemStack stack = player.getHeldItem(hand);
        ItemStack fakeStack = player.inventory.getStackInSlot(slot + 1);
        player.inventory.setInventorySlotContents(slot, fakeStack);
        player.inventory.setInventorySlotContents(slot + 1, ItemStack.EMPTY);

        RayTraceResult tr = CWorld.getMouseover(player, 100);
        boolean hit = tr != null && tr.typeOfHit != RayTraceResult.Type.MISS;

        EnumActionResult result = EnumActionResult.PASS;

        if (hit) {

            if (tr.entityHit != null && tr.entityHit instanceof EntityLivingBase) {
                result = player.interactOn(tr.entityHit, hand);
            } else {

                // TODO: Use onUseItemFirst

                BlockPos pos = tr.getBlockPos();
                IBlockState bs = world.getBlockState(pos);
                Block block = bs.getBlock();
                result = fakeStack.onItemUse(player, world, pos, hand, tr.sideHit, (float) tr.hitVec.x, (float) tr.hitVec.y, (float) tr.hitVec.z);

                if (result != EnumActionResult.SUCCESS) {
                    boolean activated = block.onBlockActivated(world, pos, bs, player, hand, tr.sideHit, (float) tr.hitVec.x, (float) tr.hitVec.y, (float) tr.hitVec.z);
                    if (activated)
                        result = EnumActionResult.SUCCESS;
                }
            }
        }

        if (fakeStack != null && result != EnumActionResult.SUCCESS) {
            System.out.println("!!!");

            Vec3d oldPos = player.getPositionVector();

            if (hit) {
                Vec3d newPos = tr.hitVec.subtract(player.getLookVec());
                player.posX = newPos.x;
                player.posY = newPos.y - player.eyeHeight;
                player.posZ = newPos.z;
            }

            ActionResult<ItemStack> ar = fakeStack.useItemRightClick(world, player, hand);
            result = ar.getType();
            fakeStack = ar.getResult();

            if (hit) {
                player.posX = oldPos.x;
                player.posY = oldPos.y;
                player.posZ = oldPos.z;
            }

        }

        player.inventory.setInventorySlotContents(slot + 1, fakeStack);
        player.inventory.setInventorySlotContents(slot, stack);

        return new ActionResult<>(result, stack);
    }

}
