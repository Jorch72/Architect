package li.cil.architect.common.config;

import li.cil.architect.api.API;
import net.minecraftforge.common.config.Config;

/**
 * Settings stored in regular config file.
 */
@Config(modid = API.MOD_ID)
public final class Settings {
    @Config.LangKey(Constants.CONFIG_MAX_BLUEPRINT_SIZE)
    @Config.Comment("The maximum size of a blueprint in any dimension.")
    @Config.RangeInt(min = 1, max = 255)
    public static int maxBlueprintSize = 16;

    @Config.LangKey(Constants.CONFIG_MAX_PROVIDER_RADIUS)
    @Config.Comment("The maximum distance between a player and the position a provider is bound to for the provider to work.")
    @Config.RangeInt(min = 1, max = 1000)
    public static int maxProviderRadius = 64;

    @Config.LangKey(Constants.CONFIG_MAX_CHUNK_OPS_PER_TICK)
    @Config.Comment("The maximum number of blocks to deserialize (place) per tick per chunk.")
    @Config.RangeInt(min = 1, max = 100)
    public static int maxChunkOperationsPerTick = 1;

    @Config.LangKey(Constants.CONFIG_MAX_WORLD_OPS_PER_TICK)
    @Config.Comment("The maximum number of blocks to deserialize (place) per tick per world.")
    @Config.RangeInt(min = 1, max = 1000)
    public static int maxWorldOperationsPerTick = 16;

    @Config.LangKey(Constants.CONFIG_USE_ENERGY)
    @Config.Comment("Whether to use energy instead of exhausting the player (increasing their hunger). Note that this will require an energy storage implementing the Forge power capability to be present in the player's inventory.")
    public static boolean useEnergy = false;

    // --------------------------------------------------------------------- //

    private Settings() {
    }
}
