package blackjack;

import blackjack.controller.BlackJackController;

public class Application {
    public static void main(String[] args) {
        final BlackJackController blackJackController = new BlackJackController();

        blackJackController.showParticipantsInitCards();

        blackJackController.inputHitCondition();
        blackJackController.showParticipantsStatus();

        blackJackController.showGameResult();
    }
}
