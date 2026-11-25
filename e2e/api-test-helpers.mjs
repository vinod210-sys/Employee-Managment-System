import { randomUUID } from "node:crypto";

export const baseUrl = process.env.EMPLOYEE_API_URL ?? "http://localhost:8082";

export function newEmployee(prefix, overrides = {}) {
  const token = randomUUID();
  return {
    name: `${prefix}-${token}`,
    email: `${token}@example.test`,
    department: `Department-${token}`,
    salary: 50000.0,
    ...overrides,
  };
}

export async function apiRequest(method, path, body) {
  const response = await fetch(new URL(path, baseUrl), {
    method,
    headers: body === undefined ? {} : { "Content-Type": "application/json" },
    body: body === undefined ? undefined : JSON.stringify(body),
  });
  const text = await response.text();
  let payload = text;
  if (text) {
    try {
      payload = JSON.parse(text);
    } catch {
      // Plain text response
    }
  }
  return { status: response.status, payload };
}

export async function removeEmployee(id) {
  if (id !== undefined) {
    const response = await apiRequest("DELETE", `/employees/${id}`);
    if (response.status !== 200 && response.status !== 404) {
      throw new Error(`Cleanup failed for employee ${id}: HTTP ${response.status}`);
    }
  }
}
