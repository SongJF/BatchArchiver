package com.laplace.dove.batcharchiver.utils;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class SnackBarQueue {
    private static final Semaphore lock = new Semaphore(1);
    /** if a snake bar is committed 10s  before, it is considered useless */
    private static int TIME_OUT = 10;

    /**
     * append to SnackBar Queue
     *
     * @param snakeBar snakeBar
     * @param <T> generic type
     */
    public synchronized static <T extends Snackbar> void append(T snakeBar) {
        append(snakeBar, TIME_OUT);
    }

    /**
     * append to SnackBar Queue with timeout
     *
     * @param snakeBar snakeBar
     * @param timeout seconds snackbar would wait; if -1, it would keep waiting until it can
     * @param <T> generic type
     */
    public synchronized static <T extends Snackbar> void append(T snakeBar, int timeout) {
        new Thread(() -> {
            if (timeout == -1){
                try {
                    lock.acquire();
                    snakeBar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);
                            lock.release();
                        }
                    });
                    snakeBar.show();
                } catch (InterruptedException e) {
                    lock.release();
                    e.printStackTrace();
                }
            } else {
                try {
                    boolean getLock = lock.tryAcquire(timeout, TimeUnit.SECONDS);
                    if (getLock){
                        snakeBar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                                lock.release();
                            }
                        });
                        snakeBar.show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.release();
                }
            }
        }).start();
    }
}
