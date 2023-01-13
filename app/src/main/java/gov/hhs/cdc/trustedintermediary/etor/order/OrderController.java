package gov.hhs.cdc.trustedintermediary.etor.order;

import ca.uhn.fhir.context.FhirContext;
import gov.hhs.cdc.trustedintermediary.domainconnector.DomainRequest;
import gov.hhs.cdc.trustedintermediary.domainconnector.DomainResponse;
import gov.hhs.cdc.trustedintermediary.wrappers.Formatter;
import gov.hhs.cdc.trustedintermediary.wrappers.FormatterProcessingException;
import gov.hhs.cdc.trustedintermediary.wrappers.Logger;
import java.util.Map;
import javax.inject.Inject;
import org.hl7.fhir.instance.model.api.IBase;

/**
 * Creates an in-memory representation of an order to be ingested by the system, and return response
 * information back to the client.
 */
public class OrderController {

    private static final OrderController ORDER_CONTROLLER = new OrderController();

    @Inject Formatter formatter;
    @Inject Logger logger;

    static final String CONTENT_TYPE_LITERAL = "Content-Type";
    static final String APPLICATION_JSON_LITERAL = "application/json";

    private OrderController() {}

    public static OrderController getInstance() {
        return ORDER_CONTROLLER;
    }

    public Order parseOrder(DomainRequest request) {
        logger.logInfo("Parsing order");
        Order order = new Order();

        //        try {
        //            order = formatter.convertToObject(request.getBody(), Order.class);
        //        } catch (FormatterProcessingException e) {
        //            logger.logError("Unable to convert request body to order object");
        //            throw new RuntimeException(e);
        //        }

        FhirContext context = FhirContext.forR4();
        var pather = context.newFhirPath();
        var parser = context.newJsonParser();
        var terser = context.newTerser();
        var patient = parser.parseResource(request.getBody());

        var answer = pather.evaluateFirst(patient, "/dogcow", IBase.class);
        logger.logInfo("answer=" + answer);

        return order;
    }

    public DomainResponse constructResponse(OrderMessage orderMessage) {
        logger.logInfo("Constructing the response");
        var response = new DomainResponse(200);

        try {
            var responseBody = formatter.convertToString(orderMessage);
            response.setBody(responseBody);
        } catch (FormatterProcessingException e) {
            logger.logError("Error constructing order message", e);
            throw new RuntimeException(e);
        }

        response.setHeaders(Map.of(CONTENT_TYPE_LITERAL, APPLICATION_JSON_LITERAL));

        return response;
    }
}
