package org.jfantasy.framework.util.asm;

import org.objectweb.asm.MethodVisitor;

public interface MethodCreator {

    void execute(MethodVisitor mv);

}
