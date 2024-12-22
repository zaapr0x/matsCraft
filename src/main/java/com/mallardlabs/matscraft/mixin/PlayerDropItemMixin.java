package com.mallardlabs.matscraft.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerDropItemMixin {

    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"), cancellable = true)
    private void onDropItem(ItemStack stack, boolean throwRandomly, CallbackInfoReturnable<ItemEntity> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        World world = player.getWorld();

        // Pastikan hanya berjalan di sisi server dan item yang dibuang bukan item kosong
        if (!world.isClient && stack != null && !stack.isEmpty()) {
            String itemName = stack.getItem().getName().getString();
            int quantity = stack.getCount();

            // Kirim pesan ke pemain
            player.sendMessage(Text.of("You dropped " + quantity + "x " + itemName), false);

            // Log event
            System.out.println("DROP - Player: " + player.getName().getString() + ", Item: " + itemName + ", Quantity: " + quantity);
        }
    }
}
