package com.github.brainlag.nsq.callbacks;

import com.github.brainlag.nsq.NSQMessage;

public interface NSQMessageCallback {
	public void message(NSQMessage message);
}
