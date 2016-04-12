package com.github.brainlag.nsq.callbacks;

import com.github.brainlag.nsq.exceptions.NSQException;

public interface NSQErrorCallback {
    void error(NSQException x);
}
