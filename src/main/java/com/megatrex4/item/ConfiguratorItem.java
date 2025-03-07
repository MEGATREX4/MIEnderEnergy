package com.megatrex4.item;

import com.megatrex4.MIEnderEnergyConfig;
import com.megatrex4.block.energy.GlobalEnergyStorage;
import com.megatrex4.block.energy.formatEnergy;
import com.megatrex4.block.entity.WirelessControllerBlockEntity;
import com.megatrex4.block.entity.WirelessOutletBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ConfiguratorItem extends Item {

    public ConfiguratorItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        if (world.isClient || player == null) return ActionResult.FAIL;

        BlockPos pos = context.getBlockPos();
        BlockEntity blockEntity = world.getBlockEntity(pos);

        ItemStack stack = context.getStack();
        NbtCompound nbt = stack.getNbt();

        // Add null check for NBT data
        if (nbt == null) {
            nbt = new NbtCompound();  // Create new NBT if it's null
        }

        if (blockEntity instanceof WirelessControllerBlockEntity wirelessEntity) {
            UUID uuid = wirelessEntity.getUUID();
            nbt.putUuid("ControllerUUID", uuid);
            stack.setNbt(nbt);
            player.sendMessage(Text.literal("Stored UUID: " + uuid), true);
            player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1f);
            return ActionResult.SUCCESS;
        }

        if (nbt.contains("ControllerUUID")) {
            UUID storedUUID = nbt.getUuid("ControllerUUID");

            // If clicked on WirelessOutletBlockEntity, set the UUID
            if (blockEntity instanceof WirelessOutletBlockEntity outletEntity) {
                outletEntity.setUUID(storedUUID);
                player.sendMessage(Text.literal("UUID linked: " + storedUUID), true);
                player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1f);
                return ActionResult.SUCCESS;
            }
        } else {
            player.sendMessage(Text.literal("No stored UUID in Configurator!"), true);
            player.playSound(SoundEvents.ENTITY_VILLAGER_NO, 0.5f, 1f);
        }

        return ActionResult.FAIL;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains("ControllerUUID")) {
            UUID uuid = nbt.getUuid("ControllerUUID");
            long energy = GlobalEnergyStorage.getEnergy(uuid);
            long capacity = MIEnderEnergyConfig.SERVER.MAX_NETWORK_ENERGY;

            // Format the energy values using formatEnergy class
            String formattedStoredEnergy = formatEnergy.format(energy);
            String formattedCapacity = formatEnergy.format(capacity);

            tooltip.add(Text.translatable("tooltip.mienderenergy.energy_stored")
                    .append(Text.literal(" " + formattedStoredEnergy + " / " + formattedCapacity)
                            .formatted(Formatting.GOLD)));

            tooltip.add(Text.literal(uuid.toString()).formatted(Formatting.DARK_GRAY));
        }
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (world.isClient) return TypedActionResult.success(stack);

        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains("ControllerUUID")) {
            UUID uuid = nbt.getUuid("ControllerUUID");
            player.sendMessage(Text.literal("Controller UUID: " + uuid), false);
            return TypedActionResult.success(stack);
        }

        player.sendMessage(Text.literal("No controller UUID stored."), false);
        return TypedActionResult.fail(stack);
    }

}
