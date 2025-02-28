package com.megatrex4.block;

import aztech.modern_industrialization.api.energy.CableTier;
import com.megatrex4.block.entity.SolarPanelBlockEntity;
import com.megatrex4.block.entity.WirelessOutletBlockEntity;
import com.megatrex4.registry.BlockEntityRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SolarPanelBlock extends BlockWithEntity {

    private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 16, 16);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    private final long capacity;
    private final long extractionRate;
    private final CableTier tier;

    public SolarPanelBlock(Settings settings, long capacity, long extractionRate, CableTier tier) {
        super(settings);
        this.capacity = capacity;
        this.extractionRate = extractionRate;
        this.tier = tier;
    }

    public long getCapacity() {
        return capacity;
    }

    public long getExtractionRate() {
        return extractionRate;
    }

    public CableTier getTier() {
        return tier;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SolarPanelBlockEntity(pos, state, capacity, extractionRate, tier);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof SolarPanelBlockEntity solarPanel) {
                // Send block's settings to the player
                player.sendMessage(Text.literal("⚡ Solar Panel Info:")
                        .append("\n🔋 Capacity: " + solarPanel.getCapacity() + " EU")
                        .append("\n⚡ Extraction Rate: " + solarPanel.getExtractRate() + " EU/t")
                        .append("\n⚙️ Tier: " + solarPanel.getMITier()), false);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return type == BlockEntityRegistry.SOLAR_PANEL_BLOCK_ENTITY ?
                (world1, pos, state1, blockEntity) -> ((SolarPanelBlockEntity) blockEntity).tick() : null;
    }

}
