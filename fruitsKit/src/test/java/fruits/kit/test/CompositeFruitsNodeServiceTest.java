package fruits.kit.test;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import fruits.kit.service.FruitsNodeService;
import fruits.kit.service.impl.CompositeFruitsNodeService;
import fruits.kit.service.impl.HttpFruitsNodeService;

@RunWith(JUnit4.class)
@Ignore // TODO
public class CompositeFruitsNodeServiceTest extends FruitsNodeServiceTest {
    @Override
    protected FruitsNodeService getFruitsNodeService() {
        FruitsNodeService http = new HttpFruitsNodeService(TestVariables.HTTP_API_ENDPOINT, "fruitskit4j-TEST");
        return new CompositeFruitsNodeService(http);
    }
}
