package net.orhanbalci.pisti.event;

import java.util.UUID;

import io.vavr.control.Either;
import net.orhanbalci.pisti.GameState;
import net.orhanbalci.pisti.GameStateValidator.ValidationResult;

public class CardsExhaustedEvent extends GameEvent {

    public CardsExhaustedEvent(UUID gameId){
        super(gameId);
    }

    @Override
    public Either<ValidationResult, GameState> allowVisit(Visitor<ValidationResult, GameState> v) {
        return v.visit(this);
    }

    @Override
    public String toString(){
        return String.format("CardsExhaustedEvent(%s)", getGameId());
    }

}