package com.mallardlabs.matscraft.datagen;

import com.mallardlabs.matscraft.MatsCraft;
import com.mallardlabs.matscraft.block.ModBlocks;
import com.mallardlabs.matscraft.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModRecipeGenerator extends FabricRecipeProvider {
    public ModRecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, ModItems.MATS, RecipeCategory.MISC, ModItems.MATT_LUONGO);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.CHAINSAW)
                .pattern("MMM")
                .pattern("M M")
                .pattern("MMM")
                .input('M', ModItems.MATT_LUONGO)
                .criterion((hasItem(ModItems.MATT_LUONGO)), conditionsFromItem(ModItems.MATT_LUONGO))
                .offerTo(exporter, Identifier.of(MatsCraft.MOD_ID, "chainsaw"));
    }
}
