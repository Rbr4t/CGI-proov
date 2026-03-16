const baseURL = 'http:localhost:8080/api'

async function handleResponse(res: Response) {
    if (!res.ok) {
        throw new Error(`HTTP error: ${res.status}`);
    }
    return res.json();
}

export async function getTables() {
    const res = await fetch(`${baseURL}/tables`)
    return handleResponse(res)
}

export async function getReservations() {
    const res = await fetch(`${baseURL}/reservations`)
    return handleResponse(res)
}

export async function getRecommendations(partySize: number, startTime: string, zone?: string, features?: string) {
    const params = new URLSearchParams({ partySize: String(partySize), startTime})
    if (zone) params.append('zone', zone);
    if (features) params.append('features', features);

    const res = await fetch(`${baseURL}/recommendations?${params}`)
    return handleResponse(res)
}

export async function createReservation(reservation: object) {
    const res = await fetch(`${baseURL}/reservations`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(reservation)
    })

    return handleResponse(res)
}
