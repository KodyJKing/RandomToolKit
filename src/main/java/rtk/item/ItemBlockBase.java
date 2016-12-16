package rtk.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import rtk.block.BlockBase;

import java.util.List;

public class ItemBlockBase extends ItemBlock {
    public ItemBlockBase(BlockBase block) {
        super(block);
    }
}
