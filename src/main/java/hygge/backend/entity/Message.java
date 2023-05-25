package hygge.backend.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Message {

    @Id
    @GeneratedValue
    @Column(name = "MESSAGE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MESSAGE_FROM")
    private Member from;

    @Column(name = "MESSAGE_CONTENT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MESSAGE_TO")
    private Member to;

    @CreatedDate
    private LocalDateTime createdTime;

    private boolean isOpened;

    //
    public void open() {
        this.isOpened = true;
    }

    @Builder
    public Message(Member from, String content, Member to, boolean isOpened) {
        this.from = from;
        this.content = content;
        this.to = to;
        this.isOpened = isOpened;
    }
}
