package com.limeshulkerbox.youreafishnow.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.SpawnLocating;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnLocating.class)
abstract class SpawnInWaterMixin {

    //Lnet/minecraft/server/network/SpawnLocating;findOverworldSpawn(Lnet/minecraft/server/world/ServerWorld;IIZ)Lnet/minecraft/util/math/BlockPos;
    //ServerWorld world, int x, int z, boolean validSpawnNeeded, CallbackInfoReturnable<BlockPos> cir
        @Inject(method = "findOverworldSpawn",at = @At("HEAD"), cancellable = true)
        private static void newSpawn(ServerWorld world, int x, int z, CallbackInfoReturnable<BlockPos> cir) {
            boolean bl = world.getDimension().hasCeiling();
            WorldChunk worldChunk = world.getChunk(ChunkSectionPos.getSectionCoord(x), ChunkSectionPos.getSectionCoord(z));
            int i = bl ? world.getChunkManager().getChunkGenerator().getSpawnHeight(world) : worldChunk.sampleHeightmap(Heightmap.Type.MOTION_BLOCKING, x & 15, z & 15);
            if (i < world.getBottomY()) {
                cir.setReturnValue(null);
                return;
            } else {
                int j = worldChunk.sampleHeightmap(Heightmap.Type.OCEAN_FLOOR, x & 15, z & 15);
                BlockPos.Mutable mutable = new BlockPos.Mutable();

                for (int k = i + 1; k > j; --k) {
                    if (!(world.getBiome(new BlockPos(x, k, z)).getCategory().equals(Biome.Category.OCEAN) || world.getBiome(new BlockPos(x, k, z)).getCategory().equals(Biome.Category.RIVER))) continue;
                    mutable.set(x, k, z);
                    BlockState blockState = world.getBlockState(mutable);
                    BlockState blockState1 = world.getBlockState(mutable.down());
                    if (blockState.getBlock().getDefaultState().equals(Blocks.WATER.getDefaultState()) && blockState1.getBlock().getDefaultState().equals(Blocks.WATER.getDefaultState())) {
                        cir.setReturnValue(mutable.down().toImmutable());
                        return;
                    }
                }

                cir.setReturnValue(null);
                return;
            }

            /*
            BlockPos.Mutable mutable = new BlockPos.Mutable(x, 0, z);
            WorldChunk worldChunk = world.getChunk(x >> 4, z >> 4);
            int i = 60;//worldChunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE, x & 15, z & 15);
            if (i < 0) {
                System.out.println("Error i <0");
                cir.setReturnValue(new BlockPos(-15,45,-15));
                return;
            } else {
                int j = worldChunk.sampleHeightmap(Heightmap.Type.OCEAN_FLOOR, x & 15, z & 15);
                for(int k = i+1; k>j;k--){
                    mutable.set(x,k,z);
                    BlockState blockState = world.getBlockState(mutable);
                    BlockState blockState1 = world.getBlockState(mutable.down());
                    if(blockState.getBlock().getDefaultState().equals(Blocks.WATER.getDefaultState()) && blockState1.getBlock().getDefaultState().equals(Blocks.WATER.getDefaultState())){
                        cir.setReturnValue(new BlockPos(x,k,z));
                        return;
                    }
                }
                cir.setReturnValue(null);
                return;
            }
        }*/
        }
    }