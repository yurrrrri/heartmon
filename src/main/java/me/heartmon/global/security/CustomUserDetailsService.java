package me.heartmon.global.security;

import lombok.RequiredArgsConstructor;
import me.heartmon.domain.member.repository.MemberRepository;
import me.heartmon.domain.member.entity.Member;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("\"%s\" is invalid username.".formatted(username)));

        return new User(member.getUsername(), member.getPassword(), member.getGrantedAuthorities());
    }
}