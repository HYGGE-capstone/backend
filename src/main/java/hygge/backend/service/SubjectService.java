package hygge.backend.service;

import hygge.backend.dto.SubjectDto;
import hygge.backend.dto.response.subject.SearchSubjectResponse;
import hygge.backend.entity.Subject;
import hygge.backend.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public SearchSubjectResponse searchSubjects(String query) {
        List<SubjectDto> subjects = subjectRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(query, query)
                .stream().map(subject -> new SubjectDto(subject)).collect(Collectors.toList());
        return SearchSubjectResponse.builder().subjects(subjects).build();
    }
}
