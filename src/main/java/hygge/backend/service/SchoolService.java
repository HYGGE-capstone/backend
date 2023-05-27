package hygge.backend.service;

import hygge.backend.dto.school.SchoolDto;
import hygge.backend.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolService {
    private final SchoolRepository schoolRepository;

    @Transactional(readOnly = true)
    public List<SchoolDto> getSchools() {
        return schoolRepository.findAll()
                .stream().map(school -> new SchoolDto(school)).collect(Collectors.toList());
    }
}
