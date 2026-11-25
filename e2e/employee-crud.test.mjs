import assert from "node:assert/strict";
import test from "node:test";
import { apiRequest, newEmployee, removeEmployee } from "./api-test-helpers.mjs";

test("employee CRUD persists changes and handles duplicate and missing records", async (t) => {
  const employee = newEmployee("crud");
  let id;
  t.after(async () => removeEmployee(id));

  const created = await apiRequest("POST", "/employees", employee);
  assert.equal(created.status, 200);
  id = created.payload.id;
  assert.ok(Number.isInteger(id) && id > 0);

  const read = await apiRequest("GET", `/employees/${id}`);
  assert.equal(read.status, 200);
  assert.equal(read.payload.email, employee.email);
  assert.equal(read.payload.name, employee.name);

  const listed = await apiRequest("GET", "/employees");
  assert.equal(listed.status, 200);
  assert.ok(listed.payload.some((item) => item.id === id));

  const duplicate = await apiRequest("POST", "/employees", employee);
  assert.equal(duplicate.status, 409);

  const updated = { ...employee, name: `${employee.name}-updated`, salary: 60000.0 };
  const updateResponse = await apiRequest("PUT", `/employees/${id}`, updated);
  assert.equal(updateResponse.status, 200);
  const readBack = await apiRequest("GET", `/employees/${id}`);
  assert.equal(readBack.payload.name, updated.name);
  assert.equal(readBack.payload.salary, updated.salary);

  const deleted = await apiRequest("DELETE", `/employees/${id}`);
  assert.equal(deleted.status, 200);
  id = undefined;
  const missing = await apiRequest("GET", `/employees/${created.payload.id}`);
  assert.equal(missing.status, 404);
});

