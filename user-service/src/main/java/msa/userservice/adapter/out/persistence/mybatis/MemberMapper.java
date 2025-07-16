package msa.userservice.adapter.out.persistence.mybatis;

import io.lettuce.core.dynamic.annotation.Param;
import msa.userservice.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {
    Optional<Member> findByLoginIdMapper(@Param("loginId") String loginId);
    void save(Member member);
}
