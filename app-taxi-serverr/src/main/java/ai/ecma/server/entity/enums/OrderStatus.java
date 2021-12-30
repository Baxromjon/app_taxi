package ai.ecma.server.entity.enums;

public enum OrderStatus {
    NEW,//bu yangi zakaz///
    WAITING,//HAYDOVCHI QABUL QILDI, KLIENT KUTYAPTI
    IN_PROGRESS,//HAYOVCHI YULDA, KLINT BILAN BIRGA
    CANCELED,//
    REJECTED,
    CLOSED,
}
