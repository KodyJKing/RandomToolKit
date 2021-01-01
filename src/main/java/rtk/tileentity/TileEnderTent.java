package rtk.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEnderTent extends TileEntity {

    private boolean deployed;
    private boolean neverDeployed;
    private ListNBT blockList;
    private ListNBT entityList;
    private BlockPos lastPosition;
    public boolean dontGrab;

    public TileEnderTent() {
        super(ModTileTypes.enderTent);
        deployed = false;
        neverDeployed = true;
        blockList = new ListNBT();
        entityList = new ListNBT();
        lastPosition = new BlockPos(0, 0, 0);
        dontGrab = false;
    }

    @Override
    public void read(BlockState bs, CompoundNBT compound) {
        readTent(compound);
        super.read(bs, compound);
    }

    public void readTent(CompoundNBT compound) {
        setDeployed(compound.getBoolean("deployed"));
        setNeverDeployed(compound.getBoolean("firstDeploy"));
        setBlockList(compound.getList("blockList", 10));
        setEntityList(compound.getList("entityList", 10));
        setLastPosition(BlockPos.fromLong(compound.getLong("lastPosition")));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        writeTent(compound);
        return super.write(compound);
    }

    public CompoundNBT writeTent(CompoundNBT compound) {
        compound.putBoolean("deployed", isDeployed());
        compound.putBoolean("firstDeploy", neverDeployed());
        compound.put("blockList", blockList);
        compound.put("entityList", entityList);
        compound.putLong("lastPosition", lastPosition.toLong());
        return compound;
    }

    public boolean isDeployed() {
        return deployed;
    }

    public void setDeployed(boolean deployed) {
        this.deployed = deployed;
        if (deployed)
            setBlockList(new ListNBT());
        markDirty();
    }

    public boolean neverDeployed() {
        return neverDeployed;
    }

    public void setNeverDeployed(boolean firstDeploy) {
        neverDeployed = firstDeploy;
        markDirty();
    }

    public ListNBT getBlockList() {
        return blockList;
    }

    public void setBlockList(ListNBT blocksList) {
        this.blockList = blocksList;
        markDirty();
    }

    public ListNBT getEntityList() {
        return entityList;
    }

    public void setEntityList(ListNBT entityList) {
        this.entityList = entityList;
        markDirty();
    }


    public BlockPos getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(BlockPos lastPosition) {
        this.lastPosition = lastPosition;
        markDirty();
    }



}
