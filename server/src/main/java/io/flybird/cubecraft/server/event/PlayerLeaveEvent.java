package io.flybird.cubecraft.server.event;

import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.util.event.Event;

public record PlayerLeaveEvent(Player player) implements Event {}
