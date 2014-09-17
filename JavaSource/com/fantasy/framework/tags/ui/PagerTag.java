package com.fantasy.framework.tags.ui;

import com.fantasy.framework.tags.AbstractUITag;

public class PagerTag extends AbstractUITag<com.fantasy.framework.tags.model.Pager> {
    private static final long serialVersionUID = -7959554412236956819L;

    public void setPage(com.fantasy.framework.dao.Pager<?> page) {
	((com.fantasy.framework.tags.model.Pager) getUIBean()).setPage(page);
    }

    public void setFormId(String formId) {
	((com.fantasy.framework.tags.model.Pager) getUIBean()).setFormId(formId);
    }
}