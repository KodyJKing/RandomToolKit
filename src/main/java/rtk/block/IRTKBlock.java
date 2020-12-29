package rtk.block;

import net.minecraft.item.Item;

public interface IRTKBlock {

    default Item.Properties itemProperties() {
        return null;
    }

}
