package com.github.gtexpert.inb.mixin.gcym;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;

import gregicality.multiblocks.common.metatileentities.multiblock.standard.MetaTileEntityElectricImplosionCompressor;

import com.github.gtexpert.inb.api.recipes.INBRecipeMaps;

@Mixin(value = MetaTileEntityElectricImplosionCompressor.class, remap = false)
public class MetaTileEntityElectricImplosionCompressorMixin {

    // Thanks to @sysnote8main
    @ModifyArg(method = "<init>",
               at = @At(value = "INVOKE",
                        target = "Lgregtech/api/metatileentity/multiblock/RecipeMapMultiblockController;<init>(Lnet/minecraft/util/ResourceLocation;Lgregtech/api/recipes/RecipeMap;)V"),
               index = 1)
    private static RecipeMap<SimpleRecipeBuilder> injectArg(RecipeMap<SimpleRecipeBuilder> a) {
        return INBRecipeMaps.ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES;
    }
}
