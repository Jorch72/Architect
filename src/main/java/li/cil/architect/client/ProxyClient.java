package li.cil.architect.client;

import li.cil.architect.api.API;
import li.cil.architect.client.gui.GuiHandlerClient;
import li.cil.architect.client.input.KeyboardEventHandlerBlueprint;
import li.cil.architect.client.input.MouseEventHandlerBlueprint;
import li.cil.architect.client.input.MouseEventHandlerSketch;
import li.cil.architect.client.renderer.BlueprintRenderer;
import li.cil.architect.client.renderer.ProviderRenderer;
import li.cil.architect.client.renderer.SketchRenderer;
import li.cil.architect.client.renderer.color.ItemColorBlueprint;
import li.cil.architect.common.Architect;
import li.cil.architect.common.ProxyCommon;
import li.cil.architect.common.init.Items;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Takes care of client-side only setup.
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public final class ProxyClient extends ProxyCommon {
    @Override
    public void onInit(final FMLInitializationEvent event) {
        super.onInit(event);

        NetworkRegistry.INSTANCE.registerGuiHandler(Architect.instance, new GuiHandlerClient());

        KeyBindings.init();

        MinecraftForge.EVENT_BUS.register(BlueprintRenderer.INSTANCE);
        MinecraftForge.EVENT_BUS.register(ProviderRenderer.INSTANCE);
        MinecraftForge.EVENT_BUS.register(SketchRenderer.INSTANCE);
        MinecraftForge.EVENT_BUS.register(KeyboardEventHandlerBlueprint.INSTANCE);
        MinecraftForge.EVENT_BUS.register(MouseEventHandlerBlueprint.INSTANCE);
        MinecraftForge.EVENT_BUS.register(MouseEventHandlerSketch.INSTANCE);
    }

    @Override
    public void onPostInit(final FMLPostInitializationEvent event) {
        super.onPostInit(event);

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(ItemColorBlueprint.INSTANCE, Items.blueprint);
    }

    @SubscribeEvent
    public static void handleModelRegistryEvent(final ModelRegistryEvent event) {
        for (final Item item : Items.getAllItems()) {
            final ResourceLocation registryName = item.getRegistryName();
            assert registryName != null;
            final ModelResourceLocation location = new ModelResourceLocation(registryName, "inventory");
            ModelLoader.setCustomModelResourceLocation(item, 0, location);
        }
    }

    @SubscribeEvent
    public static void handleConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
        if (ObjectUtils.notEqual(API.MOD_ID, event.getModID())) {
            return;
        }

        ConfigManager.sync(API.MOD_ID, Config.Type.INSTANCE);
    }
}
