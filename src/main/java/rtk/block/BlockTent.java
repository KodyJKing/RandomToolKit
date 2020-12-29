package rtk.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class BlockTent extends Block implements IRTKBlock {

    public BlockTent() {
        super(
                Properties.create(Material.WOOL)
                        .sound(SoundType.CLOTH)
                        .hardnessAndResistance(.5F, .5F)
        );
    }

    @Override
    public Item.Properties itemProperties() {
        return new Item.Properties().group(ItemGroup.TRANSPORTATION);
    }
}
