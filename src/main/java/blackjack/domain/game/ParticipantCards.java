package blackjack.domain.game;

import blackjack.domain.card.Card;
import blackjack.domain.card.CardNumber;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParticipantCards {
    private static final int INITIAL_SIZE = 2;
    private static final int BLACK_JACK = 21;

    private final List<Card> cards;

    public ParticipantCards(final List<Card> cards) {
        validate(cards);
        this.cards = new ArrayList<>(cards);
    }

    public void receive(final Card card) {
        if (cards.contains(card)) {
            throw new IllegalArgumentException("중복되는 카드를 가질 수 없습니다.");
        }
        cards.add(card);
    }

    public int calculate() {
        List<CardNumber> cardNumbers = cards.stream()
                .map(Card::getNumber)
                .collect(Collectors.toList());
        return CardNumber.getMaxValueNearBlackJack(cardNumbers, BLACK_JACK);
    }

    public List<Card> open(int size) {
        return IntStream.range(0, size)
                .mapToObj(cards::get)
                .collect(Collectors.toList());
    }

    public List<Card> openAll() {
        return cards;
    }

    public boolean isBust() {
        return calculate() > BLACK_JACK;
    }

    public boolean isBlackJack() {
        return calculate() == BLACK_JACK;
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
