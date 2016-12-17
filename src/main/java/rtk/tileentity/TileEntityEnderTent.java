package rtk.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityEnderTent extends TileEntity {
    private boolean deployed;
    private boolean isFirstDeploy;
    private NBTTagList blockList;

    public TileEntityEnderTent(){
        deployed = false;
        isFirstDeploy = true;
        blockList = new NBTTagList();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        readTent(compound);
        super.readFromNBT(compound);
    }

    public void readTent(NBTTagCompound compound){
        setDeployed(compound.getBoolean("deployed"));
        setFirstDeploy(compound.getBoolean("firstDeploy"));
        setBlockList(compound.getTagList("blockList", 10));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        writeTent(compound);
        return super.writeToNBT(compound);
    }

    public NBTTagCompound writeTent(NBTTagCompound compound){
        compound.setBoolean("deployed", isDeployed());
        compound.setBoolean("firstDeploy", isFirstDeploy());
        compound.setTag("blockList", blockList);
        return compound;
    }

    public boolean isDeployed() {
        return deployed;
    }

    public void setDeployed(boolean deployed) {
        this.deployed = deployed;
        markDirty();
    }

    public boolean isFirstDeploy() {
        return isFirstDeploy;
    }

    public void setFirstDeploy(boolean firstDeploy) {
        isFirstDeploy = firstDeploy;
        markDirty();
    }

    public NBTTagList getBlockList() {
        return blockList;
    }

    public void setBlockList(NBTTagList blocksList) {
        this.blockList = blocksList;
        markDirty();
    }
}
