package net.orhanbalci.pisti.event;

import io.vavr.control.Either;

public interface Visitor<V, G>{
    public Either<V, G> visit(CardPlayedEvent event);
    public Either<V, G> visit(CardsDealedEvent event);
    public Either<V, G> visit(CardsExhaustedEvent event);
    public Either<V, G> visit(CardsWonEvent event);
    public Either<V, G> visit(GameInitedEvent event);
    public Either<V,G> visit(GameOverEvent event);
    public Either<V,G> visit(PlayerSeatedEvent event);
    public Either<V,G> visit(PointScoredEvent event);
    public Either<V,G> visit(TurnChangedEvent event);    
}