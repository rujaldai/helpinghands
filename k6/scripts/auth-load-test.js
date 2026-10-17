import http from "k6/http";
import { check, sleep } from "k6";

export const options = {
    stages: [
        { duration: "30s", target: 20 },
        { duration: "1m", target: 50 },
        { duration: "30s", target: 0 },
    ],
    thresholds: {
        http_req_failed: ["rate<0.01"],
        http_req_duration: ["p(95)<700"],
    },
};

const BASE_URL = __ENV.BASE_URL || "http://backend:8080";

export function setup() {
    const payload = JSON.stringify({
        email: "rujaldai@gmail.com",
        password: "Test@123",
    });

    const res = http.post(`${BASE_URL}/api/auth/login`, payload, {
        headers: { "Content-Type": "application/json" },
    });

    check(res, {
        "login successful": (r) => r.status === 200,
    });

    const token = res.json("token");
    return { token };
}

export default function (data) {
    const headers = {
        Authorization: `Bearer ${data.token}`,
        "Content-Type": "application/json",
    };

    const res = http.get(`${BASE_URL}/api/donations/my`, { headers });

    check(res, {
        "authorized request OK": (r) => r.status === 200,
    });

    sleep(1);
}
