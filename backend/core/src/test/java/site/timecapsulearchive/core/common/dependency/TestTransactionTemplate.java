package site.timecapsulearchive.core.common.dependency;

import static org.mockito.Mockito.spy;

import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class TestTransactionTemplate extends TransactionTemplate {

    public static TestTransactionTemplate spied() {
        return spy(new TestTransactionTemplate());
    }

    @Override
    public <T> T execute(TransactionCallback<T> action) throws TransactionException {
        return action.doInTransaction(new SimpleTransactionStatus());
    }

}
