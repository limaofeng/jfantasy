package org.jfantasy.pay.event;

import org.hibernate.Hibernate;
import org.jfantasy.pay.bean.Card;
import org.springframework.context.ApplicationEvent;

public class CardBindEvent extends ApplicationEvent {

    public CardBindEvent(Card source) {
        super(source);
        Hibernate.initialize(source.getDesign());
    }

    public Card getCard() {
        return (Card) this.getSource();
    }

}
