package hygge.backend.service;

import hygge.backend.dto.school.SchoolDto;
import hygge.backend.dto.school.SchoolNoIdDto;
import hygge.backend.entity.School;
import hygge.backend.error.exception.BusinessException;
import hygge.backend.error.exception.ExceptionInfo;
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

    @Transactional
    public SchoolDto postSchool(SchoolNoIdDto schoolNoIdDto) {
        School school = School.builder()
                .schoolName(schoolNoIdDto.getSchoolName())
                .emailForm(schoolNoIdDto.getSchoolEmailForm())
                .build();

        School savedSchool = schoolRepository.save(school);
        return new SchoolDto(savedSchool);
    }

    @Transactional
    public SchoolDto fixSchool(SchoolDto schoolDto) {
        School school = schoolRepository.findById(schoolDto.getSchoolId())
                .orElseThrow(() -> new BusinessException(ExceptionInfo.CANNOT_FIND_SCHOOL));

        school.changeSchoolName(schoolDto.getSchoolName());
        school.changeEmailForm(schoolDto.getSchoolEmailForm());

        School savedSchool = schoolRepository.save(school);
        return new SchoolDto(savedSchool);
    }
}
