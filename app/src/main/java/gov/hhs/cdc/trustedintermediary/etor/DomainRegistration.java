package gov.hhs.cdc.trustedintermediary.etor;

import gov.hhs.cdc.trustedintermediary.domainconnector.DomainConnector;
import gov.hhs.cdc.trustedintermediary.domainconnector.DomainRequest;
import gov.hhs.cdc.trustedintermediary.domainconnector.DomainResponse;
import gov.hhs.cdc.trustedintermediary.domainconnector.HttpVerbPath;
import java.util.Map;
import java.util.function.Function;

public class DomainRegistration implements DomainConnector {

    @Override
    public Map<HttpVerbPath, Function<DomainRequest, DomainResponse>> domainRegistration() {
        return Map.of(new HttpVerbPath("POST", "/v1/etor/order"), this::handleOrder);
    }

    private DomainResponse handleOrder(DomainRequest request) {
        var response = new DomainResponse(200);

        response.setBody("DogCow requsted a lab order");

        return response;
    }
}
