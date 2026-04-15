package com.eightsidedsquare.euphorics.core;

import com.eightsidedsquare.euphorics.common.command.EuphoricsCommand;
import com.mojang.brigadier.CommandDispatcher;
import java.util.function.BiConsumer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

public class EuphoricsCommands {
    public static void init() {
        registerCommand(EuphoricsCommand::register, "euphorics");
    }

    private static void registerCommand(BiConsumer<CommandDispatcher<ServerCommandSource>, String> consumer, String id) {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            consumer.accept(dispatcher, id);
        });
    }
}
