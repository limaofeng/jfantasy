package org.jfantasy.framework.lucene.cluster;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

public class BufferUtil {

    public static ByteBuffer toBuffer(Serializable obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.flush();
        ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
        oos.close();
        baos.close();
        return buffer;
    }

    public static Object fromBuffer(ByteBuffer buffer) throws IOException, ClassNotFoundException {
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes, 0, bytes.length);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object obj = ois.readObject();
        ois.close();
        bais.close();
        return obj;
    }

}
