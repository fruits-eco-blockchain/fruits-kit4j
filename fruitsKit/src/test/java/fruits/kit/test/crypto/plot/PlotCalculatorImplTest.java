package fruits.kit.test.crypto.plot;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import fruits.kit.crypto.FruitsCrypto;
import fruits.kit.crypto.plot.PlotCalculator;
import fruits.kit.crypto.plot.impl.PlotCalculatorImpl;

@RunWith(JUnit4.class)
public class PlotCalculatorImplTest extends PlotCalculatorTest {
    @Override
    protected PlotCalculator getPlotCalculator() {
        return new PlotCalculatorImpl(() -> FruitsCrypto.getInstance().getShabal256());
    }
}
