package com.shop.service;

import com.shop.dto.MemberASDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service // 나 서비스다.
@Transactional // 트랜젝션설정 : 성공을하면 그대로 적용 실패하면 롤백
@RequiredArgsConstructor // final 또는 @NonNull 명령어가 붙으면 객체를 자동 붙혀줍니다.
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member); // 데이터베이스에 저장을 하라는 명령
    }
    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }
        //빌더패턴
        return User.builder().username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
    public String loadMemberEmail(Principal principal, HttpSession httpSession) { // 외부 로그인 멤버 와 회원가입 폼 Dto 로 들어온 멤버 분기
        String userEmail = "";
        Member member1 = ((Member) httpSession.getAttribute("user"));

        if (principal != null) {
            if (member1 != null) {
                String email = (String) httpSession.getAttribute("email");
                memberRepository.save(member1);
                return email;
            } else {
                userEmail = principal.getName();
                System.out.println(userEmail + " memberService3");
                return principal.getName();
            }
        }
        return null;
    }
    public String loadMemberName(Principal principal, HttpSession httpSession) { // 외부 로그인 멤버 와 회원가입 폼 Dto 로 들어온 멤버 분기
        String userName = "";
        String MemberName = (String) httpSession.getAttribute("name");

        if (principal != null) {
            if (httpSession.getAttribute("user") != null) {

                System.out.println(MemberName );

                return MemberName;
            }
            else {
                userName = principal.getName();
                Member member = memberRepository.findByEmail(userName);
                return member.getName();
            }
        }
        return null;
    }
    public Member findMember(HttpSession httpSession, Principal principal) {
        String email = loadMemberEmail(principal, httpSession);
        return memberRepository.findByEmail(email);
    }
    public void memberAS(MemberASDto memberASDto) {
        Member member = memberRepository.findByEmail(memberASDto.getEmail());
        member = member.MemberAS(memberASDto);
        memberRepository.save(member);
    }
}
