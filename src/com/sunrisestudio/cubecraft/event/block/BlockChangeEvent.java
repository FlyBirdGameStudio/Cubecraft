package com.sunrisestudio.cubecraft.event.block;

import com.sunrisestudio.cubecraft.event.CancelCallback;
import com.sunrisestudio.cubecraft.world.block.Block;

public record BlockChangeEvent(Block newBlock, CancelCallback cancelCallback){
}
