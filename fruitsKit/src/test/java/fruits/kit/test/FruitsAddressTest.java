package fruits.kit.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import fruits.kit.crypto.FruitsCrypto;
import fruits.kit.entity.FruitsAddress;
import fruits.kit.util.FruitsKitUtils;

import static org.junit.Assert.assertEquals;

import org.bouncycastle.util.encoders.Hex;

@RunWith(JUnit4.class)
public class FruitsAddressTest {
    @Test
    public void testFruitsAddressFromEither() {
        FruitsKitUtils.setAddressPrefix("FRUITS");
        assertEquals(7009665667967103287L, FruitsAddress.fromEither("FRUITS-WEBR-T74Q-V2DH-8PUK-446E-TRSH").getSignedLongId());
        assertEquals("FRUITS-WEBR-T74Q-V2DH-8PUK-446E-TRSH", FruitsAddress.fromEither("7009665667967103287").getFullAddress());
        assertEquals("FRUITS-WEBR-T74Q-V2DH-8PUK-446E-TRSH", FruitsAddress.fromEither("FRUITS-WEBR-T74Q-V2DH-8PUK-446E-TRSH").getFullAddress());
        
        assertEquals("FRUITS-JJQS-MMA4-XSQQ-4ZNZ-UB6U-3TCX", FruitsAddress.fromEither("FRUITS-JJQS-MMA4-XSQQ-4ZNZ-UB6U-3TCX").getFullAddress());
        assertEquals("FRUITS-JJQS-MMA4-XSQQ-4ZNZ-UB6U-3TCX", FruitsAddress.fromEither("FRUITS-JJQS-MMA4-XSQQ-4ZNZ-UB6U-3TCX-1UQDBIWHL54TH1HBIK1ZEQWQ3DR4MW6E5B3SNDK1HM9TMLL1T2").getFullAddress());
        assertEquals("4A5FA0EE4E40BA8D322EF75963D6B8E71BD206897326CC9683960C9D2FF25966",
                FruitsCrypto.getInstance().toHexString(FruitsAddress.fromEither("FRUITS-JJQS-MMA4-XSQQ-4ZNZ-UB6U-3TCX-1UQDBIWHL54TH1HBIK1ZEQWQ3DR4MW6E5B3SNDK1HM9TMLL1T2").getPublicKey()).toUpperCase());

        assertEquals("FRUITS-NMXS-ZZS2-X9V6-9UKJ-PN5C-5LUL", FruitsAddress.fromEither("FRUITS-NMXS-ZZS2-X9V6-9UKJ-PN5C-5LUL").getFullAddress());
        
        assertEquals("CB85806964BB888E9AE1C2F27A3DB85D448B84FE9CB2BCCEAFBC49729A03AC16",
                FruitsCrypto.getInstance().toHexString(FruitsAddress.fromEither("FRUITS-NMXS-ZZS2-X9V6-9UKJ-PN5C-5LUL-52M12IVV2RO5B03A568CVA5AO59D0Z1IF8ILFD11CS8M7N3AQE").getPublicKey()).toUpperCase());
        
        assertEquals("FRUITS-JJQS-MMA4-XSQQ-4ZNZ-UB6U-3TCX", FruitsCrypto.getInstance().getFruitsAddressFromPublic(Hex.decode("4A5FA0EE4E40BA8D322EF75963D6B8E71BD206897326CC9683960C9D2FF25966")).getFullAddress());
        assertEquals("FRUITS-JJQS-MMA4-XSQQ-4ZNZ-UB6U-3TCX-1UQDBIWHL54TH1HBIK1ZEQWQ3DR4MW6E5B3SNDK1HM9TMLL1T2", FruitsCrypto.getInstance().getFruitsAddressFromPublic(Hex.decode("4A5FA0EE4E40BA8D322EF75963D6B8E71BD206897326CC9683960C9D2FF25966")).getExtendedAddress());

        assertEquals("FRUITS-NMXS-ZZS2-X9V6-9UKJ-PN5C-5LUL", FruitsCrypto.getInstance().getFruitsAddressFromPublic(Hex.decode("CB85806964BB888E9AE1C2F27A3DB85D448B84FE9CB2BCCEAFBC49729A03AC16")).getFullAddress());
        assertEquals("FRUITS-NMXS-ZZS2-X9V6-9UKJ-PN5C-5LUL-52M12IVV2RO5B03A568CVA5AO59D0Z1IF8ILFD11CS8M7N3AQE", FruitsCrypto.getInstance().getFruitsAddressFromPublic(Hex.decode("CB85806964BB888E9AE1C2F27A3DB85D448B84FE9CB2BCCEAFBC49729A03AC16")).getExtendedAddress());

        FruitsKitUtils.setAddressPrefix("BAT");
        assertEquals(7009665667967103287L, FruitsAddress.fromEither("BAT-WEBR-T74Q-V2DH-8PUK-446E-TRSH").getSignedLongId());
        assertEquals("BAT-WEBR-T74Q-V2DH-8PUK-446E-TRSH", FruitsAddress.fromEither("7009665667967103287").getFullAddress());
        assertEquals("BAT-WEBR-T74Q-V2DH-8PUK-446E-TRSH", FruitsAddress.fromEither("BAT-WEBR-T74Q-V2DH-8PUK-446E-TRSH").getFullAddress());
    }
}
