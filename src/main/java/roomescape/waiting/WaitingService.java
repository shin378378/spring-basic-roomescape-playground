package roomescape.waiting;

import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.dto.WaitingRequest;
import roomescape.waiting.dto.WaitingResponse;

@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final MemberRepository memberRepository;

    public WaitingService(WaitingRepository waitingRepository,
                          ThemeRepository themeRepository,
                          TimeRepository timeRepository,
                          MemberRepository memberRepository) {
        this.waitingRepository = waitingRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.memberRepository = memberRepository;
    }

    public WaitingResponse save(WaitingRequest waitingRequest) {
        Theme theme = themeRepository.getById(waitingRequest.getTheme());
        Time time = timeRepository.getById(waitingRequest.getTime());
        Member member = memberRepository.findByName(waitingRequest.getName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Waiting waiting = new Waiting(waitingRequest.getDate(),
                time,
                theme,
                member);

        waitingRepository.save(waiting);

        return new WaitingResponse(waiting.getId(), waitingRequest.getName(), waiting.getTheme().getName(), waiting.getDate(), waiting.getTime().getValueByString());
    }

    public void deleteById(Long id) {
        waitingRepository.deleteById(id);
    }
}
