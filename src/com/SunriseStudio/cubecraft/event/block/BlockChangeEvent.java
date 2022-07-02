package com.SunriseStudio.cubecraft.event.block;

import com.SunriseStudio.cubecraft.event.CancelCallback;
import com.SunriseStudio.cubecraft.world.block.Block;

public record BlockChangeEvent(Block newBlock, CancelCallback cancelCallback){
}
