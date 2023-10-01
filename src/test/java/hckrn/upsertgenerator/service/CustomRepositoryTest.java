package hckrn.upsertgenerator.service;

import hckrn.upsertgenerator.data.Student;
import hckrn.upsertgenerator.data.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;


@SpringBootTest
class CustomRepositoryTest {

    SecureRandom random = new SecureRandom();

    @Autowired
    CustomRepository customRepository;

    @Autowired
    StudentRepository studentRepository;

    @Test
    void shouldFindExecutionSpeedForUpsertAndRepoSaving() {
        studentRepository.deleteAll();

        List<Student> students = generateStudentList(10000);

        long repoSavingMillis = saveUsingRepository(students);
        long upsertSavingMillis = saveUsingUpsert(students);
        Assertions.assertThat(upsertSavingMillis).isLessThan(repoSavingMillis);
    }

    private List<Student> generateStudentList(int listSize) {
        var students = new LinkedList<Student>();
        for (int i = 1; i <= listSize; i++) {
            String firstName = randomFirstName();
            String lastName = randomLastName();
            String email = generateEmail(firstName, lastName, i);

            Student student = new Student();
            student.setId(i);
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setEmail(email);
            student.setDateOfBirth(LocalDate.now().minusYears(random.nextInt(18, 66)));

            students.add(student);
        }

        return students;
    }

    long saveUsingRepository(List<Student> students) {
        long startMillis = System.currentTimeMillis();
        studentRepository.saveAll(students);
        long executionMillis = System.currentTimeMillis() - startMillis;
        String message = String.format("Saving using repository:\n\tTime: %d ms\n\tStudents Processed: %d",
                executionMillis, students.size());
        System.out.println(message);
        return executionMillis;
    }

    long saveUsingUpsert(List<Student> students) {
        long startMillis = System.currentTimeMillis();
        customRepository.upsertAll(students);
        long executionMillis = System.currentTimeMillis() - startMillis;
        String message = String.format("Saving using upsert:\n\tTime: %d ms\n\tStudents Processed: %d",
                executionMillis, students.size());
        System.out.println(message);
        return executionMillis;
    }

    String randomFirstName() {
        var names = List.of(
                "Emma",
                "Liam",
                "Olivia",
                "Noah",
                "Ava",
                "Ethan",
                "Sophia",
                "Mason",
                "Isabella",
                "Alexander"
        );
        return names.get(random.nextInt(0, names.size()));
    }

    String randomLastName() {
        var names = List.of(
                "Smith",
                "Johnson",
                "Williams",
                "Brown",
                "Jones",
                "Garcia",
                "Miller",
                "Davis",
                "Rodriguez",
                "Martinez"
        );
        return names.get(random.nextInt(0, names.size()));
    }

    String generateEmail(String firstName, String lastName, Integer id) {
        return firstName + "_" + lastName + "_" + id + "@mail.com";
    }

}