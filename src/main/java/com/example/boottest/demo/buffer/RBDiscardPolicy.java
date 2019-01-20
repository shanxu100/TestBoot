package com.example.boottest.demo.buffer;


/**
 * This implements an overflow policy for the RingBuffer
 * which simply discards the tail of the buffer and adds the new
 * object.
 *
 * @author Guan
 * @date Created on 2019/1/15
 */
public class RBDiscardPolicy<T> implements OverflowPolicy<T> {
    @Override
    public void addToFullBuffer(T object, RingBuffer<T> bufferData) {
        bufferData.remove();
        bufferData.add(object);
    }
}
