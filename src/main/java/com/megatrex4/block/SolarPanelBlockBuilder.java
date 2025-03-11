package com.megatrex4.block;

import aztech.modern_industrialization.api.energy.CableTier;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.sound.BlockSoundGroup;

public class SolarPanelBlockBuilder {
    private final long capacity;
    private final long extractionRate;
    private final long generationRate;
    private final CableTier tier;

    public SolarPanelBlockBuilder(long capacity, long extractionRate, long generationRate, CableTier tier) {
        this.capacity = capacity;
        this.extractionRate = extractionRate;
        this.generationRate = generationRate;
        this.tier = tier;
    }

    public SolarPanelBlock build() {
        return new SolarPanelBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).strength(3.0f).sounds(BlockSoundGroup.METAL),
                capacity, extractionRate, generationRate, tier);
    }
}
