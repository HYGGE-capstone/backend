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

    @OneToMany(mappedBy = "messageRoom", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    @JsonIgnore
    private LocalDateTime lastOpenTime;

    private LocalDateTime lastUpdateTime;
    private boolean isDirty;

    @Builder
    public MessageRoom(Member from, Member to, boolean isDirty) {
        this.from = from;
        this.to = to;
        this.isDirty = isDirty;
    }

    // ------
    public void updateTime() {
        this.lastUpdateTime = LocalDateTime.now();
    }

    public void open(){
        this.lastOpenTime = LocalDateTime.now();
    }

    public void setDirty(boolean dirty) {
        this.isDirty = dirty;
    }
}
