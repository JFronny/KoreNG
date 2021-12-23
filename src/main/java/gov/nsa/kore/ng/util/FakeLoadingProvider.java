package gov.nsa.kore.ng.util;

import java.util.Random;
import java.util.function.Consumer;

public class FakeLoadingProvider {
    private static final Random RND = new Random();
    public static void provideFakeLoad(boolean fast, Consumer<Double> progress) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            if (RND.nextDouble() < 0.02)
                Thread.sleep(500);
            Thread.sleep(RND.nextInt(fast ? 10 : 20, fast ? 30 : 100));
            progress.accept(i * 0.01);
        }
    }
}
