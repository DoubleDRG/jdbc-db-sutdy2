package study.buddyBook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import study.buddyBook.domain.Member;
import study.buddyBook.repository.MemberRepositoryV3;

import java.sql.SQLException;

//트랜잭션 매니저
//특정기술이 아닌, 트랜잭션 매니저에 의존한다.
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1
{
    private final PlatformTransactionManager transactionManager;
    private final MemberRepositoryV3 memberRepository;

    public void accountTransfer(String fromId, String toId, int money)
    {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try
        {
            bizLogic(fromId, toId, money);
            transactionManager.commit(status);
        }
        catch (Exception e)
        {
            transactionManager.rollback(status);
            throw new IllegalStateException(e);
        }
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException
    {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);
        validation(toMember);
        memberRepository.update(fromId, fromMember.getMoney() - money);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember)
    {
        if(toMember.getMemberId().equals("bad"))
        {
            throw new IllegalStateException("이체 중 예외 발생");
        }
    }
}
