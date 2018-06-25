package com.disware.spider.core;

import com.disware.spider.listener.Reticular;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author 4everlynn
 * Create at 2018/6/24
 */
public class ReticularSpiders<T> {
    private ExecutorService executor;
    private List<Future<T>> futures = new ArrayList<>();

    public ReticularSpiders(int pool) {
        executor = Executors.newFixedThreadPool(pool);
    }

    public void put(Reticular<T> reticular, String... args) {
        futures.add(executor.submit(() -> reticular.start(args)));
    }

    public List<T> results() {
        List<T> list = new ArrayList<>();
        for (Future<T> future : futures) {
            try {
                list.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        return list;
    }


}
