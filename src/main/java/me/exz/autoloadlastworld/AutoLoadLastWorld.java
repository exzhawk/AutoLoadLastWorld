package me.exz.autoloadlastworld;

import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@Mod(modid = AutoLoadLastWorld.MODID, name = AutoLoadLastWorld.NAME, version = AutoLoadLastWorld.VERSION)
public class AutoLoadLastWorld {
    public static final String MODID = "autoloadlastworld";
    public static final String NAME = "Auto load last world";
    public static final String VERSION = "1.0";

    @SuppressWarnings("unused")
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void openGui(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiMainMenu) {
            if (!GuiScreen.isShiftKeyDown()) {
                try {
                    Minecraft minecraft = Minecraft.getMinecraft();
                    List<WorldSummary> worldSummaryList = minecraft.getSaveLoader().getSaveList();
                    if (worldSummaryList.size() > 0) {
                        WorldSummary firstWorldSummary = worldSummaryList.get(0);
                        minecraft.launchIntegratedServer(firstWorldSummary.getFileName(), firstWorldSummary.getDisplayName(), null);
                    }
                } catch (AnvilConverterException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @SuppressWarnings("unused")
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(AutoLoadLastWorld.class);
    }
}
