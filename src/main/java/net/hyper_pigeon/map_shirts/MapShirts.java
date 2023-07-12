package net.hyper_pigeon.map_shirts;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hyper_pigeon.map_shirts.networking.MapShirtsNetworkingConstants;
import net.hyper_pigeon.map_shirts.recipe.MapArmorRecipe;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class MapShirts implements ModInitializer {

    public static RecipeSerializer<MapArmorRecipe> MAP_ARMOR_RECIPE_SERIALIZER;

    @Override
    public void onInitialize() {
        MAP_ARMOR_RECIPE_SERIALIZER =  Registry.register(Registries.RECIPE_SERIALIZER, new Identifier("map_shirts","crafting_special_map_armor"),
                new SpecialRecipeSerializer<>(MapArmorRecipe::new));

        ServerPlayNetworking.registerGlobalReceiver(MapShirtsNetworkingConstants.GET_MAP_STATE, ((server, player, handler, buf, responseSender) -> {
            int mapId = buf.readInt();

            server.execute(() -> {
                MapState mapState = FilledMapItem.getMapState(mapId, player.getServerWorld());

                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound = mapState.writeNbt(nbtCompound);

                PacketByteBuf packetByteBuf = PacketByteBufs.create();

                packetByteBuf.writeInt(mapId);
                packetByteBuf.writeNbt(nbtCompound);

                ServerPlayNetworking.send(player,MapShirtsNetworkingConstants.PUT_MAP_STATE,packetByteBuf);

            });
        }));

        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            if(stack.getItem() instanceof ArmorItem armorItem && armorItem.getSlotType() == EquipmentSlot.CHEST && stack.getOrCreateNbt().contains("mapId")) {
                lines.add(Text.of("Glued to Map #" + stack.getOrCreateNbt().get("mapId")));
            }
        });
    }
}
