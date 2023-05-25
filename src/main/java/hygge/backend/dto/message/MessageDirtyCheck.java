package hygge.backend.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDirtyCheck {
    private boolean isDirty;

    public void setDirty(boolean dirty) {
        this.isDirty = dirty;
    }
}
