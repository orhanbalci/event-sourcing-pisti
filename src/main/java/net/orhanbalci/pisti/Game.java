package net.orhanbalci.pisti;

import java.util.UUID;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import io.vavr.collection.List;
import net.orhanbalci.pisti.command.InitGameCommand;
import net.orhanbalci.pisti.command.SeatPlayerCommand;
import net.orhanbalci.pisti.event.EventStorage;
import net.orhanbalci.pisti.event.GameEvent;
import net.orhanbalci.pisti.event.GameInitedEvent;
import net.orhanbalci.pisti.event.PlayerSeatedEvent;
import net.orhanbalci.pisti.player.DummyPlayer;
import net.orhanbalci.pisti.player.PlayerAgent;
import net.orhanbalci.pisti.command.*;

public class Game {

    private EventBus commandBus = new EventBus("GameCommandBus");
    private EventBus eventBus = new EventBus("GameEventBus");
    private EventBus eventStoreBus = new EventBus("GameEventStoreBus");
    private GameState gs = new GameState();
    private PlayerAgent[] agents = new PlayerAgent[4];
    private EventStore es = new EventStore();

    public Game() {

    }

    public void startGame() {
        agents[0] = new DummyPlayer(commandBus, UUID.randomUUID());
        agents[1] = new DummyPlayer(commandBus, UUID.randomUUID());
        agents[2] = new DummyPlayer(commandBus, UUID.randomUUID());
        agents[3] = new DummyPlayer(commandBus, UUID.randomUUID());
        for (PlayerAgent agent : agents) {
            eventBus.register(agent);
        }

        commandBus.register(this);
        eventBus.register(this);
        eventStoreBus.register(es);

        publishCommand(new InitGameCommand(UUID.randomUUID()));
    }

    @Subscribe
    public void handleCommand(InitGameCommand initGameCommand) {
        System.out.println("Game => HandleCommand called " + initGameCommand.toString());
        handleUnpublishedEvents(gs.handleCommand(initGameCommand));
    }

    @Subscribe
    public void handleCommand(SeatPlayerCommand seatPlayer) {
        System.out.println("Game => HandleCommand called " + seatPlayer.toString());
        handleUnpublishedEvents(gs.handleCommand(seatPlayer));
    }

    @Subscribe
    public void handleEvent(GameInitedEvent gameInited) {
        System.out.println("Game => HandleEvent called " + gameInited);
        if (gs.getPlayers().getOrElse(List.empty()).isEmpty()){
            publishCommand(new SeatPlayerCommand(gs.getGameId().get(), agents[0].getId()));
        }
    }

    @Subscribe
    public void handleEvent(PlayerSeatedEvent playerSeated) {
        System.out.println("Game => HandleEvent called " + playerSeated);
        //masada eksik oyuncu varsa tamamla
        if(gs.getPlayers().getOrElse(List.empty()).length() < 4){
            publishCommand(new SeatPlayerCommand(gs.getGameId().get(), agents[gs.getPlayers().getOrElse(List.empty()).length()].getId()));
        }
        //kartlari dagit
        else{

        }

    }

    public void handleUnpublishedEvents(GameState newState) {
        for (GameEvent var : newState.getUnpublishedEvents().getOrElse(List.empty())) {
            gs = var.allowVisit(gs).getOrElse(gs).markAsPublished();
            // store event
            storeEvent(var);
            publishEvent(var);
        }
    }

    private void storeEvent(GameEvent ge) {
        eventStoreBus.post(ge);
    }

    private void publishEvent(GameEvent ge) {
        eventBus.post(ge);
    }

    private void publishCommand(Command command) {
        commandBus.post(command);
    }

}