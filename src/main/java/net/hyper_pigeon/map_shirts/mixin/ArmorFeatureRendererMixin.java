package net.hyper_pigeon.map_shirts.mixin;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.hyper_pigeon.map_shirts.networking.MapShirtsNetworkingConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin {

    @Inject(method = "renderArmor", at = @At("TAIL"))
    private void renderMap(MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity entity, EquipmentSlot armorSlot, int light, BipedEntityModel model, CallbackInfo ci){
        ItemStack itemStack = entity.getEquippedStack(armorSlot);
        Item bl = itemStack.getItem();
        if (bl instanceof ArmorItem armorItem && armorItem.getSlotType() == EquipmentSlot.CHEST) {
            if(itemStack.hasNbt() && itemStack.getNbt().contains("mapId")) {

                int mapID = itemStack.getNbt().getInt("mapId");
                MapState mapState = FilledMapItem.getMapState(mapID, entity.getWorld());

                if(mapState != null) {

                    if (entity instanceof ClientPlayerEntity player && player.isInSneakingPose() && !model.riding && !player.isSwimming()) {
                        matrices.translate(0.0F, 0.2F, 0.0F);
                        matrices.multiply(RotationAxis.POSITIVE_X.rotation(model.body.pitch));
                    }
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotation(model.body.yaw));
                    matrices.translate(-0.185F, 0.2F, -0.20F);
                    matrices.scale(0.003F, 0.003F, 0.003F);

                    MinecraftClient.getInstance()
                            .gameRenderer
                            .getMapRenderer()
                            .draw(matrices, vertexConsumers, 1, mapState, true, light);
                }
                else {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeInt(mapID);
                    ClientPlayNetworking.send(MapShirtsNetworkingConstants.GET_MAP_STATE, buf);
                }
            }


        }
    }

}
