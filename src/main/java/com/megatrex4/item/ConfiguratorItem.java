package com.megatrex4.item;

import aztech.modern_industrialization.proxy.CommonProxy;
import com.megatrex4.block.energy.format;
import com.megatrex4.block.entity.WirelessControllerBlockEntity;
import com.megatrex4.block.entity.WirelessOutletBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
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
            player.sendMessage(Text.translatable("item.mienderenergy.configurator.stored_uuid"), true);
            player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.5f, 1f);
            return ActionResult.SUCCESS;
        }

        if (nbt.contains("ControllerUUID")) {
            UUID storedUUID = nbt.getUuid("ControllerUUID");

            // If clicked on WirelessOutletBlockEntity, set the UUID
            if (blockEntity instanceof WirelessOutletBlockEntity outletEntity) {
                outletEntity.setUUID(storedUUID);
                player.sendMessage(Text.translatable("item.mienderenergy.configurator.load"), true);
                player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.5f, 0.5f);
                return ActionResult.SUCCESS;
            }
        } else {
            player.sendMessage(Text.translatable("item.mienderenergy.configurator.no_uuid_stored"), true);
            player.playSound(SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.BLOCKS, 0.5f, 1f);
        }

        return ActionResult.FAIL;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains("ControllerUUID")) {
            UUID uuid = nbt.getUuid("ControllerUUID");

            tooltip.add(Text.literal(uuid.toString()).formatted(Formatting.DARK_GRAY));
        }
        tooltip.add(Text.literal(" "));
        if (CommonProxy.INSTANCE.hasShiftDown()) {

            String outlet = Text.translatable("block.mienderenergy.wireless_outlet_block").getString();
            String controller = Text.translatable("block.mienderenergy.wireless_controller_block").getString();

            String details = Text.translatable("tooltip.mienderenergy.configurator.details", controller, outlet).getString();

            format.fotmattedTooltips(tooltip, details);
        } else {
            tooltip.add(Text.translatable("tooltip.mienderenergy.more").formatted(Formatting.DARK_GRAY));
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
