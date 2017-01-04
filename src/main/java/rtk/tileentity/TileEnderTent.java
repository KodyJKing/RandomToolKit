package rtk.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEnderTent extends TileEntity {
    private boolean deployed;
    private boolean neverDeployed;
    private NBTTagList blockList;

    public boolean dontGrab;

    public TileEnderTent(){
        dontGrab = false;
        deployed = false;
        neverDeployed = true;
        blockList = new NBTTagList();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        readTent(compound);
        super.readFromNBT(compound);
    }

    public void readTent(NBTTagCompound compound){
        setDeployed(compound.getBoolean("deployed"));
        setNeverDeployed(compound.getBoolean("firstDeploy"));
        setBlockList(compound.getTagList("blockList", 10));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        writeTent(compound);
        return super.writeToNBT(compound);
    }

    public NBTTagCompound writeTent(NBTTagCompound compound){
        compound.setBoolean("deployed", isDeployed());
        compound.setBoolean("firstDeploy", neverDeployed());
        compound.setTag("blockList", blockList);
        return compound;
    }

    public boolean isDeployed() {
        return deployed;
    }

    public void setDeployed(boolean deployed) {
        this.deployed = deployed;
        if(deployed)
            setBlockList(new NBTTagList());
        markDirty();
    }

    public boolean neverDeployed() {
        return neverDeployed;
    }

    public void setNeverDeployed(boolean firstDeploy) {
        neverDeployed = firstDeploy;
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
