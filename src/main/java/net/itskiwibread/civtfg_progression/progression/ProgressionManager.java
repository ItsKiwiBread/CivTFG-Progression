package net.itskiwibread.civtfg_progression.progression;

import com.mojang.logging.LogUtils;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProgressionManager extends SavedData {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String DATA_NAME = "recreated_progression";

    /*
     * Team UUID -> highest unlocked goal.
     *
     * Example:
     *
     * Team A -> 2
     * Team B -> 1
     * Team C -> 3
     */
    private final Map<UUID, Integer> teamProgress = new HashMap<>();


    // ============================================================
    // Getting the ProgressionManager
    // ============================================================

    public static ProgressionManager get(MinecraftServer server) {

        return server.overworld()
                .getDataStorage()
                .computeIfAbsent(
                        ProgressionManager::load,
                        ProgressionManager::new,
                        DATA_NAME
                );
    }


    // ============================================================
    // Loading / Saving
    // ============================================================

    public static ProgressionManager load(CompoundTag tag) {

        ProgressionManager data = new ProgressionManager();

        CompoundTag teams = tag.getCompound("Teams");

        for (String key : teams.getAllKeys()) {

            try {

                UUID teamId = UUID.fromString(key);
                int progress = teams.getInt(key);

                data.teamProgress.put(teamId, progress);

            } catch (IllegalArgumentException ignored) {

                LOGGER.warn("Invalid team UUID in progression data: {}", key);

            }
        }

        return data;
    }


    @Override
    public CompoundTag save(CompoundTag tag) {

        CompoundTag teams = new CompoundTag();

        for (Map.Entry<UUID, Integer> entry : teamProgress.entrySet()) {

            teams.putInt(
                    entry.getKey().toString(),
                    entry.getValue()
            );
        }

        tag.put("Teams", teams);

        return tag;
    }


    // ============================================================
    // FTB TEAM LOOKUP
    // ============================================================

    /**
     * Gets the FTB Team that the player currently belongs to.
     */
    private static Team getTeam(ServerPlayer player) {

        return FTBTeamsAPI.api()
                .getManager()
                .getTeamForPlayer(player)
                .orElse(null);
    }


    /**
     * Gets the unique UUID used for storing progression.
     */
    private static UUID getTeamId(ServerPlayer player) {

        Team team = getTeam(player);

        if (team == null) {
            return null;
        }

        /*
         * getTeamId() is important here.
         *
         * If the player is in an FTB party/team, this gives us
         * the effective party team ID rather than just their
         * personal player-team ID.
         */
        return team.getTeamId();
    }


    // ============================================================
    // PROGRESSION
    // ============================================================

    /**
     * Returns the highest goal this player's team has completed.
     *
     * 0 = nothing
     * 1 = iron pickaxe
     * 2 = gold pickaxe
     * 3 = diamond pickaxe
     */
    public int getProgress(ServerPlayer player) {

        UUID teamId = getTeamId(player);

        if (teamId == null) {
            return 0;
        }

        return teamProgress.getOrDefault(teamId, 0);
    }


    /**
     * Checks whether the player's team has completed
     * at least the specified goal.
     */
    public boolean hasGoal(ServerPlayer player, int goal) {

        return getProgress(player) >= goal;
    }


    /**
     * Unlocks a goal for the player's entire FTB team.
     */
    public void unlockGoal(ServerPlayer player, int goal) {

        if (goal < 1 || goal > 3) {
            throw new IllegalArgumentException(
                    "Goal must be between 1 and 3."
            );
        }

        UUID teamId = getTeamId(player);

        if (teamId == null) {
            LOGGER.warn(
                    "Could not unlock goal {} for {} because they have no FTB Team.",
                    goal,
                    player.getGameProfile().getName()
            );

            return;
        }

        int currentProgress =
                teamProgress.getOrDefault(teamId, 0);

        /*
         * Never decrease progression.
         */
        if (goal <= currentProgress) {
            return;
        }

        teamProgress.put(teamId, goal);

        setDirty();

        LOGGER.info(
                "Team {} unlocked progression level {}",
                teamId,
                goal
        );
    }


    /**
     * Completely resets this team's progression.
     *
     * This is mainly useful for testing.
     */
    public void reset(ServerPlayer player) {

        UUID teamId = getTeamId(player);

        if (teamId == null) {
            return;
        }

        teamProgress.remove(teamId);

        setDirty();

        LOGGER.info(
                "Reset progression for team {}",
                teamId
        );
    }
}