package gov.hhs.cdc.trustedintermediary

import com.openpojo.reflection.impl.PojoClassFactory
import com.openpojo.validation.Validator
import com.openpojo.validation.ValidatorBuilder
import com.openpojo.validation.test.impl.GetterTester
import com.openpojo.validation.test.impl.SetterTester

class PojoTestUtils {

    private static final Validator VALIDATOR = ValidatorBuilder.create()
    .with(new GetterTester())
    .with(new SetterTester())
    .build()

    static def validateGettersAndSetters(final Class<?> clazz) {
        VALIDATOR.validate(PojoClassFactory.getPojoClass(clazz))
    }
}
