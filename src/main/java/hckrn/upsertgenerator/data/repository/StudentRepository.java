package hckrn.upsertgenerator.data.repository;

import hckrn.upsertgenerator.data.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
