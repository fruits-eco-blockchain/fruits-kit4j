package fruits.kit.test.crypto.plot;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import fruits.kit.crypto.FruitsCrypto;
import fruits.kit.crypto.plot.PlotCalculator;
import fruits.kit.crypto.plot.impl.PlotCalculatorNativeImpl;

@RunWith(JUnit4.class)
public class PlotCalculatorNativeImplTest extends PlotCalculatorTest {
    @Override
    protected PlotCalculator getPlotCalculator() {
        return new PlotCalculatorNativeImpl(() -> FruitsCrypto.getInstance().getShabal256());
    }
}
