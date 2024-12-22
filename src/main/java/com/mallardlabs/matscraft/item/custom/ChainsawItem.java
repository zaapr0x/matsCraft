package com.mallardlabs.matscraft.item.custom;

import com.mallardlabs.matscraft.sound.ModSounds;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import java.util.Objects;
public class ChainsawItem extends Item {
    public ChainsawItem(Settings settings) {
        super(settings);
    }
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();

        if(!world.isClient()) {
            if(world.getBlockState(context.getBlockPos()).isIn(BlockTags.LOGS)) {
                world.breakBlock(context.getBlockPos(), true, context.getPlayer());

                context.getStack().damage(1, ((ServerWorld) world), ((ServerPlayerEntity) context.getPlayer()), item ->
                        Objects.requireNonNull(context.getPlayer()).sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND));

                context.getWorld().playSound(null, context.getBlockPos(), ModSounds.CHAINSAW_CUT, SoundCategory.BLOCKS, 1f, 1f);
            } else {
                context.getWorld().playSound(null, context.getBlockPos(), ModSounds.CHAINSAW_PULL, SoundCategory.BLOCKS, 1f, 1f);
            }
        }
        return ActionResult.CONSUME;
    }
}