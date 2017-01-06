package rtk.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public class TileUltraDispenser extends TileEntity {

    public NBTTagCompound data = new NBTTagCompound();

    public TileUltraDispenser() {
        super();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("data", data);
        System.out.println(data.toString());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        data = compound.getCompoundTag("data");
        System.out.println(data.toString());
    }

//    @Override
//    public NBTTagCompound getUpdateTag() {
//        return writeToNBT(new NBTTagCompound());
//    }
//
//    @Nullable
//    @Override
//    public SPacketUpdateTileEntity getUpdatePacket() {
//        return new SPacketUpdateTileEntity(getPos(), 1, data);
//    }
//
//    @Override
//    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
//        data = pkt.getNbtCompound();
//    }
}
