package me.heartmon.domain.heartTarget.service;

import lombok.RequiredArgsConstructor;
import me.heartmon.domain.heartTarget.entity.HeartTarget;
import me.heartmon.domain.heartTarget.repository.HeartTargetRepository;
import me.heartmon.domain.instaMember.entity.InstaMember;
import me.heartmon.domain.instaMember.service.InstaMemberService;
import me.heartmon.domain.member.entity.Member;
import me.heartmon.global.resultData.ResultData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class HeartTargetService {

    private final HeartTargetRepository heartTargetRepository;
    private final InstaMemberService instaMemberService;

    @Transactional(readOnly = true)
    public Optional<HeartTarget> findById(Long id) {
        return heartTargetRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<HeartTarget> findByInstaMember(InstaMember instaMember) {
        return heartTargetRepository.findByFromInstaMember(instaMember);
    }

    @Transactional(readOnly = true)
    public List<HeartTarget> findByInstaMember_Id(Long fromInstaMemberId) {
        return heartTargetRepository.findByFromInstaMember_Id(fromInstaMemberId);
    }

    public ResultData heart(Member member, String username, int reasonPoint) {
        if (!member.isConnectedInstaMember()) {
            return ResultData.of("F-H1", "인스타그램 아이디를 등록해주세요.");
        }

        if (member.getInstaMember().getUsername().equals(username)) {
            return ResultData.of("F-H2", "본인에게 하트할 수 없습니다.");
        }

        InstaMember fromInstaMember = member.getInstaMember();
        InstaMember toInstaMember = (InstaMember) instaMemberService.findByUsernameOrCreate(username).getData();

        HeartTarget heartTarget = HeartTarget.builder()
                .fromInstaMember(fromInstaMember)
                .fromInstaMemberUsername(fromInstaMember.getUsername())
                .toInstaMember(toInstaMember)
                .toInstaMemberUsername(toInstaMember.getUsername())
                .reasonPoint(reasonPoint)
                .build();

        heartTargetRepository.save(heartTarget);

        fromInstaMember.addMyHeartTarget(heartTarget);
        toInstaMember.addYourHeartTarget(heartTarget);

        return ResultData.of("S-H1", "%s 님이 하트되었습니다!".formatted(username));
    }

    public ResultData cancel(Member member, HeartTarget heartTarget) {
        if (!member.getInstaMember().equals(heartTarget.getFromInstaMember())) {
            return ResultData.of("F-H3", "하트를 취소할 권한이 없습니다.");
        }

        String target = heartTarget.getToInstaMemberUsername();
        heartTargetRepository.delete(heartTarget);
        return ResultData.of("S-H2", "%s 님에 대한 하트를 취소했습니다.".formatted(target));
    }
}
