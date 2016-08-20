package org.jfantasy.pay.validators;

import org.jfantasy.framework.spring.validation.ValidationException;
import org.jfantasy.framework.spring.validation.Validator;
import org.jfantasy.pay.service.CardBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CardBatchNoCannotRepeatValidator implements Validator<String> {

    private final CardBatchService cardBatchService;

    @Autowired
    public CardBatchNoCannotRepeatValidator(CardBatchService cardBatchService) {
        this.cardBatchService = cardBatchService;
    }

    @Override
    public void validate(String value) throws ValidationException {
        if (cardBatchService.findByNo(value) != null) {
            throw new ValidationException("No["+value+"]已经存在");
        }
    }

}
