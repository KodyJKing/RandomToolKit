package rtk;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

import java.util.HashSet;

/**
 * I don't use this anymore but I keep it around as an example.
 */
public class WorldSavedDataPlayers extends WorldSavedData {
    public static final String DATA_NAME = RTK.modId + "_Players";
    NBTTagList players;
    HashSet<String> playerSet;

    public WorldSavedDataPlayers() {
        this(DATA_NAME);
    }

    public WorldSavedDataPlayers(String name){
        super(name);
        players = new NBTTagList();
        playerSet = new HashSet<String>();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        players = nbt.getTagList("players", 8);
        for(int i = 0; i < players.tagCount(); i++)
            playerSet.add(players.getStringTagAt(i));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("players", players);
        return nbt;
    }

    public void addPlayer(EntityPlayer player){
        players.appendTag(new NBTTagString(player.getName()));
        playerSet.add(player.getName());
        markDirty();
    }

    public boolean hasPlayer(EntityPlayer player){
        return playerSet.contains(player.getName());
    }

    public static WorldSavedDataPlayers get(World world){
        MapStorage storage = world.getMapStorage();
        WorldSavedDataPlayers instance = (WorldSavedDataPlayers) storage.getOrLoadData(WorldSavedDataPlayers.class, DATA_NAME);

        if(instance == null){
            instance = new WorldSavedDataPlayers();
            storage.setData(DATA_NAME, instance);
        }
        return instance;
    }
}
