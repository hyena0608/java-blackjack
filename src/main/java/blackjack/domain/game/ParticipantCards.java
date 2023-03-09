package blackjack.domain.game;

import blackjack.domain.card.Card;
import blackjack.domain.card.CardNumber;
import blackjack.domain.card.Cards;
import blackjack.domain.card.Deck;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import static blackjack.domain.card.CardNumber.ACE;
import static blackjack.domain.card.CardNumber.ACE_CONVERT_NUMBER;

public class ParticipantCards {
    private static final int INITIAL_SIZE = 2;
    private static final int BLACK_JACK = 21;

    private final Cards cards;

    public ParticipantCards(final Deck deck) {
        this.cards = createDeckCards(deck);
    }

    public void receive(final Card card) {
        cards.receive(card);
    }

    public int getMaxValueNearBlackJack() {
        final List<CardNumber> cardNumbers = cards.getCardNumbers();
        final int aceCount = getAceCount(cardNumbers);
        final int sumBeforeOptimize = getSumBeforeOptimize(cardNumbers);
        return getSumAfterOptimize(aceCount, sumBeforeOptimize);
    }

    public List<Card> open(int openCount) {
        return cards.open(openCount);
    }

    public List<Card> openAll() {
        return cards.openAll();
    }

    public boolean isBust() {
        return getMaxValueNearBlackJack() > BLACK_JACK;
    }

    public boolean isBlackJack() {
        return getMaxValueNearBlackJack() == BLACK_JACK;
    }

    private Cards createDeckCards(final Deck deck) {
        final List<Card> initCards = new ArrayList<>();
        initCards.add(deck.draw());
        initCards.add(deck.draw());
        validate(initCards);
        return new Cards(initCards);
    }

    private int getAceCount(final List<CardNumber> cardNumbers) {
        return (int) cardNumbers.stream()
                .filter(number -> number == ACE)
                .count();
    }

    private int getSumBeforeOptimize(final List<CardNumber> cardNumbers) {
        return cardNumbers.stream()
                .mapToInt(CardNumber::getValue)
                .sum();
    }

    private int getSumAfterOptimize(final int aceCount, final int sumBeforeOptimize) {
        return IntStream.range(0, aceCount)
                .reduce(sumBeforeOptimize, (before, after) -> optimizeMaxValue(before));
    }

    private int optimizeMaxValue(final int before) {
        if (before + ACE_CONVERT_NUMBER <= BLACK_JACK) {
            return before + ACE_CONVERT_NUMBER;
        }
        return before;
    }

    private void validate(final List<Card> cards) {
        if (cards.size() != INITIAL_SIZE) {
            throw new IllegalArgumentException("첫 카드는 두 장이어야 합니다.");
        }

        if (new HashSet<>(cards).size() != INITIAL_SIZE) {
            throw new IllegalArgumentException("카드는 중복될 수 없습니다.");
        }
    }
}
