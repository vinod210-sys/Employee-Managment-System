package employee.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import employee.demo.dto.EmployeeDTO;
import employee.demo.dto.EmployeeRequestDTO;
import employee.demo.entity.Employee;
import employee.demo.exception.DuplicateEmailException;
import employee.demo.exception.EmployeeNotFoundException;
import employee.demo.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Add Employee
    public Employee addEmployee(Employee employee) {
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    // Get All Employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Get Employee By ID
    public Employee getEmployeeById(int id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + id + " not found"));
    }

    // Update Employee
    public Employee updateEmployee(int id, Employee newEmployee) {
        Employee existingEmployee = getEmployeeById(id);

        if (!existingEmployee.getEmail().equals(newEmployee.getEmail())
                && employeeRepository.existsByEmail(newEmployee.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + newEmployee.getEmail());
        }

        existingEmployee.setName(newEmployee.getName());
        existingEmployee.setEmail(newEmployee.getEmail());
        existingEmployee.setDepartment(newEmployee.getDepartment());
        existingEmployee.setSalary(newEmployee.getSalary());

        return employeeRepository.save(existingEmployee);
    }

    // Delete Employee
    public void deleteEmployee(int id) {
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
    }

    // Search Employees By Department
    public List<Employee> searchEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment(department);
    }

    // Pagination
    public Page<Employee> getEmployeesWithPagination(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    // Get Employee DTO By ID
    public EmployeeDTO getEmployeeDTOById(int id) {
        Employee employee = getEmployeeById(id);
        return new EmployeeDTO(
                employee.getId(),
                employee.getName(),
                employee.getDepartment(),
                employee.getSalary()
        );
    }

    // Add Employee From DTO
    public Employee addEmployeeFromDTO(EmployeeRequestDTO dto) {
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + dto.getEmail());
        }

        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary());

        return employeeRepository.save(employee);
    }

    // Update Employee using DTO
    public Employee updateEmployeeFromDTO(int id, EmployeeRequestDTO dto) {
        Employee employee = getEmployeeById(id);

        if (!employee.getEmail().equals(dto.getEmail())
                && employeeRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + dto.getEmail());
        }

        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary());

        return employeeRepository.save(employee);
    }

    // Search Employees By Name (Exact)
    public List<Employee> searchEmployeesByName(String name) {
        return employeeRepository.findByName(name);
    }

    // Search Employees By Partial Name
    public List<Employee> searchEmployeesByPartialName(String name) {
        return employeeRepository.findByNameContaining(name);
    }

    // Sort Employees
    public List<Employee> sortEmployees(String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return employeeRepository.findAll(sort);
    }
}

