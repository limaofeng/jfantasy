package com.fantasy.framework.tags.ui;

import com.fantasy.framework.tags.AbstractUITag;
import com.fantasy.framework.tags.model.Form;

public class FormTag extends AbstractUITag<Form> {
    private static final long serialVersionUID = -8416799697944954794L;

    public void setAction(String action) {
	((Form) getUIBean()).setAction(action);
    }

    public void setTarget(String target) {
	((Form) getUIBean()).setTarget(target);
    }

    public void setMethod(String method) {
	((Form) getUIBean()).setMethod(method);
    }
}