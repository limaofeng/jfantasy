package com.fantasy.framework.tags.ui;

import com.fantasy.framework.tags.AbstractUITag;
import com.fantasy.framework.tags.model.Button;

public class ButtonTag extends AbstractUITag<Button> {
    private static final long serialVersionUID = 9027980275440323067L;

    public String getUrl() {
	return ((Button) getUIBean()).getUrl();
    }

    public void setUrl(String url) {
	((Button) getUIBean()).setUrl(url);
    }
}