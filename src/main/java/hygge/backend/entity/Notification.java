package hygge.backend.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOTI_TO")
    private Member to;

    @CreatedDate
    private LocalDateTime createdTime;

    private boolean isOpened;

    //
    public void open() {
        this.isOpened = true;
    }

    //
    @Builder
    public Notification(String from, String content, Member to, boolean isOpened) {
        this.from = from;
        this.content = content;
        this.to = to;
        this.isOpened = isOpened;
    }
}
