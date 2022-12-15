package io.flybird.cubecraft.server.event;

import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.util.event.Event;

public record PlayerKickEvent(Player player,String reason) implements Event {
}
