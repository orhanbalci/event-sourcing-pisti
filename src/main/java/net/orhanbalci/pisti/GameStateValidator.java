
package net.orhanbalci.pisti;

import java.util.UUID;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;

public class GameStateValidator {

    public enum ValidationResult {
        CenterPileIsNotEmpty("Masada kart var kart dagitamazsiniz"),
        PlayerPileNotEmpty("Oyuncuda kart var kart dagitamazsiniz"), 
        GameInitedBefore("Oyun onceden ilklendirilmis"),
        CardNotOnPlayer("Kart oyuncuda degil"), 
        Succesfull("Kontrol basarili")

        ;
        private String description;

        private ValidationResult(String description) {
            this.description = description;
        }
    }

    public static ValidationResult validateCenterPileEmpty(GameState gs) {
        if (gs.getCenterPile().getOrElse(List.empty()).isEmpty())
            return ValidationResult.Succesfull;
        else
            return ValidationResult.CenterPileIsNotEmpty;
    }

    public static ValidationResult validatePlayerPileEmpty(GameState gs, UUID playerId) {
        if (gs.getCards().getOrElse(HashMap.empty()).get(playerId).getOrElse(List.empty()).isEmpty())
            return ValidationResult.Succesfull;
        else
            return ValidationResult.PlayerPileNotEmpty;
    }

    public static ValidationResult validateGameUnited(GameState gs) {
        if (gs.getGameId().isEmpty())
            return ValidationResult.Succesfull;
        else
            return ValidationResult.GameInitedBefore;
    }

    public static ValidationResult validateCardOnPlayer(GameState gs, UUID player, Card c) {
        if (gs.getCards().getOrElse(HashMap.empty()).getOrElse(player, List.empty()).contains(c)) {
            return ValidationResult.Succesfull;
        } else
            return ValidationResult.CardNotOnPlayer;
    }
}