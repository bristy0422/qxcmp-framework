package com.qxcmp.framework.mall;

import com.qxcmp.framework.exception.FinanceException;
import com.qxcmp.framework.exception.NoBalanceException;
import com.qxcmp.framework.finance.Wallet;
import com.qxcmp.framework.finance.WalletService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRecordService transferRecordService;

    private final WalletService walletService;

    @Override
    public Optional<TransferRecord> transfer(String from, String to, int fee) throws FinanceException {
        return transfer(from, to, fee, "CNY");
    }

    @Override
    public Optional<TransferRecord> transfer(String from, String to, int fee, String feeType) throws FinanceException {
        checkArgument(StringUtils.equals(feeType, "CNY"), "Current only support CNY fee type");
        Optional<Wallet> sourceWallet = walletService.getByUserId(from);
        checkState(sourceWallet.isPresent(), "Source wallet not exist");
        Optional<Wallet> targetWallet = walletService.getByUserId(to);
        checkState(targetWallet.isPresent(), "Target wallet not exist");

        if (sourceWallet.get().getBalance() < 0) {
            throw new NoBalanceException("Source balance not enough");
        }

        TransferRecord transferRecord = transferRecordService.next();

        transferRecord.setSourceUserId(from);
        transferRecord.setTargetUserId(to);
        transferRecord.setFee(fee);
        transferRecord.setFeeType(feeType);

        try {
            walletService.update(sourceWallet.get().getId(), wallet -> wallet.setBalance(wallet.getBalance() - fee));
            walletService.update(targetWallet.get().getId(), wallet -> wallet.setBalance(wallet.getBalance() + fee));
            transferRecord.setStatus(TransferRecord.Status.SUCCESS);
        } catch (Exception e) {
            transferRecord.setStatus(TransferRecord.Status.FAILED);
            transferRecord.setComments(e.getMessage());
        }

        return transferRecordService.create(() -> transferRecord);
    }
}
