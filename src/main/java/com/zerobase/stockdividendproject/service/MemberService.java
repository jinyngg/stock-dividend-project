package com.zerobase.stockdividendproject.service;

import com.zerobase.stockdividendproject.Repository.MemberRepository;
import com.zerobase.stockdividendproject.entity.Member;
import com.zerobase.stockdividendproject.exception.StockDividendException;
import com.zerobase.stockdividendproject.model.AuthDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.zerobase.stockdividendproject.Type.ErrorCode.*;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("couldn't find user -> " + username));
    }

    public Member register(AuthDto.SignUp member) {
        boolean exists = this.memberRepository.existsByUsername(member.getUsername());
        if (exists) {
            throw new StockDividendException(USER_ALREADY_EXISTS);
        }
        
        // 사용자 정보 -> 암호화 (인코딩된 패스워드를 저장)
        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        var result = this.memberRepository.save(member.toEntity());

        return result;
    }

    public Member authenticate(AuthDto.SignIn member) {
        var user = this.memberRepository.findByUsername(member.getUsername())
//                .orElseThrow(() -> new RuntimeException("존재하지 않는 ID 입니다."));
                .orElseThrow(() -> new StockDividendException(ID_NOT_EXISTS));

        if (!this.passwordEncoder.matches(member.getPassword(), user.getPassword())) {
//            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
            throw new StockDividendException(PASSWORD_NOT_MATCH);
        }

        return user;
    }
}
