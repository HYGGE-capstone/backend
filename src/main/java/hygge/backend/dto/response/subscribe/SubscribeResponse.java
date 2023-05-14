package hygge.backend.dto.response.subscribe;

import com.fasterxml.jackson.annotation.JsonProperty;
import hygge.backend.entity.Subject;
import hygge.backend.entity.Subscribe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeResponse {

    private List<Subject> subscribes;
}
