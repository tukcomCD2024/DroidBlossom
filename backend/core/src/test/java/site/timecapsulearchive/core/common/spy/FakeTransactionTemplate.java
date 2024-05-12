package site.timecapsulearchive.core.common.spy;

import static org.mockito.Mockito.spy;

import java.util.function.Consumer;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

public class FakeTransactionTemplate extends TransactionTemplate {

    public static FakeTransactionTemplate spied() {
        return spy(new FakeTransactionTemplate());
    }

    @Override
    public void executeWithoutResult(Consumer<TransactionStatus> action)
        throws TransactionException {
        action.accept(new SimpleTransactionStatus());
    }
}
