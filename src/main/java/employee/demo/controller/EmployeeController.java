package employee.demo.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import employee.demo.dto.EmployeeDTO;
import employee.demo.dto.EmployeeRequestDTO;
import employee.demo.entity.Employee;
import employee.demo.service.EmployeeService;
import jakarta.validation.Valid;

@Valid
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Add Employee
    @PostMapping
    public Employee addEmployee(@Valid @RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    // Get All Employees
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // Search by Department
    @GetMapping("/department/{department}")
    public List<Employee> getEmployeesByDepartment(@PathVariable String department) {
        return employeeService.searchEmployeesByDepartment(department);
    }

    // Get Employee By ID
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable int id) {
        return employeeService.getEmployeeById(id);
    }

    // Update Employee
    @PutMapping("/{id}")
    public Employee updateEmployee(
            @PathVariable int id,
            @Valid @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    // Delete Employee
    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployee(id);
        return "Employee deleted successfully";
    }

    // Pagination
    @GetMapping("/pagination")
    public Page<Employee> getEmployeesWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return employeeService.getEmployeesWithPagination(pageable);
    }

    // Get Employee DTO By ID
    @GetMapping("/dto/{id}")
    public EmployeeDTO getEmployeeDTO(@PathVariable int id) {
        return employeeService.getEmployeeDTOById(id);
    }

    // Add Employee From DTO
    @PostMapping("/dto")
    public Employee addEmployeeFromDTO(@Valid @RequestBody EmployeeRequestDTO dto) {
        return employeeService.addEmployeeFromDTO(dto);
    }

    // Update Employee using DTO
    @PutMapping("/dto/{id}")
    public Employee updateEmployeeFromDTO(
            @PathVariable int id,
            @Valid @RequestBody EmployeeRequestDTO dto) {
        return employeeService.updateEmployeeFromDTO(id, dto);
    }

    // Search by Exact Name
    @GetMapping("/name/{name}")
    public List<Employee> getEmployeesByName(@PathVariable String name) {
        return employeeService.searchEmployeesByName(name);
    }

    // Search by Partial Name
    @GetMapping("/search")
    public List<Employee> searchEmployeesByPartialName(@RequestParam String name) {
        return employeeService.searchEmployeesByPartialName(name);
    }

    // Sort Employees
    @GetMapping("/sort")
    public List<Employee> sortEmployees(
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return employeeService.sortEmployees(sortBy, direction);
    }
}

