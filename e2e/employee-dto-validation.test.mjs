import assert from "node:assert/strict";
import test from "node:test";
import { apiRequest, newEmployee, removeEmployee } from "./api-test-helpers.mjs";

test("DTO routes persist data and invalid employee input is rejected", async (t) => {
  const employee = newEmployee("dto");
  let id;
  t.after(async () => removeEmployee(id));

  const invalidEntity = await apiRequest("POST", "/employees", {
    ...employee,
    name: "",
    salary: -100,
  });
  assert.equal(invalidEntity.status, 400);

  const created = await apiRequest("POST", "/employees/dto", employee);
  assert.equal(created.status, 200);
  id = created.payload.id;
  assert.ok(Number.isInteger(id) && id > 0);

  const dto = await apiRequest("GET", `/employees/dto/${id}`);
  assert.equal(dto.status, 200);
  assert.equal(dto.payload.name, employee.name);
  assert.equal(dto.payload.department, employee.department);

  const updated = { ...employee, name: `${employee.name}-updated`, department: "DTO-Updated" };
  const updateResponse = await apiRequest("PUT", `/employees/dto/${id}`, updated);
  assert.equal(updateResponse.status, 200);
  const readBack = await apiRequest("GET", `/employees/${id}`);
  assert.equal(readBack.payload.name, updated.name);
  assert.equal(readBack.payload.department, updated.department);

  const invalidDto = await apiRequest("POST", "/employees/dto", {
    ...employee,
    email: "not-an-email",
    salary: 0,
  });
  assert.equal(invalidDto.status, 400);

  const missingDto = await apiRequest("GET", `/employees/dto/${id + 1000000000}`);
  assert.equal(missingDto.status, 404);
});

