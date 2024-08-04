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

public class RewindHourglassGui extends SimpleGui {
    /**
     * Constructs a new simple container gui for the supplied player.
     *
     * @param type                  the screen handler that the client should display
     * @param player                the player to server this gui to
     * @param manipulatePlayerSlots if <code>true</code> the players inventory
     *                              will be treated as slots of this gui
     */
    public RewindHourglassGui(ScreenHandlerType<?> type, ServerPlayerEntity player, boolean manipulatePlayerSlots) {
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
                "pardon ${name}".replace("${name}", element.getItemStack().getName().getString()));

        player.getServer().getPlayerManager().broadcast(Text.literal(element.getItemStack().getName().getString() + " has been revived.")
                .setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)), false);

        for (ServerPlayerEntity entity : player.getServer().getPlayerManager().getPlayerList()) {
            entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.MASTER, 1f, 1f);
        }

        close();

        if (!player.isCreative()) {
            if (player.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.REWIND_HOURGLASS)) {
                player.getStackInHand(Hand.MAIN_HAND).decrement(1);
            } else {
                player.getStackInHand(Hand.OFF_HAND).decrement(1);
            }
        }

        return false;
    }
}
