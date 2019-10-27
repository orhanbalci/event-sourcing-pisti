package net.orhanbalci.pisti.command;

import java.util.UUID;

import io.vavr.collection.List;
import net.orhanbalci.pisti.PointType;

public class ScorePointCommand extends Command {
    private List<PointType> points;
    private UUID player;

    public ScorePointCommand(UUID gameId, UUID player, List<PointType> points) {
        super(gameId);
        this.player = player;
        this.points = points;
    }

    public UUID getPlayer() {
        return player;
    }

    public List<PointType> getPoints() {
        return points;
    }
}