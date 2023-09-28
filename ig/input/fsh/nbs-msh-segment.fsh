Logical: MSHSegment
Id: segment-msh-logical-model
Title: "MSH Segment"
Description: "The MSH Segment"
* segmentType 1..1 string "MSH"
* fieldSeparator 1..1 string "MSH-1 The field separator for the message"
* encodingCharacters 1..1 string "MSH-2 Encoding Characters"
* sendingApplication 1..1 code "MSH-3 The sending application"
* sendingApplicationNamespaceId 1..1 code "MSH-3.1 Sending Application Namespace ID"
* sendingApplicationUniversalId 1..1 string "MSH-3.2 Sending Application Universal ID"
* sendingApplicationUniversalIdType 1..1 id "MSH-3.3 Sending Application Universal ID Type"
* sendingFacility 1..1 code "MSH-4 The sending facility"
* sendingFacilityNamespaceId 1..1 code "MSH-4.1 The sending facility namespace ID"
* sendingFacilityUniversalId 1..1 string "MSH-4.2 The sending facility universal ID"
* sendingFacilityUniversalIdType 1..1 id "MSH-4.3 The sending facility universal ID type"
* receivingApplication 1..1 code "MSH-5 The receiving application"
* receivingApplicationNamespaceId 1..1 code "MSH-5.1 The receiving application namespace ID"
* receivingApplicationUniversalId 1..1 string "MSH-5.2 The receiving application universal ID type"
* receivingApplicationUniversalIdType 1..1 id "MSH-5.3 The receiving application universal ID type"
* receivingFacility 1..1 code "MSH-6 The receiving facility"
* receivingFacilityNamespaceId 1..1 code "MSH-6.1 Receiving Facility Namespace ID"
* receivingFacilityUniversalId 1..1 string "MSH-6.2 Receiving Facility Universal ID"
* receivingFacilityUniversalIdType 1..1 id "MSH-6.3 Receiving Facility Universal ID Type"
* messageDateTime 1..1 dateTime "MSH-7 The date and time the message was created"
* messageType 1..1 Coding "MSH-9 The type of message"
* messageTypeMessageCode 1..1 id "MSH-9.1 Message Code"
* messageTypeTriggerEvent 1..1 id "MSH-9.2 Trigger Event"
* messageTypeMessageStructure 1..1 id "MSH-9.3 Message Structure"
* messageControlId 1..1 string "MSH-10 a control id for the message"
* processingId 1..1 string "MSH-11 the processing id"
* versionId 1..1 string "MSH-12 the HL7 version of this message"
* acceptAcknowledgmentType 1..1 id "MSH-15 Accept Acknowledgment Type"
* applicationAcknowledgementType 1..1 id "MSH-16 Application Acknowledgement Type"
* messageProfileIdentifier 1..2 Identifier "MSH-21 Message Profile Identifier"
* messageProfileIdentifierEntityIdentifier 1..1 string "MSH-21.1 Message Profile Identifier Entity Identifier"
* messageProfileIdentifierUniversalId 1..1 string "MSH-21.3 Message Profile Identifier Universal ID"
* messageProfileIdentifierUniversalIdType 1..1 id "MSH-21.4 Message Profile Identifier Universal ID Type"



// Provenance Section begins
Instance: segment-msh-logical-model-history-create
InstanceOf: Provenance
Title: "Initial creation of MSH segment changelog"
Usage: #definition
* target[+] = Reference(StructureDefinition/segment-msh-logical-model)
* recorded = "2023-08-29T17:46:36.0000Z"
* occurredDateTime = "2023-08-29"
* reason = http://terminology.hl7.org/CodeSystem/v3-ActReason#METAMGT
* reason.text = "Created an MSH segment resource"
* activity = http://terminology.hl7.org/CodeSystem/v3-DataOperation#CREATE
* agent[+].type = http://terminology.hl7.org/CodeSystem/provenance-participant-type#author
* agent[=].who.display = "T. R. Johnson"
