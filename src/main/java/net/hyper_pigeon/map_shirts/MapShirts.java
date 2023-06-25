package net.hyper_pigeon.map_shirts;

import net.fabricmc.api.ModInitializer;
import net.hyper_pigeon.map_shirts.recipe.MapArmorRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class MapShirts implements ModInitializer {

    public static RecipeSerializer<MapArmorRecipe> MAP_ARMOR_RECIPE_SERIALIZER;

    @Override
    public void onInitialize() {
        MAP_ARMOR_RECIPE_SERIALIZER =  Registry.register(Registries.RECIPE_SERIALIZER, new Identifier("map_shirts","crafting_special_map_armor"),
                new SpecialRecipeSerializer<>(MapArmorRecipe::new));

    }
}
