package com.fantasy.framework.io;

public interface Buffers {
	  public Buffer getHeader();

	  public Buffer getBuffer();

	  public Buffer getBuffer(int paramInt);

	  public void returnBuffer(Buffer paramBuffer);
}
