package com.flybirdstudio.cubecraft.event.block;

import com.flybirdstudio.cubecraft.event.CancelCallback;
import com.flybirdstudio.cubecraft.world.block.BlockState;
import com.flybirdstudio.util.event.Event;

public record BlockChangeEvent(long x,long y,long z,BlockState newBlockState)implements Event {}
