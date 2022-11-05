package io.flybird.cubecraft.event.block;

import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.util.event.Event;

public record BlockChangeEvent(long x,long y,long z,BlockState newBlockState)implements Event {}
