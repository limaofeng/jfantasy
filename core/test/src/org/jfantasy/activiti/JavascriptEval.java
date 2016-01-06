package org.jfantasy.activiti;


import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.StringReader;

public class JavascriptEval {

    @Test
    public void runJavascript() throws ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        engine.eval(new StringReader("function addFun(a, b){\n" +
                " return a+b;\n" +
                "}"));
        Invocable invoke = (Invocable) engine;
        Double sum = (Double) invoke.invokeFunction("addFun", 2, 3);
        System.out.println(sum);
    }


}
