package rtk.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rtk.ModConfig;
import rtk.common.CNBT;
import rtk.common.CWorld;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class ItemDolly extends ItemBase {
    public ItemDolly(String name) {
        super(name);
        setCreativeTab(CreativeTabs.TOOLS);
        setMaxStackSize(1);
        addPropertyOverride(new ResourceLocation("full"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
                boolean result = stack.getTagCompound() != null && stack.getTagCompound().hasKey("container");
                return result ? 1 : 0;
            }
        });
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        NBTTagCompound nbt = CNBT.ensureCompound(stack);

        if (world.isRemote)
            return EnumActionResult.PASS;

        if (canPickUp(world, pos) && !nbt.hasKey("container")){
            nbt.setTag("container", CNBT.NBTFromBlock(world, pos));
            CWorld.silentSetBlockStateAndUpdate(world, pos, Blocks.AIR.getDefaultState(), 3);
            return EnumActionResult.SUCCESS;
        }

        if (nbt.hasKey("container") && CWorld.shouldReplace(world, pos.offset(side))){
            CNBT.placeBlockFromNBT(world, pos.offset(side), nbt.getCompoundTag("container"), player, hand, side, hitX, hitY, hitZ);
            nbt.removeTag("container");
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    public static boolean canPickUp(World world, BlockPos pos){
        String blockName = world.getBlockState(pos).getBlock().getRegistryName().toString();

        TileEntity te = world.getTileEntity(pos);
        boolean defaultResult = te != null && te instanceof IInventory;

        if (defaultResult && Arrays.asList(ModConfig.dollyBlacklist).contains(blockName))
            return false;

        if (!defaultResult && Arrays.asList(ModConfig.dollyWhitelist).contains(blockName))
            return true;

        return defaultResult;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("container")){
            NBTTagCompound content = stack.getTagCompound().getCompoundTag("container");
            IBlockState bs = Block.getStateById(content.getInteger("stateID"));
            ItemStack fakeStack = new ItemStack(bs.getBlock(), 1, bs.getBlock().getMetaFromState(bs));
            tooltip.add(TextFormatting.DARK_GRAY + "- " + fakeStack.getDisplayName());
        }
    }
}
