package net.itskiwibread.civtfg_progression.progression;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ProgressionEvents {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {

        CommandDispatcher<CommandSourceStack> dispatcher =
                event.getDispatcher();

        dispatcher.register(
                Commands.literal("progression")

                        // /progression status
                        .then(
                                Commands.literal("status")
                                        .executes(context -> {

                                            ServerPlayer player =
                                                    context.getSource()
                                                            .getPlayerOrException();

                                            ProgressionManager manager =
                                                    ProgressionManager.get(
                                                            player.getServer()
                                                    );

                                            int progress =
                                                    manager.getProgress(player);

                                            player.sendSystemMessage(
                                                    Component.literal(
                                                            "Progression: "
                                                                    + progress
                                                                    + "/3"
                                                    )
                                            );

                                            return 1;
                                        })
                        )

                        // /progression unlock <1-3>
                        .then(
                                Commands.literal("unlock")
                                        .then(
                                                net.minecraft.commands.Commands
                                                        .argument(
                                                                "goal",
                                                                IntegerArgumentType.integer(1, 3)
                                                        )
                                                        .executes(context -> {

                                                            ServerPlayer player =
                                                                    context.getSource()
                                                                            .getPlayerOrException();

                                                            int goal =
                                                                    IntegerArgumentType.getInteger(
                                                                            context,
                                                                            "goal"
                                                                    );

                                                            ProgressionManager manager =
                                                                    ProgressionManager.get(
                                                                            player.getServer()
                                                                    );

                                                            manager.unlockGoal(
                                                                    player,
                                                                    goal
                                                            );

                                                            player.sendSystemMessage(
                                                                    Component.literal(
                                                                            "Unlocked goal "
                                                                                    + goal
                                                                                    + " for your team."
                                                                    )
                                                            );

                                                            return 1;
                                                        })
                                        )
                        )

                        // /progression reset
                        .then(
                                Commands.literal("reset")
                                        .executes(context -> {

                                            ServerPlayer player =
                                                    context.getSource()
                                                            .getPlayerOrException();

                                            ProgressionManager manager =
                                                    ProgressionManager.get(
                                                            player.getServer()
                                                    );

                                            manager.reset(player);

                                            player.sendSystemMessage(
                                                    Component.literal(
                                                            "Progression reset."
                                                    )
                                            );

                                            return 1;
                                        })
                        )
        );
    }
}