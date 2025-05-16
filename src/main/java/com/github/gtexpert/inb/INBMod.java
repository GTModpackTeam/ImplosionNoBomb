package com.github.gtexpert.inb;

import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import gregtech.GTInternalTags;
import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.registry.MTEManager;

import com.github.gtexpert.inb.api.INBValues;
import com.github.gtexpert.inb.api.util.INBLog;
import com.github.gtexpert.inb.api.util.Mods;
import com.github.gtexpert.inb.modules.INBModuleManager;
import com.github.gtexpert.inb.modules.INBModules;

@Mod(modid = INBValues.MODID,
     name = INBValues.MODNAME,
     acceptedMinecraftVersions = "[1.12.2]",
     version = Tags.VERSION,
     dependencies = GTInternalTags.DEP_VERSION_STRING + "required-after:" + Mods.Names.MODULRAUI + ";" +
             "required-after:" + Mods.Names.MIXINBOOTER + ";" +
             "required-after:" + Mods.Names.GREGICALITY_MULTIBLOCKS + ";")
@Mod.EventBusSubscriber(modid = INBValues.MODID)
public class INBMod {

    private INBModuleManager moduleManager;

    @Mod.EventHandler
    public void onConstruction(FMLConstructionEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        INBLog.logger.info("starting construction event...");
        moduleManager = INBModuleManager.getInstance();
        moduleManager.registerContainer(new INBModules());
        moduleManager.setup(event.getASMHarvestedData(), Loader.instance().getConfigDir());
        moduleManager.onConstruction(event);
        INBLog.logger.info("finished construction!");
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        moduleManager.onPreInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        moduleManager.onInit(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        moduleManager.onPostInit(event);
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        moduleManager.onLoadComplete(event);
    }

    @Mod.EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        moduleManager.onServerAboutToStart(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        moduleManager.onServerStarting(event);
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        moduleManager.onServerStarted(event);
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        moduleManager.onServerStopping(event);
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        moduleManager.onServerStopped(event);
    }

    @Mod.EventHandler
    public void respondIMC(FMLInterModComms.IMCEvent event) {
        moduleManager.processIMC(event.getMessages());
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        INBLog.logger.info("Registering Blocks...");
        moduleManager.registerBlocks(event);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        INBLog.logger.info("Registering Items...");
        moduleManager.registerItems(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void registerRecipesHighest(RegistryEvent.Register<IRecipe> event) {
        moduleManager.registerRecipesHighest(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void registerRecipesHigh(RegistryEvent.Register<IRecipe> event) {
        moduleManager.registerRecipesHigh(event);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        moduleManager.registerRecipesNormal(event);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void registerRecipesLow(RegistryEvent.Register<IRecipe> event) {
        moduleManager.registerRecipesLow(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerRecipesLowest(RegistryEvent.Register<IRecipe> event) {
        moduleManager.registerRecipesLowest(event);
    }

    public static <T extends Block> ItemBlock createItemBlock(T block, Function<T, ItemBlock> producer) {
        ItemBlock itemBlock = producer.apply(block);
        ResourceLocation registryName = block.getRegistryName();
        if (registryName == null) {
            INBLog.logger.error("Block has no registry name: {}", block.getTranslationKey(), new Throwable());
        } else {
            itemBlock.setRegistryName(registryName);
        }
        return itemBlock;
    }

    @SubscribeEvent
    public static void registerMTERegistry(MTEManager.MTERegistryEvent event) {
        GregTechAPI.mteManager.createRegistry(INBValues.MODID);
    }

    @SubscribeEvent
    public static void syncConfigValues(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(INBValues.MODID)) {
            ConfigManager.sync(INBValues.MODID, Config.Type.INSTANCE);
        }
    }
}
