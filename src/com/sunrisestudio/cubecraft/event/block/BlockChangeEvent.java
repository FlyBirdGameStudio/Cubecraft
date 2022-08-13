package com.sunrisestudio.cubecraft.event.block;

import com.sunrisestudio.cubecraft.event.CancelCallback;
import com.sunrisestudio.cubecraft.world.block.BlockState;

public record BlockChangeEvent(BlockState newBlockState, CancelCallback cancelCallback){
}
