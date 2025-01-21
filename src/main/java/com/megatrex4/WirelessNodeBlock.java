package com.megatrex4;

import com.megatrex4.blockentity.WirelessNodeBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;

public class WirelessNodeBlock extends BlockWithEntity implements BlockEntityProvider {

    public WirelessNodeBlock() {
        // Use AbstractBlock.Settings.create() to set block properties
        super(Block.Settings.create()
                .sounds(BlockSoundGroup.METAL)  // Use appropriate sound group
                .strength(3.0f, 4.0f));         // Set hardness and resistance (example values)
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WirelessNodeBlockEntity(pos, state);
    }
}
