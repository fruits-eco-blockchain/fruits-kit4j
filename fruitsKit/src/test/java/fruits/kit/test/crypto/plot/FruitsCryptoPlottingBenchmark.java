package fruits.kit.test.crypto.plot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import fruits.kit.crypto.FruitsCrypto;
import fruits.kit.crypto.plot.impl.MiningPlot;
import fruits.kit.util.LibShabal;

@RunWith(JUnit4.class)
public class FruitsCryptoPlottingBenchmark {
    private final FruitsCrypto fruitsCrypto = FruitsCrypto.getInstance();

    private void runBenchmark(int numberOfRepetitions) {
        byte[] buffer = new byte[numberOfRepetitions * MiningPlot.PLOT_SIZE];
        long start = System.currentTimeMillis();
        fruitsCrypto.plotNonces(123, 321, numberOfRepetitions, (byte) 2, buffer, 0);
        long duration = System.currentTimeMillis() - start;
        System.out.println("Time to plot " + numberOfRepetitions + " nonces: " + duration + "ms");
    }

    @Test
    public void benchmarkBulkPlot() {
        fruitsCrypto.setNativeEnabled(false);
        runBenchmark(64);
    }

    @Test
    public void benchmarkBulkPlot_native() {
        fruitsCrypto.setNativeEnabled(true);
        if (LibShabal.LOAD_ERROR != null) {
            System.out.println("LibShabal not loaded, can't benchmark native implementation");
            LibShabal.LOAD_ERROR.printStackTrace();
            return;
        }
        runBenchmark(64);
    }
}
