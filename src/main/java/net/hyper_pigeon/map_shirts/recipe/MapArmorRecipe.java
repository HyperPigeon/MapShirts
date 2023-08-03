package net.hyper_pigeon.map_shirts.recipe;

import net.hyper_pigeon.map_shirts.MapShirts;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class MapArmorRecipe extends SpecialCraftingRecipe {
    private static final Ingredient MAP = Ingredient.ofItems(Items.FILLED_MAP);
    private static final Ingredient SLIME_BALL = Ingredient.ofItems(Items.SLIME_BALL);

    public MapArmorRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        boolean hasMapIngredient = false;
        boolean hasSlimeballIngredient = false;
        boolean hasArmorIngredient = false;

        for(int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack = inventory.getStack(i);
            if (!itemStack.isEmpty()) {
                if (MAP.test(itemStack)) {
                    if (hasMapIngredient) {
                        return false;
                    }

                    hasMapIngredient = true;
                }
                else if (SLIME_BALL.test(itemStack)) {
                    if (hasSlimeballIngredient) {
                        return false;
                    }

                    hasSlimeballIngredient = true;
                }
                else if (itemStack.getItem() instanceof ArmorItem armorItem && armorItem.getSlotType() == EquipmentSlot.CHEST) {
                    if (hasArmorIngredient) {
                        return false;
                    }

                    hasArmorIngredient = true;
                }
                else {
                    return false;
                }
            }
        }

        return hasMapIngredient && hasSlimeballIngredient && hasArmorIngredient;
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        ItemStack armorStack = ItemStack.EMPTY;
        ItemStack mapStack = ItemStack.EMPTY;
        for(int i = 0; i < inventory.size(); ++i) {
            ItemStack inventoryStack = inventory.getStack(i);
            if(inventoryStack.getItem() instanceof ArmorItem armorItem && armorItem.getSlotType() == EquipmentSlot.CHEST) {
                armorStack = inventoryStack.copy();
                armorStack.setCount(1);
            }
            else if(MAP.test(inventoryStack)) {
                mapStack = inventoryStack;
            }

        }

        if(!mapStack.equals(ItemStack.EMPTY) && !armorStack.equals(ItemStack.EMPTY)){

            int mapId = FilledMapItem.getMapId(mapStack);

            armorStack.getOrCreateNbt().putBoolean("IsMapShirt", true);
            armorStack.getOrCreateNbt().putInt("mapId",mapId);



        }

        return armorStack;
    }


    @Override
    public boolean fits(int width, int height) {
        return width*height >=3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MapShirts.MAP_ARMOR_RECIPE_SERIALIZER;
    }
}