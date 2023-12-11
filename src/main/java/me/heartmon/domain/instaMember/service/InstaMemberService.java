package me.heartmon.domain.instaMember.service;

import lombok.RequiredArgsConstructor;
import me.heartmon.domain.instaMember.entity.InstaMember;
import me.heartmon.domain.instaMember.repository.InstaMemberRepository;
import me.heartmon.domain.member.entity.Member;
import me.heartmon.global.resultData.ResultData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class InstaMemberService {

    private final InstaMemberRepository instaMemberRepository;

    @Transactional(readOnly = true)
    public Optional<InstaMember> findByUsername(String username) {
        return instaMemberRepository.findByUsername(username);
    }

    public ResultData<String> connect(Member member, String username) {
        Optional<InstaMember> opInstaMember = findByUsername(username);

        if (opInstaMember.isPresent()) {
            InstaMember instaMember = opInstaMember.get();

            if (instaMember.isSelf()) {
                return ResultData.of("F-I1", "이미 등록되어 있는 인스타그램 아이디입니다.", username);
            }

            instaMember.setIsSelf();
            return ResultData.of("S-I1", "인스타그램 아이디가 등록되었습니다.", username);
        }

        InstaMember newInstaMember = create(username, true);

        member.connectInstaMember(newInstaMember);

        return ResultData.of("S-I2", "인스타그램 아이디가 등록되었습니다.", username);
    }

    public ResultData<InstaMember> findByUsernameOrCreate(String username) {
        Optional<InstaMember> opInstaMember = findByUsername(username);
        return opInstaMember.map(instaMember -> ResultData.of("S-I3", "인스타그램 아이디를 조회했습니다.", instaMember))
                .orElseGet(() -> ResultData.of("S-I4", "인스타그램 아이디가 등록되었습니다.", create(username, false)));
    }

    private InstaMember create(String username, boolean isSelf) {
        return instaMemberRepository.save(InstaMember.builder()
                .username(username)
                .isSelf(isSelf)
                .build());
    }
}
