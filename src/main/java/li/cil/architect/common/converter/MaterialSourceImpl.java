package li.cil.architect.common.converter;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import li.cil.architect.api.converter.MaterialSource;
import li.cil.architect.util.ItemStackUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public final class MaterialSourceImpl implements MaterialSource {
    private final boolean isCreative;
    private final IItemHandler itemHandler;
    private final IFluidHandler fluidHandler;

    public MaterialSourceImpl(final boolean isCreative, final IItemHandler itemHandler, final IFluidHandler fluidHandler) {
        this.isCreative = isCreative;
        this.itemHandler = itemHandler;
        this.fluidHandler = fluidHandler;
    }

    @Override
    public boolean isCreative() {
        return isCreative;
    }

    @Override
    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    @Nullable
    @Override
    public ItemStack extractItem(final ItemStack wantStack, final boolean simulate) {
        if (ItemStackUtils.isEmpty(wantStack)) {
            throw new IllegalArgumentException("Cannot extract an empty stack.");
        }

        if (isCreative) {
            return wantStack.copy();
        }

        final TIntList slots = new TIntArrayList();
        final ItemStack remaining = wantStack.copy();
        for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
            final ItemStack haveStack = itemHandler.getStackInSlot(slot);
            if (remaining.isItemEqual(haveStack)) {
                final ItemStack extractedStack = itemHandler.extractItem(slot, remaining.stackSize, true);
                if (!ItemStackUtils.isEmpty(extractedStack)) {
                    assert remaining.isItemEqual(extractedStack);
                    slots.add(slot);
                    remaining.stackSize -= extractedStack.stackSize;
                    if (remaining.stackSize < 1) {
                        break;
                    }
                }
            }
        }

        if (!simulate) {
            remaining.stackSize = wantStack.stackSize;
            for (int i = 0; i < slots.size(); i++) {
                assert remaining.stackSize > 0;
                final int slot = slots.get(i);
                final ItemStack haveStack = itemHandler.getStackInSlot(slot);
                assert remaining.isItemEqual(haveStack);
                final ItemStack extractedStack = itemHandler.extractItem(slot, remaining.stackSize, false);
                assert !ItemStackUtils.isEmpty(extractedStack);
                assert remaining.isItemEqual(extractedStack);
                remaining.stackSize -= extractedStack.stackSize;
            }
        }

        final ItemStack result = wantStack.copy();
        result.stackSize -= remaining.stackSize;
        return result.stackSize > 0 ? result : null;
    }

    @Override
    public IFluidHandler getFluidHandler() {
        return fluidHandler;
    }

    @Override
    @Nullable
    public FluidStack extractFluid(final FluidStack wantStack, final boolean simulate) {
        if (wantStack.amount <= 0) {
            throw new IllegalArgumentException("Cannot extract an empty stack.");
        }

        if (isCreative) {
            return wantStack.copy();
        }

        final FluidStack drained = fluidHandler.drain(wantStack, false);
        if (drained == null || !drained.containsFluid(wantStack)) {
            return null;
        }

        if (simulate) {
            return drained;
        } else {
            return fluidHandler.drain(wantStack, true);
        }
    }
}
