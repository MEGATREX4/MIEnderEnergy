package com.megatrex4.block.energy;

import java.util.UUID;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

public class EnderEnergyStorageUtil {

    /**
     * Moves energy from GlobalEnergyStorage (UUID-based) to EnergyStorage (block/item-based).
     *
     * @param from The UUID of the player/entity to extract energy from.
     * @param to   The EnergyStorage object to insert energy into (e.g., a block or item).
     * @param maxAmount The maximum amount of energy to move.
     * @param transaction The transaction context.
     * @return The amount of energy successfully moved.
     */
    public static long move(UUID from, @Nullable EnergyStorage to, long maxAmount, @Nullable TransactionContext transaction) {
        if (to != null) {
            StoragePreconditions.notNegative(maxAmount);
            Transaction moveTransaction = Transaction.openNested(transaction);

            // Get energy from GlobalEnergyStorage using the UUID
            long energyInStorage = GlobalEnergyStorage.getEnergy(from);
            long maxExtracted = Math.min(energyInStorage, maxAmount); // Max extracted is limited by the energy in storage

            // Extract energy from GlobalEnergyStorage
            try {
                GlobalEnergyStorage.removeEnergy(from, maxExtracted); // Remove energy from GlobalEnergyStorage
            } catch (Throwable var14) {
                if (moveTransaction != null) {
                    try {
                        moveTransaction.close();
                    } catch (Throwable var13) {
                        var14.addSuppressed(var13);
                    }
                }

                throw var14;
            }

            // Try to insert the extracted energy into the EnergyStorage
            if (moveTransaction != null) {
                moveTransaction.close();
            }

            moveTransaction = Transaction.openNested(transaction);

            long var10;
            label67: {
                try {
                    long accepted = to.insert(maxExtracted, moveTransaction); // Insert into EnergyStorage
                    if (accepted > 0) {
                        moveTransaction.commit();
                        var10 = accepted;
                        break label67;
                    }
                } catch (Throwable var15) {
                    if (moveTransaction != null) {
                        try {
                            moveTransaction.close();
                        } catch (Throwable var12) {
                            var15.addSuppressed(var12);
                        }
                    }

                    throw var15;
                }

                if (moveTransaction != null) {
                    moveTransaction.close();
                }

                return 0L;
            }

            if (moveTransaction != null) {
                moveTransaction.close();
            }

            return var10;
        } else {
            return 0L;
        }
    }

    public static long moveFrom(@Nullable EnergyStorage from, UUID to, long maxAmount, @Nullable TransactionContext transaction) {
        if (from != null) {
            StoragePreconditions.notNegative(maxAmount);
            Transaction moveTransaction = Transaction.openNested(transaction);

            // Get energy from EnergyStorage
            long energyInStorage = from.getAmount();
            long maxExtracted = Math.min(energyInStorage, maxAmount); // Max extracted is limited by the energy in storage

            // Extract energy from EnergyStorage
            try {
                from.extract(maxExtracted, null); // Extract energy from the EnergyStorage
            } catch (Throwable var14) {
                if (moveTransaction != null) {
                    try {
                        moveTransaction.close();
                    } catch (Throwable var13) {
                        var14.addSuppressed(var13);
                    }
                }

                throw var14;
            }

            // Try to insert the extracted energy into GlobalEnergyStorage using UUID
            if (moveTransaction != null) {
                moveTransaction.close();
            }

            moveTransaction = Transaction.openNested(transaction);

            long accepted = 0L;
            long var10;
            label67: {
                try {
                    // Insert the extracted energy into GlobalEnergyStorage
                    GlobalEnergyStorage.addEnergy(to, maxExtracted);  // Assuming this method doesn't return a value
                    accepted = maxExtracted;
                    if (accepted > 0) {
                        moveTransaction.commit();
                        var10 = accepted;
                        break label67;
                    }
                } catch (Throwable var15) {
                    if (moveTransaction != null) {
                        try {
                            moveTransaction.close();
                        } catch (Throwable var12) {
                            var15.addSuppressed(var12);
                        }
                    }

                    throw var15;
                }

                if (moveTransaction != null) {
                    moveTransaction.close();
                }

                return 0L;
            }

            if (moveTransaction != null) {
                moveTransaction.close();
            }

            return var10;
        } else {
            return 0L;
        }
    }


    public static boolean isEnergyStorage(ItemStack stack) {
        return ContainerItemContext.withConstant(stack).find(EnergyStorage.ITEM) != null;
    }

    private void EnergyStorageUtil() {
    }
}
