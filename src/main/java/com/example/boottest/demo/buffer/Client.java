package com.example.boottest.demo.buffer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Guan
 * @date Created on 2019/1/15
 */
public class Client {

    public static void main(String[] args) {

//        DelimiterBasedFrameDecoder
        byte[] bytes1 = {0x01, 0x02, 0x30, 0x31, 0x32, 0x33, 0x34, 0x21, 0x22, 0x23, 0x24, 0x31, 0x32, 0x33, 0x34};
        byte[] bytes2 = {0x31, 0x32, 0x36};
        byte[] bytes3 = {0x21, 0x22, 0x23};
        System.out.println(indexOf(bytes1, bytes2));
        System.out.println(indexOf(bytes1, bytes3));




    }

    private void testQueue() {
        ArrayBlockingQueue queue;
    }

    private void testRingBuffer() {
        final RingBuffer<Byte> buffer = new RingBuffer<>(1024, new OverflowPolicy<Byte>() {
            @Override
            public void addToFullBuffer(Byte object, RingBuffer<Byte> bufferData) {

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    buffer.add((byte) 0x30);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    buffer.add((byte) 0x31);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Byte b;

                while (true) {
                    if ((b = buffer.remove()) != null) {
                        System.out.println(Thread.currentThread().getName() + "---" + (b));
                    }
                }
            }
        }).start();
    }


    /**
     * Returns the number of bytes between the readerIndex of the haystack and
     * the first needle found in the haystack.  -1 is returned if no needle is
     * found in the haystack.
     */
    private static int indexOf(byte[] haystack, byte[] needle) {
        for (int i = 0; i < haystack.length; i++) {
            int haystackIndex = i;
            int needleIndex;
            for (needleIndex = 0; needleIndex < needle.length; needleIndex++) {
                if (haystack[haystackIndex] != needle[needleIndex]) {
                    break;
                } else {
                    haystackIndex++;
                    if (haystackIndex == haystack.length &&
                            needleIndex != needle.length - 1) {
                        return -1;
                    }
                }
            }

            if (needleIndex == needle.length) {
                // Found the needle from the haystack!
                return i;
            }
        }
        return -1;
    }

}
