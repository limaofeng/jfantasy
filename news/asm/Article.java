package org.jfantasy.framework.util.asm;


import org.jfantasy.framework.lucene.annotations.Indexed;

@Indexed
public class Article {

    private Long id;

    private String testOp;

    private String user;

    private boolean tflag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTestOp() {
        return testOp;
    }

    public void setTestOp(String testOp) {
        this.testOp = testOp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isTflag() {
        return tflag;
    }

    public void setTflag(boolean tflag) {
        this.tflag = tflag;
    }
}
