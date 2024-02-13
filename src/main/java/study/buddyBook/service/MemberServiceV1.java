package study.buddyBook.service;

import lombok.RequiredArgsConstructor;
import study.buddyBook.domain.Member;
import study.buddyBook.repository.MemberRepositoryV1;

@RequiredArgsConstructor
public class MemberServiceV1
{
    private final MemberRepositoryV1 memberRepository;

    public void accountTransfer(String fromId, String toId, int money)
    {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember)
    {
        if (toMember.getMemberId().equals("bad"))
        {
            throw new IllegalStateException("이체 중 예외 발생");
        }
    }
}
