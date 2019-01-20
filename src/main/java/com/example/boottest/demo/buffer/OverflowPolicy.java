package com.example.boottest.demo.buffer;


/**
 * @author Guan
 */
public interface OverflowPolicy<T> {
    /**
     * @param object
     * @param bufferData
     */
    void addToFullBuffer(T object, RingBuffer<T> bufferData);
}