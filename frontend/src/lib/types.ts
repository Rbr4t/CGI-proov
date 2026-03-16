export interface Table {
    id: number;
    capacity: number;
    zone: string;
    features: string;
    x: number;
    y: number;
    score?: number;
}

export interface Reservation {
    id?: number;
    tableId: number;
    partySize: number;
    startTime: string;
    customerName: string;
}