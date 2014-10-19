package com.fantasy.framework;

import com.fantasy.framework.net.util.CoderUtil;

import java.nio.ByteBuffer;
import java.util.UUID;

public class CoderUtilsTest {

	/**
	 * @功能描述 
	 * @param args
	 */
	public static void main(String[] args) {

        System.out.println(UUID.randomUUID().toString().toUpperCase());

		byte[] Maskingkey = new byte[]{(byte)33, (byte)-65,(byte) -62,(byte) -108};
		byte data = (byte)16;
		System.out.println(data);
		System.out.println(data ^ Maskingkey[0]);
		ByteBuffer byteBuffer = ByteBuffer.allocate(1);
		byteBuffer.put((byte) (data ^ Maskingkey[0]));
		byteBuffer.flip();
		System.out.println(CoderUtil.decode(byteBuffer));
		System.out.println("---------------------------");
		System.out.println(data ^ Maskingkey[1]);
		byteBuffer = ByteBuffer.allocate(1);
		byteBuffer.put((byte) (data ^ Maskingkey[1]));
		byteBuffer.flip();
		System.out.println(CoderUtil.decode(byteBuffer));
		System.out.println("---------------------------");
		System.out.println(data ^ Maskingkey[2]);
		byteBuffer = ByteBuffer.allocate(1);
		byteBuffer.put((byte) (data ^ Maskingkey[2]));
		byteBuffer.flip();
		System.out.println(CoderUtil.decode(byteBuffer));
		System.out.println("---------------------------");
		System.out.println(data ^ Maskingkey[3]);
		byteBuffer = ByteBuffer.allocate(1);
		byteBuffer.put((byte) (data ^ Maskingkey[3]));
		byteBuffer.flip();
		System.out.println(CoderUtil.decode(byteBuffer));
		System.out.println("---------------------------");
	}

}
