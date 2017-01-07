package rtk.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileHole extends TileEntity implements ITickable {

    public int timeLeft = 20;
    public IBlockState prevState;

    @Override
    public void update() {
        if(worldObj.isRemote)
            return;
        timeLeft--;
        if(timeLeft <= 0)
            replace(false);
    }

    public void replace(boolean force){
        if(force || worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos, pos.add(1, 1, 1))).isEmpty())
            worldObj.setBlockState(pos, prevState == null ? Blocks.STONE.getDefaultState() : prevState);
        else
            timeLeft = 0;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("stateID", Block.getStateId(prevState));
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        prevState = Block.getStateById(compound.getInteger("stateID"));
    }
}
