package rtk.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

public class TileHole extends TileEntity implements ITickable {

    private int timeLeft = 20;
    private IBlockState prevState;

    @Override
    public void update() {
        if(worldObj.isRemote)
            return;
        setTimeLeft(getTimeLeft() - 1);
        if(getTimeLeft() <= 0)
            replace(false);
    }

    public void replace(boolean force){
        if(force || worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos, pos.add(1, 1, 1))).isEmpty())
            worldObj.setBlockState(pos, getPrevState() == null ? Blocks.STONE.getDefaultState() : getPrevState());
        else
            setTimeLeft(0);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("stateID", Block.getStateId(getPrevState()));
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        setPrevState(Block.getStateById(compound.getInteger("stateID")));
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
        markDirty();
    }

    public IBlockState getPrevState() {
        return prevState;
    }

    public void setPrevState(IBlockState prevState) {
        this.prevState = prevState;
        markDirty();
    }
}
