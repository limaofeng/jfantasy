package org.jfantasy.pay.validators;

import org.jfantasy.framework.spring.validation.ValidationException;
import org.jfantasy.framework.spring.validation.Validator;
import org.jfantasy.pay.service.CardTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CardTypeKeyCannotRepeatValidator implements Validator<String> {

    private final CardTypeService cardTypeService;

    @Autowired
    public CardTypeKeyCannotRepeatValidator(CardTypeService cardTypeService) {
        this.cardTypeService = cardTypeService;
    }

    @Override
    public void validate(String value) throws ValidationException {
        if (cardTypeService.get(value) != null) {
            throw new ValidationException("KEY["+value+"]已经存在");
        }
    }

}
