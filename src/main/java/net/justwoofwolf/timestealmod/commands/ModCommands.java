package net.justwoofwolf.timestealmod.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands {
    public static void RegisterCommands() {
        CommandRegistrationCallback.EVENT.register(SeasonCommand::register);
        CommandRegistrationCallback.EVENT.register(WithdrawCommand::register);
    }
}
