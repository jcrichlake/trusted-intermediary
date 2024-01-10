package gov.hhs.cdc.trustedintermediary.etor.orders

import gov.hhs.cdc.trustedintermediary.OrderMock
import gov.hhs.cdc.trustedintermediary.context.TestApplicationContext
import gov.hhs.cdc.trustedintermediary.etor.metadata.EtorMetadataStep
import gov.hhs.cdc.trustedintermediary.etor.metadata.PartnerMetadataException
import gov.hhs.cdc.trustedintermediary.etor.metadata.PartnerMetadataOrchestrator
import gov.hhs.cdc.trustedintermediary.wrappers.Logger
import gov.hhs.cdc.trustedintermediary.wrappers.MetricMetadata
import spock.lang.Specification

class SendOrderUsecaseTest extends Specification {

    def mockOrchestrator = Mock(PartnerMetadataOrchestrator)
    def mockConverter = Mock(OrderConverter)
    def mockSender = Mock(OrderSender)
    def mockLogger = Mock(Logger)

    def setup() {
        TestApplicationContext.reset()
        TestApplicationContext.init()
        TestApplicationContext.register(SendOrderUseCase, SendOrderUseCase.getInstance())
        TestApplicationContext.register(MetricMetadata, Mock(MetricMetadata))
        TestApplicationContext.register(PartnerMetadataOrchestrator, mockOrchestrator)
        TestApplicationContext.register(OrderConverter, mockConverter)
        TestApplicationContext.register(OrderSender, mockSender)
        TestApplicationContext.register(Logger, mockLogger)
    }

    def "send sends successfully"() {
        given:
        def receivedSubmissionId = "receivedId"
        def sentSubmissionId = "sentId"

        def sendOrder = SendOrderUseCase.getInstance()
        def mockOrder = new OrderMock(null, null, null)
        def mockOmlOrder = Mock(Order)

        TestApplicationContext.injectRegisteredImplementations()

        when:
        sendOrder.convertAndSend(mockOrder, receivedSubmissionId)

        then:
        1 * mockConverter.convertMetadataToOmlOrder(mockOrder) >> mockOmlOrder
        1 * mockConverter.addContactSectionToPatientResource(mockOmlOrder) >> mockOmlOrder
        1 * mockSender.sendOrder(mockOmlOrder) >> Optional.of(sentSubmissionId)
        1 * sendOrder.metadata.put(_, EtorMetadataStep.ORDER_CONVERTED_TO_OML)
        1 * sendOrder.metadata.put(_, EtorMetadataStep.CONTACT_SECTION_ADDED_TO_PATIENT)
        1 * mockOrchestrator.updateMetadataForReceivedOrder(receivedSubmissionId, _ as String)
        1 * mockOrchestrator.updateMetadataForSentOrder(receivedSubmissionId, sentSubmissionId)
    }

    def "send fails to send"() {
        given:
        mockSender.sendOrder(_) >> { throw new UnableToSendOrderException("DogCow", new NullPointerException()) }
        TestApplicationContext.injectRegisteredImplementations()

        when:
        SendOrderUseCase.getInstance().convertAndSend(Mock(Order), _ as String)

        then:
        thrown(UnableToSendOrderException)
    }

    def "convertAndSend should log warnings for null receivedSubmissionId"() {
        given:
        mockSender.sendOrder(_) >> Optional.of("sentSubmissionId")
        TestApplicationContext.injectRegisteredImplementations()

        when:
        SendOrderUseCase.getInstance().convertAndSend(Mock(Order), null)

        then:
        2 * mockLogger.logWarning(_)
        0 * mockOrchestrator.updateMetadataForReceivedOrder(_, _)
    }

    def "convertAndSend logs error and continues when updateMetadataForReceivedOrder throws exception"() {
        given:
        def order = Mock(Order)
        def omlOrder = Mock(Order)
        def receivedSubmissionId = "receivedId"
        mockOrchestrator.updateMetadataForReceivedOrder(receivedSubmissionId, _ as String) >> { throw new PartnerMetadataException("Error") }
        TestApplicationContext.injectRegisteredImplementations()

        when:
        SendOrderUseCase.getInstance().convertAndSend(order, receivedSubmissionId)

        then:
        1 * mockLogger.logError(_, _)
        1 * mockConverter.convertMetadataToOmlOrder(order) >> omlOrder
        1 * mockConverter.addContactSectionToPatientResource(omlOrder) >> omlOrder
        1 * mockSender.sendOrder(omlOrder) >> Optional.of("sentId")
    }

    def "convertAndSend logs error and continues when updating the metadata for the sent order throws exception"() {
        given:
        def order = Mock(Order)
        def omlOrder = Mock(Order)
        def partnerMetadataException = new PartnerMetadataException("Error")
        mockOrchestrator.updateMetadataForSentOrder("receivedId", _) >> { throw  partnerMetadataException}
        TestApplicationContext.injectRegisteredImplementations()

        when:
        SendOrderUseCase.getInstance().convertAndSend(order, "receivedId")

        then:
        1 * mockConverter.convertMetadataToOmlOrder(order) >> omlOrder
        1 * mockConverter.addContactSectionToPatientResource(omlOrder) >> omlOrder
        1 * mockSender.sendOrder(omlOrder) >> Optional.of("sentId")
        1 * mockLogger.logError(_, partnerMetadataException)
    }
}
