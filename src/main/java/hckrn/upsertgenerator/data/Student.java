package hckrn.upsertgenerator.data;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "students")
public class Student {

    @Id
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    private String lastName;

    @Column(name = "email")
    private String email;

    @Column
    private LocalDate dateOfBirth;
}