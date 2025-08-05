package msa.productservice.kafka.consumer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCreatedEvent {
    private Long memberId;
    private String loginId;
    private String name;
    private String email;
    // 필요한 필드만 작성
}
