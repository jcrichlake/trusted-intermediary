package gov.hhs.cdc.trustedintermediary.external.hapi;

import java.util.List;
import java.util.stream.Stream;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.MessageHeader;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;

/** Helper class that works on HapiFHIR constructs. */
public class HapiHelper {

    private HapiHelper() {}

    public static final Coding OML_CODING =
            new Coding(
                    "http://terminology.hl7.org/CodeSystem/v2-0003",
                    "O21",
                    "OML - Laboratory order");

    /**
     * Returns a {@link Stream} of FHIR resources inside the provided {@link Bundle} that match the
     * given resource type.
     *
     * @param bundle The bundle to search.
     * @param resourceType The class of the resource to search for.
     * @param <T> The type that either is or extends {@link Resource}.
     * @return The stream of the given resource type.
     */
    public static <T extends Resource> Stream<T> resourcesInBundle(
            Bundle bundle, Class<T> resourceType) {
        return bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getClass().equals(resourceType))
                .map(resource -> ((T) resource));
    }

    public static <T extends Resource> Resource resourceInBundle(
            Bundle bundle, Class<T> resourceType) {
        return resourcesInBundle(bundle, resourceType).findFirst().orElse(null);
    }

    public static void addMetaTag(
            Bundle messageBundle, String system, String code, String display) {
        MessageHeader messageHeader = getOrCreateMessageHeader(messageBundle);
        var meta = messageHeader.hasMeta() ? messageHeader.getMeta() : new Meta();

        if (meta.getTag(system, code) == null) {
            meta.addTag(new Coding(system, code, display));
        }

        messageHeader.setMeta(meta);
    }

    // MSH - Message Header
    public static MessageHeader getMessageHeader(Bundle bundle) {
        return (MessageHeader) resourceInBundle(bundle, MessageHeader.class);
    }

    public static MessageHeader getOrCreateMessageHeader(Bundle bundle) {
        MessageHeader messageHeader = getMessageHeader(bundle);
        if (messageHeader == null) {
            messageHeader = new MessageHeader();
            bundle.addEntry(new Bundle.BundleEntryComponent().setResource(messageHeader));
        }
        return messageHeader;
    }

    // MSH.9 - Message Type
    public static Coding getMessageTypeCoding(Bundle bundle) {
        MessageHeader messageHeader = getMessageHeader(bundle);
        return messageHeader.getEventCoding();
    }

    // MSH.9 - Message Type
    public static void setMessageTypeCoding(Bundle order, Coding coding) {
        var messageHeader = getOrCreateMessageHeader(order);
        messageHeader.setEvent(coding);
    }

    // MSH.3 - Sending Application
    public static MessageHeader.MessageSourceComponent getSendingApplication(Bundle bundle) {
        MessageHeader messageHeader = getMessageHeader(bundle);
        return messageHeader.getSource();
    }

    // MSH.4 - Sending Facility
    public static Organization getSendingFacility(Bundle bundle) {
        MessageHeader messageHeader = getMessageHeader(bundle);
        return (Organization) messageHeader.getSender().getResource();
    }

    // MSH.5 - Receiving Application
    public static MessageHeader.MessageDestinationComponent getReceivingApplication(Bundle bundle) {
        MessageHeader messageHeader = getMessageHeader(bundle);
        return messageHeader.getDestinationFirstRep();
    }

    // MSH.6 - Receiving Facility
    public static Organization getReceivingFacility(Bundle bundle) {
        MessageHeader messageHeader = getMessageHeader(bundle);
        return (Organization) messageHeader.getDestinationFirstRep().getReceiver().getResource();
    }

    // PID.3 - Patient Identifier List
    public static List<Identifier> getPatientIdentifierList(Bundle bundle) {
        Patient patient = (Patient) resourceInBundle(bundle, Patient.class);
        return patient.getIdentifier();
    }

    // HD.1 - Namespace Id
    public static Identifier createHDNamespaceIdentifier() {
        Identifier identifier = new Identifier();
        Extension extension =
                new Extension(
                        "https://reportstream.cdc.gov/fhir/StructureDefinition/hl7v2Field",
                        new StringType("HD.1"));
        identifier.addExtension(extension);
        return identifier;
    }
}
