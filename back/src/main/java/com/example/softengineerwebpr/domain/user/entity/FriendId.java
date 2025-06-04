package com.example.softengineerwebpr.domain.user.entity;

import java.io.Serializable;
import java.util.Objects;

public class FriendId implements Serializable {
    private Long requester; // User 엔티티의 ID 타입과 일치 (requester_idx 매핑)
    private Long recipient; // User 엔티티의 ID 타입과 일치 (recipient_idx 매핑)

    public FriendId() {
    }

    public FriendId(Long requester, Long recipient) {
        this.requester = requester;
        this.recipient = recipient;
    }

    // Getter, Setter (필요시)

    public Long getRequester() {
        return requester;
    }

    public void setRequester(Long requester) {
        this.requester = requester;
    }

    public Long getRecipient() {
        return recipient;
    }

    public void setRecipient(Long recipient) {
        this.recipient = recipient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendId friendId = (FriendId) o;
        return Objects.equals(requester, friendId.requester) &&
                Objects.equals(recipient, friendId.recipient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requester, recipient);
    }
}