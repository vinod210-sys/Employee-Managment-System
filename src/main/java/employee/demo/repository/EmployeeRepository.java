package employee.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import employee.demo.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findByDepartment(String department);

    List<Employee> findByName(String name);

    List<Employee> findByNameContaining(String name);

    boolean existsByEmail(String email);
}

