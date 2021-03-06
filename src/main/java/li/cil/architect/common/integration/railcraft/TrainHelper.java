package li.cil.architect.common.integration.railcraft;

import mods.railcraft.api.carts.CartToolsAPI;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class TrainHelper implements ITrainHelper {
    @Nullable
    @Override
    public IItemHandler getTrainItemHandler(final EntityMinecart cart) {
        try {
            return CartToolsAPI.transferHelper.getTrainItemHandler(cart);
        } catch (final Throwable ignored) {
        }
        return null;
    }

    @Nullable
    @Override
    public IFluidHandler getTrainFluidHandler(final EntityMinecart cart) {
        try {
            return CartToolsAPI.transferHelper.getTrainFluidHandler(cart);
        } catch (final Throwable ignored) {
        }
        return null;
    }
}
