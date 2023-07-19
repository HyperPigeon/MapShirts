package net.hyper_pigeon.map_shirts.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.hyper_pigeon.map_shirts.networking.MapShirtsNetworkingConstants;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.map.MapState;
import net.minecraft.text.Text;

import java.util.Objects;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class MapShirtsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(MapShirtsNetworkingConstants.PUT_MAP_STATE, (client, handler, buf, responseSender) -> {
            int mapId = buf.readInt();
            MapState mapState = MapState.fromNbt(Objects.requireNonNull(buf.readNbt()));
            client.execute(() -> {
                if(client.world != null) {
                    client.world.putClientsideMapState(FilledMapItem.getMapName(mapId), mapState);
                }
            });
        });

        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            if(stack.getItem() instanceof ArmorItem armorItem && armorItem.getSlotType() == EquipmentSlot.CHEST && stack.getOrCreateNbt().contains("mapId")) {
                lines.add(Text.of("Glued to Map #" + stack.getOrCreateNbt().get("mapId")));
            }
        });
    }

}
