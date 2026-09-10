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

    /*
     * This is the name used when Minecraft saves the data.
     *
     * The file will be stored in the world's saved-data area.
     */
    private static final String DATA_NAME = "recreated_progression";

    /*
     * FTB Team UUID -> highest unlocked goal
     *
     * Example:
     *
     * Team A -> 2
     * Team B -> 1
     * Team C -> 3
     */
    private final Map<UUID, Integer> teamProgress = new HashMap<>();


    // ============================================================
    // GET THE MANAGER
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
    // LOAD
    // ============================================================

    public static ProgressionManager load(CompoundTag tag) {

        ProgressionManager data = new ProgressionManager();

        CompoundTag teams = tag.getCompound("Teams");

        for (String key : teams.getAllKeys()) {

            try {

                UUID teamId = UUID.fromString(key);

                int progress = teams.getInt(key);

                data.teamProgress.put(teamId, progress);

            } catch (IllegalArgumentException exception) {

                LOGGER.warn(
                        "Invalid FTB Team UUID in progression data: {}",
                        key
                );
            }
        }

        return data;
    }


    // ============================================================
    // SAVE
    // ============================================================

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
    // FTB TEAM
    // ============================================================

    /**
     * Gets the FTB Team associated with the player.
     *
     * FTB Teams returns the player's current effective team,
     * which means this is the party team if the player belongs
     * to an FTB party.
     */
    private static Team getTeam(ServerPlayer player) {

        return FTBTeamsAPI.api()
                .getManager()
                .getTeamForPlayer(player)
                .orElse(null);
    }


    /**
     * Gets the UUID that we use as the progression key.
     *
     * Team#getTeamId() is important here because it gives the
     * effective team ID. Players in the same FTB party therefore
     * use the same progression entry.
     */
    private static UUID getTeamId(ServerPlayer player) {

        Team team = getTeam(player);

        if (team == null) {
            return null;
        }

        return team.getTeamId();
    }


    // ============================================================
    // GET PROGRESS
    // ============================================================

    /**
     * Returns the highest goal completed by the player's team.
     *
     * 0 = no goals
     * 1 = Goal 1
     * 2 = Goal 2
     * 3 = Goal 3
     */
    public int getProgress(ServerPlayer player) {

        UUID teamId = getTeamId(player);

        if (teamId == null) {
            return 0;
        }

        return teamProgress.getOrDefault(teamId, 0);
    }


    // ============================================================
    // CHECK GOAL
    // ============================================================

    /**
     * Returns true if the player's team has unlocked
     * at least the requested goal.
     */
    public boolean hasGoal(ServerPlayer player, int goal) {

        return getProgress(player) >= goal;
    }


    // ============================================================
    // UNLOCK GOAL
    // ============================================================

    /**
     * Unlocks a goal for the player's entire FTB Team.
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
                    "Could not unlock Goal {} for {} because they are not in an FTB Team.",
                    goal,
                    player.getGameProfile().getName()
            );

            return;
        }

        int currentProgress =
                teamProgress.getOrDefault(teamId, 0);

        /*
         * Don't allow progression to go backwards.
         */
        if (goal <= currentProgress) {
            return;
        }

        teamProgress.put(teamId, goal);

        /*
         * Tell Minecraft that this SavedData has changed
         * and needs to be saved.
         */
        setDirty();

        LOGGER.info(
                "FTB Team {} unlocked Goal {}",
                teamId,
                goal
        );
    }


    // ============================================================
    // RESET
    // ============================================================

    /**
     * Resets this team's progression.
     *
     * This is mainly for testing.
     */
    public void reset(ServerPlayer player) {

        UUID teamId = getTeamId(player);

        if (teamId == null) {
            return;
        }

        teamProgress.remove(teamId);

        setDirty();

        LOGGER.info(
                "Reset progression for FTB Team {}",
                teamId
        );
    }
}
