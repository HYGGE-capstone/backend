package hygge.backend.entity;

import hygge.backend.dto.subject.SubjectDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private String professorName;  // 교수명

    private String time;  // 수업시간 ex) 월A 수B

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHOOL_ID")
    private School school;

    @OneToMany(mappedBy = "subject")
    private List<Team> teams = new ArrayList<>();

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<Subscribe> subscribes = new ArrayList<>();

    @Builder
    public Subject(Long id, String name, String code, int year, Semester semester, String professorName, String time, School school) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.year = year;
        this.semester = semester;
        this.professorName = professorName;
        this.time = time;
        this.school = school;
    }

    public void change(SubjectDto subjectDto) {
        log.info("Subject.change(), subjectId : {}", this.getId());

        this.name = subjectDto.getName();
        this.code = subjectDto.getCode();
        this.year = subjectDto.getYear();
        this.semester = subjectDto.getSemester();
        this.professorName = subjectDto.getProfessorName();
        this.time = subjectDto.getTime();
    }
}
