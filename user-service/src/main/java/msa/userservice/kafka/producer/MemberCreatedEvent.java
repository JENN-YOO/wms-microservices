package msa.userservice.kafka.producer;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCreatedEvent {
    private Long memberId;
    private String loginId;
    private String name;
    private String email;
    // 필요한 필드만 작성
}
