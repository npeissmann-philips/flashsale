import http from 'k6/http';
import { check } from 'k6';
import { Counter } from 'k6/metrics';

// Custom metrics to track success and rejection counts
export const successCounter = new Counter('successful_requests');
export const rejectCounter = new Counter('rejected_requests');

export const options = {
    /*vus: 10000,
    iterations: 10000,
    maxDuration: '20m'*/
    stages: [
            { duration: '10s', target: 100 },   // ramp up to 100 VUs
            { duration: '30s', target: 1000 },  // scale to 1,000 VUs
            { duration: '1m', target: 2000 },   // peak stress at 2,000 VUs
            { duration: '10s', target: 0 },     // ramp down
    ],
};

export function setup() {
    const now = new Date();
    const dateTime = now.toISOString().replace(/[:.]/g, '-').slice(0, -5);
    const eventName = `Stress Test Event - ${dateTime}`;
    
    const payload = JSON.stringify({
        name: eventName,
        totalCapacity: 10,
        remainingCapacity: 10,
        eventDate: '2026-12-31'
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post('http://localhost:8080/events', payload, params);

    check(res, {
        'event created successfully': (r) => r.status >= 200 && r.status < 300,
    });

    let eventId = null;
    if (res.status >= 200 && res.status < 300) {
        const event = JSON.parse(res.body);
        eventId = event.id;
    } else {
        console.error('Failed to create event. Status:', res.status, 'Body:', res.body);
    }

    return { eventId: eventId };
}

export default function (data) {
    const payload = JSON.stringify({
        eventId: data.eventId,
        orderDate: '2026-02-22',
        status: 'PENDING',
        userId: '3fa85f64-5717-4562-b3fc-2c963f66afa6'
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post('http://localhost:8080/orders', payload, params);

    const isSuccess = res.status >= 200 && res.status < 300;

    if (isSuccess) {
        successCounter.add(1);
    } else {
        rejectCounter.add(1);
    }

    // You can adjust the expected status codes depending on your API's response
    check(res, {
        'status is success (2xx)': (r) => r.status >= 200 && r.status < 300,
        'status is rejected (4xx/5xx)': (r) => r.status >= 400,
    });
}