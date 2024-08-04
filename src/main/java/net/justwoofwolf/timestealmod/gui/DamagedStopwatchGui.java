package net.justwoofwolf.timestealmod.gui;

import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.justwoofwolf.timestealmod.TimestealMod;
import net.justwoofwolf.timestealmod.items.ModItems;
import net.justwoofwolf.timestealmod.utils.PlayerData;
import net.justwoofwolf.timestealmod.utils.StateSaverAndLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;

public class DamagedStopwatchGui extends SimpleGui {
    /**
     * Constructs a new simple container gui for the supplied player.
     *
     * @param type                  the screen handler that the client should display
     * @param player                the player to server this gui to
     * @param manipulatePlayerSlots if <code>true</code> the players inventory
     *                              will be treated as slots of this gui
     */
    public DamagedStopwatchGui(ScreenHandlerType<?> type, ServerPlayerEntity player, boolean manipulatePlayerSlots) {
        super(type, player, manipulatePlayerSlots);
    }

    @Override
    public boolean onAnyClick(int index, ClickType type, SlotActionType action) {
        GuiElementInterface element = getSlot(index);
        if (element == null) return false;

        // Set time to target and title
        MinecraftServer server = this.player.getServer();
        assert server != null;
        server.getCommandManager().executeWithPrefix(server.getCommandSource(),
                "title ${name} title [\"\",{\"text\":\"abc\",\"obfuscated\":true,\"color\":\"dark_red\"},{\"text\":\" Welcome to your Final Hour. \",\"color\":\"dark_red\"},{\"text\":\"abc\",\"obfuscated\":true,\"color\":\"dark_red\"}]"
                        .replace("${name}", element.getItemStack().getName().getString()));

        ServerPlayerEntity target = server.getPlayerManager().getPlayer(element.getItemStack().getName().getString());
        assert target != null;
        PlayerData playerState = StateSaverAndLoader.getPlayerState(target);
        playerState.timeLeft = 72000L;

        PacketByteBuf data = PacketByteBufs.create();
        data.writeLong(playerState.timeLeft);
        server.execute(() -> ServerPlayNetworking.send(target, TimestealMod.TIME_LEFT, data));

        target.getWorld().playSound(null, target.getBlockPos(), SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.MASTER, 1f, 1f);
        player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.MASTER, 1f, 1f);

        // Broadcast to server
        server.getPlayerManager().broadcast(Text.literal(element.getItemStack().getName().getString() + " has been brought down to 1 hour.")
                .setStyle(Style.EMPTY.withColor(Formatting.RED)), false);

        close();

        if (!player.isCreative()) {
            if (player.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.DAMAGED_STOPWATCH)) {
                player.getStackInHand(Hand.MAIN_HAND).decrement(1);
            } else {
                player.getStackInHand(Hand.OFF_HAND).decrement(1);
            }
        }

        return false;
    }
}
