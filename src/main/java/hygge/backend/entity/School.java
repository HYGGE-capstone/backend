package hygge.backend.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class School {

    @Id @GeneratedValue
    @Column(name = "SCHOOL_ID")
    private Long id;

    @Column(name = "SCHOOL_NAME")
    private String schoolName;

    @Column(name = "EMAIL_FORM")
    private String emailForm;

    @OneToMany(mappedBy = "school")
    private List<User> users = new ArrayList<>();
}
