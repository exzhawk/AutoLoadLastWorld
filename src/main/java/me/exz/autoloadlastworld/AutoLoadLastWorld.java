package me.exz.autoloadlastworld;

import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collections;
import java.util.List;

@Mod(modid = AutoLoadLastWorld.MODID, name = AutoLoadLastWorld.NAME, version = AutoLoadLastWorld.VERSION)
public class AutoLoadLastWorld {
    public static final String MODID = "autoloadlastworld";
    public static final String NAME = "Auto load last world";
    public static final String VERSION = "@VERSION@";
    static boolean firstTime = true;

    @SuppressWarnings("unused")
    @SubscribeEvent(priority = EventPriority.LOWEST)

    public static void openGui(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiMainMenu) {
            if (ModConfig.disabled) {
                return;
            }
            if (!firstTime && ModConfig.firstOnly) {
                return;
            }
            firstTime = false;
            if (GuiScreen.isShiftKeyDown() || GuiScreen.isAltKeyDown() || GuiScreen.isCtrlKeyDown()) {
                return;
            }
            try {
                Minecraft minecraft = Minecraft.getMinecraft();
                List<WorldSummary> worldSummaryList = minecraft.getSaveLoader().getSaveList();
                if (worldSummaryList.size() > 0) {
                    Collections.sort(worldSummaryList);
                    WorldSummary lastWorldSummary = worldSummaryList.get(0);
                    minecraft.launchIntegratedServer(lastWorldSummary.getFileName(), lastWorldSummary.getDisplayName(), null);
                }
            } catch (AnvilConverterException e) {
                e.printStackTrace();
            }
        }
    }


    @SuppressWarnings("unused")
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(AutoLoadLastWorld.class);
    }
}

@Config(modid = AutoLoadLastWorld.MODID)
final class ModConfig {
    @Config.Comment("Only auto load world at the first time")
    public static boolean firstOnly = true;
    @Config.Comment("Disable auto load world")
    public static boolean disabled = false;
}
