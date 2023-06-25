package net.hyper_pigeon.map_shirts.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.recipe.SmithingTrimRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SmithingMapRecipe implements SmithingRecipe {

    public static final SmithingMapRecipe.Serializer SERIALIZER = new SmithingMapRecipe.Serializer();

    @Override
    public boolean testTemplate(ItemStack stack) {
        return false;
    }

    @Override
    public boolean testBase(ItemStack stack) {
        return false;
    }

    @Override
    public boolean testAddition(ItemStack stack) {
        return false;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return false;
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return null;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return null;
    }

    @Override
    public Identifier getId() {
        return null;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    public static class Serializer implements RecipeSerializer<SmithingTrimRecipe> {

        @Override
        public SmithingTrimRecipe read(Identifier id, JsonObject json) {
            return null;
        }

        @Override
        public SmithingTrimRecipe read(Identifier id, PacketByteBuf buf) {
            return null;
        }

        @Override
        public void write(PacketByteBuf buf, SmithingTrimRecipe recipe) {

        }
    }
}
