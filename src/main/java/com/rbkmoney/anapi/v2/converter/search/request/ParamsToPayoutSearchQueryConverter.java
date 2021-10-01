package com.rbkmoney.anapi.v2.converter.search.request;

import com.rbkmoney.anapi.v2.exception.BadRequestException;
import com.rbkmoney.damsel.domain.PaymentInstitutionAccount;
import com.rbkmoney.damsel.domain.PayoutToolInfo;
import com.rbkmoney.damsel.domain.RussianBankAccount;
import com.rbkmoney.damsel.domain.WalletInfo;
import com.rbkmoney.magista.PayoutSearchQuery;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

import static com.rbkmoney.anapi.v2.util.ConverterUtil.fillCommonParams;

@Component
public class ParamsToPayoutSearchQueryConverter {

    public PayoutSearchQuery convert(String partyID,
                                     OffsetDateTime fromTime,
                                     OffsetDateTime toTime,
                                     Integer limit,
                                     List<String> shopIDs,
                                     String payoutID,
                                     String payoutToolType,
                                     String continuationToken) {
        return new PayoutSearchQuery()
                .setCommonSearchQueryParams(
                        fillCommonParams(fromTime, toTime, limit, partyID, shopIDs, continuationToken))
                .setPayoutId(payoutID)
                .setPayoutType(payoutToolType != null ? mapToDamselPayoutToolInfo(payoutToolType) : null);
    }

    private PayoutToolInfo mapToDamselPayoutToolInfo(String payoutToolType) {
        var payoutToolInfo = new PayoutToolInfo();
        switch (payoutToolType) {
            case "PayoutAccount" -> payoutToolInfo
                    .setRussianBankAccount(new RussianBankAccount());//TODO: Russian or International?
            case "Wallet" -> payoutToolInfo.setWalletInfo(new WalletInfo());
            case "PaymentInstitutionAccount" -> payoutToolInfo
                    .setPaymentInstitutionAccount(new PaymentInstitutionAccount());
            default -> throw new BadRequestException(
                    String.format("PayoutToolType %s cannot be processed", payoutToolType));
        }

        return payoutToolInfo;
    }
}
