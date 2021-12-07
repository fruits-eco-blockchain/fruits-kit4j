package fruits.kit.test;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import fruits.kit.service.FruitsNodeService;
import fruits.kit.service.impl.HttpFruitsNodeService;

@RunWith(JUnit4.class)
@Ignore // until we have a public node with 3.1.1
public class HttpFruitsNodeServiceTest extends FruitsNodeServiceTest {
    @Override
    protected FruitsNodeService getFruitsNodeService() {
        return new HttpFruitsNodeService(TestVariables.HTTP_API_ENDPOINT, "fruitskit4j-TEST");
    }
}
