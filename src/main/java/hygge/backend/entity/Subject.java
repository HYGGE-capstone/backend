package hygge.backend.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Subject {

    @Id @GeneratedValue
    @Column(name = "SUBJECT_ID")
    private Long id;

    @Column(name = "SUBJECT_NAME")
    private String name;

    private String code;

    private int year;

    @Enumerated(EnumType.STRING)
    private Semester semester;

    @Column(name = "P_NAME")
    private String pName;  // 교수명

    private String time;  // 수업시간 ex) 월A 수B

}
