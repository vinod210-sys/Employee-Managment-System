import assert from "node:assert/strict";
import test from "node:test";
import { apiRequest, newEmployee, removeEmployee } from "./api-test-helpers.mjs";

test("employee search, sort, and pagination return persisted records", async (t) => {
  const department = `Search-${crypto.randomUUID()}`;
  const first = newEmployee("search-alpha", { department });
  const second = newEmployee("search-beta", { department });
  const ids = [];
  t.after(async () => Promise.all(ids.map(removeEmployee)));

  for (const employee of [first, second]) {
    const created = await apiRequest("POST", "/employees", employee);
    assert.equal(created.status, 200);
    ids.push(created.payload.id);
  }

  const byDepartment = await apiRequest(
    "GET",
    `/employees/department/${encodeURIComponent(department)}`,
  );
  assert.equal(byDepartment.status, 200);
  assert.deepEqual(new Set(byDepartment.payload.map((item) => item.id)), new Set(ids));

  const byName = await apiRequest("GET", `/employees/name/${encodeURIComponent(first.name)}`);
  assert.equal(byName.status, 200);
  assert.ok(byName.payload.some((item) => item.id === ids[0]));

  const partialName = first.name.slice(0, 12);
  const partialSearch = await apiRequest(
    "GET",
    `/employees/search?name=${encodeURIComponent(partialName)}`,
  );
  assert.equal(partialSearch.status, 200);
  assert.ok(partialSearch.payload.some((item) => item.id === ids[0]));

  const sorted = await apiRequest("GET", "/employees/sort?sortBy=name&direction=asc");
  assert.equal(sorted.status, 200);
  const names = sorted.payload.map((item) => item.name);
  assert.deepEqual(names, [...names].sort((a, b) => a.localeCompare(b)));

  const page = await apiRequest("GET", "/employees/pagination?page=0&size=100&sortBy=id&direction=asc");
  assert.equal(page.status, 200);
  assert.ok(page.payload.content.some((item) => ids.includes(item.id)));
});

