package hygge.backend.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue
    @Column(name = "NOTI_ID")
    private Long id;

    @Column(name = "NOTI_FROM")
    private String from;

    @Column(name = "NOTI_CONTENT")
    private String content;

    @CreatedDate
    private LocalDateTime createdTime;

    private boolean isOpened;
}
