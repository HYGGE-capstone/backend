package hygge.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageRoom {
    @Id @GeneratedValue
    @Column(name = "MESSAGE_ROOM_ID") private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MESSAGE_ROOM_FROM_ID")
    private Member from;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MESSAGE_ROOM_TO_ID")
    private Member to;

    @OneToMany(mappedBy = "messageRoom")
    private List<Message> messages = new ArrayList<>();

    @JsonIgnore
    private LocalDateTime lastOpenTime;

    @Builder
    public MessageRoom(Member from, Member to) {
        this.from = from;
        this.to = to;
    }
}
