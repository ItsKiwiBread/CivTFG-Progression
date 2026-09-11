package net.itskiwibread.civtfg_progression.progression;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mod.EventBusSubscriber
public class ProgressionItemManager
        extends SimplePreparableReloadListener<Map<ResourceLocation, ItemRule>> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String NAMESPACE = "civtfg_progression";

    private static final String FILE_NAME =
            "progression_items.json";

    private static final Map<ResourceLocation, ItemRule> RULES =
            new HashMap<>();


    // ============================================================
    // REGISTER RELOAD LISTENER
    // ============================================================

    @SubscribeEvent
    public static void registerReloadListener(
            AddReloadListenerEvent event
    ) {

        event.addListener(
                new ProgressionItemManager()
        );
    }


    // ============================================================
    // PREPARE
    // ============================================================

    @Override
    protected Map<ResourceLocation, ItemRule> prepare(
            ResourceManager resourceManager,
            ProfilerFiller profiler
    ) {

        Map<ResourceLocation, ItemRule> loadedRules =
                new HashMap<>();

        ResourceLocation location =
                ResourceLocation.tryBuild(
                        NAMESPACE,
                        FILE_NAME
                );

        Optional<Resource> optionalResource =
                resourceManager.getResource(location);

        if (optionalResource.isEmpty()) {

            LOGGER.warn(
                    "Could not find progression item file: {}:{}",
                    NAMESPACE,
                    FILE_NAME
            );

            return loadedRules;
        }

        Resource resource =
                optionalResource.get();

        try (Reader reader = resource.openAsReader()) {

            JsonObject root =
                    JsonParser.parseReader(reader)
                            .getAsJsonObject();

            JsonObject items =
                    root.getAsJsonObject("items");

            if (items == null) {

                LOGGER.error(
                        "Progression item file is missing 'items'."
                );

                return loadedRules;
            }


            for (Map.Entry<String, JsonElement> entry :
                    items.entrySet()) {

                try {

                    ResourceLocation itemId =
                            ResourceLocation.tryParse(
                                    entry.getKey()
                            );

                    if (itemId == null) {
                        LOGGER.warn(
                                "Invalid resource location: {}",
                                entry.getKey()
                        );

                        continue;
                    }

                    JsonObject ruleJson =
                            entry.getValue()
                                    .getAsJsonObject();


                    // ------------------------------------------------
                    // REQUIRED GOAL
                    // ------------------------------------------------

                    if (!ruleJson.has("goal")) {

                        LOGGER.warn(
                                "Item {} has no 'goal'. Skipping.",
                                itemId
                        );

                        continue;
                    }

                    int goal =
                            ruleJson
                                    .get("goal")
                                    .getAsInt();


                    if (goal < 1) {

                        LOGGER.warn(
                                "Item {} has invalid goal {}. Skipping.",
                                itemId,
                                goal
                        );

                        continue;
                    }


                    // ------------------------------------------------
                    // BLOCK USE
                    // ------------------------------------------------

                    boolean blockUse =
                            ruleJson.has("block_use")
                                    && ruleJson
                                    .get("block_use")
                                    .getAsBoolean();


                    // ------------------------------------------------
                    // DROP ON BLOCKED USE
                    // ------------------------------------------------

                    boolean dropOnBlockedUse =
                            ruleJson.has("drop_on_blocked_use")
                                    && ruleJson
                                    .get("drop_on_blocked_use")
                                    .getAsBoolean();


                    // ------------------------------------------------
                    // VERIFY ITEM EXISTS
                    // ------------------------------------------------

                    if (!ForgeRegistries.ITEMS
                            .containsKey(itemId)) {

                        LOGGER.warn(
                                "Progression item {} does not exist. Skipping.",
                                itemId
                        );

                        continue;
                    }


                    // ------------------------------------------------
                    // CREATE RULE
                    // ------------------------------------------------

                    loadedRules.put(
                            itemId,
                            new ItemRule(
                                    goal,
                                    blockUse,
                                    dropOnBlockedUse
                            )
                    );

                } catch (Exception exception) {

                    LOGGER.error(
                            "Failed to load progression entry '{}'.",
                            entry.getKey(),
                            exception
                    );
                }
            }

        } catch (Exception exception) {

            LOGGER.error(
                    "Failed to load progression item data.",
                    exception
            );
        }

        LOGGER.info(
                "Loaded {} progression item rules.",
                loadedRules.size()
        );

        return loadedRules;
    }


    // ============================================================
    // APPLY
    // ============================================================

    @Override
    protected void apply(
            Map<ResourceLocation, ItemRule> prepared,
            ResourceManager resourceManager,
            ProfilerFiller profiler
    ) {

        RULES.clear();

        RULES.putAll(prepared);

        LOGGER.info(
                "Applied {} progression item rules.",
                RULES.size()
        );
    }


    // ============================================================
    // GET RULE
    // ============================================================

    @Nullable
    public static ItemRule getRule(
            ItemStack stack
    ) {

        ResourceLocation itemId =
                ForgeRegistries.ITEMS
                        .getKey(stack.getItem());

        if (itemId == null) {
            return null;
        }

        return RULES.get(itemId);
    }


    // ============================================================
    // REQUIRED GOAL
    // ============================================================

    public static int requiredGoal(
            ItemStack stack
    ) {

        ItemRule rule =
                getRule(stack);

        if (rule == null) {
            return -1;
        }

        return rule.goal();
    }


    // ============================================================
    // BLOCK USE
    // ============================================================

    public static boolean shouldBlockUse(
            ItemStack stack
    ) {

        ItemRule rule =
                getRule(stack);

        return rule != null
                && rule.blockUse();
    }


    // ============================================================
    // DROP ON BLOCKED USE
    // ============================================================

    public static boolean shouldDropOnBlockedUse(
            ItemStack stack
    ) {

        ItemRule rule =
                getRule(stack);

        return rule != null
                && rule.dropOnBlockedUse();
    }
}