package com.github.gtexpert.inb.core;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.registries.IForgeRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.ingredients.GTRecipeInput;

import com.github.gtexpert.inb.api.INBValues;
import com.github.gtexpert.inb.api.modules.IINBModule;
import com.github.gtexpert.inb.api.modules.INBModule;
import com.github.gtexpert.inb.api.recipes.INBRecipeMaps;
import com.github.gtexpert.inb.common.CommonProxy;
import com.github.gtexpert.inb.modules.INBModules;

@INBModule(
           moduleID = INBModules.MODULE_CORE,
           containerID = INBValues.MODID,
           name = "ImplosionNoBomb",
           coreModule = true)
public class INBCoreModule implements IINBModule {

    public static final Logger logger = LogManager.getLogger("ImplosionNoBomb");

    @SidedProxy(modId = INBValues.MODID,
                clientSide = "com.github.gtexpert.inb.client.ClientProxy",
                serverSide = "com.github.gtexpert.inb.common.CommonProxy")
    public static CommonProxy proxy;

    @Override
    public @NotNull Logger getLogger() {
        return logger;
    }

    @Override
    public void construction(FMLConstructionEvent event) {}

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);

        RecipeMaps.IMPLOSION_RECIPES.onRecipeBuild(
                recipeBuilder -> {
                    List<ItemStack> inputs = recipeBuilder.getInputs().stream()
                            .map(GTRecipeInput::getInputStacks)
                            .filter(stacks -> stacks.length > 0)
                            .map(stacks -> stacks[0])
                            .collect(Collectors.toList());
                    if (INBRecipeMaps.ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES
                            .findRecipe(Long.MAX_VALUE, inputs, Collections.emptyList()) != null) {
                        return;
                    }
                    INBRecipeMaps.ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES.recipeBuilder()
                            .inputs(recipeBuilder.getInputs().toArray(new GTRecipeInput[0]))
                            .outputs(recipeBuilder.getOutputs())
                            .chancedOutputs(recipeBuilder.getChancedOutputs())
                            .buildAndRegister();
                });
    }

    @Override
    public void init(FMLInitializationEvent event) {}

    @Override
    public void postInit(FMLPostInitializationEvent event) {}

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
    }

    @Override
    public void registerRecipesNormal(RegistryEvent.Register<IRecipe> event) {}

    @Override
    public void registerRecipesLowest(RegistryEvent.Register<IRecipe> event) {}
}
