package hygge.backend.service;

import hygge.backend.dto.response.subject.SearchSubjectResponse;
import hygge.backend.entity.Subject;
import hygge.backend.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public SearchSubjectResponse searchSubjects(String query) {
        List<Subject> subjects = subjectRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(query, query);
        return SearchSubjectResponse.builder().subjects(subjects).build();
    }
}
