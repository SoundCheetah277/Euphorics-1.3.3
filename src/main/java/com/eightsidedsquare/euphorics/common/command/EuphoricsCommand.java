package com.eightsidedsquare.euphorics.common.command;

import com.eightsidedsquare.euphorics.cca.EuphoriaComponent;
import com.eightsidedsquare.euphorics.cca.EuphoricsEntityComponents;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.text.Text;
import net.minecraft.server.network.ServerPlayerEntity;

public class EuphoricsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, String literal) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal(literal).requires((source) -> {
            return source.hasPermissionLevel(2);
        })).then(CommandManager.argument("targets", EntityArgumentType.players()).then(((LiteralArgumentBuilder)CommandManager.literal("euphoria").then(CommandManager.literal("get").executes((ctx) -> {
            return executeGet(ctx.getSource(), EntityArgumentType.getPlayers(ctx, "targets"), EuphoriaComponent::getEuphoriaTicks, "euphoria");
        }))).then(CommandManager.literal("set").then(CommandManager.argument("ticks", IntegerArgumentType.integer(0)).executes((ctx) -> {
            return executeSet(ctx.getSource(), EntityArgumentType.getPlayers(ctx, "targets"), EuphoriaComponent::setEuphoriaTicks, IntegerArgumentType.getInteger(ctx, "ticks"), "euphoria");
        })))).then(((LiteralArgumentBuilder)CommandManager.literal("last_use").then(CommandManager.literal("get").executes((ctx) -> {
            return executeGet(ctx.getSource(), EntityArgumentType.getPlayers(ctx, "targets"), EuphoriaComponent::getLastUse, "last_use");
        }))).then(CommandManager.literal("set").then(CommandManager.argument("ticks", IntegerArgumentType.integer(0)).executes((ctx) -> {
            return executeSet(ctx.getSource(), EntityArgumentType.getPlayers(ctx, "targets"), EuphoriaComponent::setLastUse, IntegerArgumentType.getInteger(ctx, "ticks"), "last_use");
        }))).then(CommandManager.literal("reset").executes((ctx) -> {
            return executeResetLastUse(ctx.getSource(), EntityArgumentType.getPlayers(ctx, "targets"));
        }))).then(((LiteralArgumentBuilder)CommandManager.literal("astral_plane").then(CommandManager.literal("get").executes((ctx) -> {
            return executeGet(ctx.getSource(), EntityArgumentType.getPlayers(ctx, "targets"), EuphoriaComponent::isInAstralPlane, "astral_plane_ticks");
        }))).then(CommandManager.literal("set").then(CommandManager.argument("is_in_astral_plane", BoolArgumentType.bool()).executes((ctx) -> {
            return executeSet(ctx.getSource(), EntityArgumentType.getPlayers(ctx, "targets"), EuphoriaComponent::setInAstralPlane, BoolArgumentType.getBool(ctx, "is_in_astral_plane"), "astral_plane_ticks");
        })))).then(((LiteralArgumentBuilder)CommandManager.literal("blink_cooldown").then(CommandManager.literal("get").executes((ctx) -> {
            return executeGet(ctx.getSource(), EntityArgumentType.getPlayers(ctx, "targets"), EuphoriaComponent::getBlinkCooldown, "blink_cooldown");
        }))).then(CommandManager.literal("set").then(CommandManager.argument("ticks", IntegerArgumentType.integer(0)).executes((ctx) -> {
            return executeSet(ctx.getSource(), EntityArgumentType.getPlayers(ctx, "targets"), EuphoriaComponent::setBlinkCooldown, IntegerArgumentType.getInteger(ctx, "ticks"), "blink_cooldown");
        })))).then(((LiteralArgumentBuilder)CommandManager.literal("shade_aggro").then(CommandManager.literal("get").executes((ctx) -> {
            return executeGet(ctx.getSource(), EntityArgumentType.getPlayers(ctx, "targets"), EuphoriaComponent::getShadeAggro, "shade_aggro");
        }))).then(CommandManager.literal("set").then(CommandManager.argument("amount", IntegerArgumentType.integer(0)).executes((ctx) -> {
            return executeSet(ctx.getSource(), EntityArgumentType.getPlayers(ctx, "targets"), EuphoriaComponent::setShadeAggro, IntegerArgumentType.getInteger(ctx, "amount"), "shade_aggro");
        })))).then(((LiteralArgumentBuilder)CommandManager.literal("forced_astral_plane_ticks").then(CommandManager.literal("get").executes((ctx) -> {
            return executeGet(ctx.getSource(), EntityArgumentType.getPlayers(ctx, "targets"), EuphoriaComponent::getForcedAstralPlaneTicks, "forced_astral_plane_ticks");
        }))).then(CommandManager.literal("set").then(CommandManager.argument("ticks", IntegerArgumentType.integer(0)).executes((ctx) -> {
            return executeSet(ctx.getSource(), EntityArgumentType.getPlayers(ctx, "targets"), EuphoriaComponent::setForcedAstralPlaneTicks, IntegerArgumentType.getInteger(ctx, "ticks"), "forced_astral_plane_ticks");
        })))).then(((LiteralArgumentBuilder)CommandManager.literal("visual_euphoria").then(CommandManager.literal("get").executes((ctx) -> {
            return executeGet(ctx.getSource(), EntityArgumentType.getPlayers(ctx, "targets"), EuphoriaComponent::getVisualEuphoriaTicks, "visual_euphoria");
        }))).then(CommandManager.literal("set").then(CommandManager.argument("ticks", IntegerArgumentType.integer(0)).executes((ctx) -> {
            return executeSet(ctx.getSource(), EntityArgumentType.getPlayers(ctx, "targets"), EuphoriaComponent::setVisualEuphoriaTicks, IntegerArgumentType.getInteger(ctx, "ticks"), "visual_euphoria");
        }))))));
    }

    private static <T> int executeGet(ServerCommandSource source, Collection<ServerPlayerEntity> targets, Function<EuphoriaComponent, T> getter, String type) {
        targets.forEach((target) -> {
            EuphoriaComponent component = target.getComponent(EuphoricsEntityComponents.EUPHORIA);
            source.sendFeedback(Text.translatable("commands.euphorics." + type + ".get", target.getName(), getter.apply(component)), false);
        });
        return 1;
    }

    private static <T> int executeSet(ServerCommandSource source, Collection<ServerPlayerEntity> targets, BiConsumer<EuphoriaComponent, T> setter, T value, String type) {
        targets.forEach((target) -> {
            EuphoriaComponent component = target.getComponent(EuphoricsEntityComponents.EUPHORIA);
            setter.accept(component, value);
            EuphoricsEntityComponents.EUPHORIA.sync(target);
            source.sendFeedback(Text.translatable("commands.euphorics." + type + ".set", target.getName(), value), false);
        });
        return 1;
    }

    private static int executeResetLastUse(ServerCommandSource source, Collection<ServerPlayerEntity> targets) {
        targets.forEach((target) -> {
            EuphoriaComponent component = target.getComponent(EuphoricsEntityComponents.EUPHORIA);
            component.setLastUse(component.getEuphoriaTicks());
            EuphoricsEntityComponents.EUPHORIA.sync(target);
            source.sendFeedback(Text.translatable("commands.euphorics.last_use.reset", target.getName()), false);
        });
        return 1;
    }
}
