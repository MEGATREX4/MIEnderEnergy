package com.megatrex4.block;

import aztech.modern_industrialization.api.energy.CableTier;
import com.megatrex4.block.energy.formatEnergy;
import com.megatrex4.block.entity.SolarPanelBlockEntity;
import com.megatrex4.registry.BlockEntityRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {

        String formattedCapacity = formatEnergy.format(capacity);
        String formattedExtractionRate = formatEnergy.format(extractionRate);

        tooltip.add(Text.translatable("tooltip.mienderenergy.solar_panel.capacity")
                .append(Text.literal(" " + formattedCapacity).formatted(Formatting.GOLD)));

        tooltip.add(Text.translatable("tooltip.mienderenergy.solar_panel.extraction_rate")
                .append(Text.literal(" " + formattedExtractionRate).formatted(Formatting.GOLD)));
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return type == BlockEntityRegistry.SOLAR_PANEL_BLOCK_ENTITY ?
                (world1, pos, state1, blockEntity) -> ((SolarPanelBlockEntity) blockEntity).tick() : null;
    }
}
