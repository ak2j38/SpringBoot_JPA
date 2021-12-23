package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 테스트")
    void joinMemberTest() {
        // given
        Member member = new Member();
        member.setName("park");
        member.setAddress(new Address("서울시", "노원로", "564"));

        // when
        Long memberId = memberService.join(member);

        // then
        assertThat(member).isEqualTo(memberRepository.findOne(memberId));
    }

    @Test
    @DisplayName("중복회원 테스트")
    void duplicateMemberTest() {
        // given
        Member member1 = new Member();
        member1.setName("park");
        member1.setAddress(new Address("서울시", "노원로", "564"));

        Member member2 = new Member();
        member2.setName("park");
        member2.setAddress(new Address("서울시", "노원로", "564"));

        // when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

}