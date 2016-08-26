package org.jfantasy.member.validators;

import org.jfantasy.framework.spring.validation.ValidationException;
import org.jfantasy.framework.spring.validation.Validator;
import org.jfantasy.member.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeamIdCannotRepeatValidator implements Validator<String> {

    private final TeamService teamService;

    @Autowired
    public TeamIdCannotRepeatValidator(TeamService teamService) {
        this.teamService = teamService;
    }

    @Override
    public void validate(String value) throws ValidationException {
        if (teamService.get(value) != null) {
            throw new ValidationException("编码[" + value + "]已经存在");
        }
    }

}
