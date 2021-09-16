package com.rbkmoney.anapi.v2.converter.search.response;

import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.magista.InvoiceStatus;
import com.rbkmoney.magista.StatInvoice;
import com.rbkmoney.openapi.anapi_v2.model.Invoice;
import com.rbkmoney.openapi.anapi_v2.model.InvoiceLine;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Component
public class StatInvoiceToInvoiceConverter {

    public Invoice convert(StatInvoice invoice) {
        Invoice result = new Invoice()
                .amount(invoice.getAmount())
                .createdAt(TypeUtil.stringToInstant(invoice.getCreatedAt()).atOffset(ZoneOffset.UTC))
                .currency(invoice.getCurrencySymbolicCode())
                .externalID(invoice.getExternalId())
                .cart(invoice.getCart() != null
                        ? invoice.getCart().getLines().stream().map(invoiceLine -> new InvoiceLine()
                                .cost(invoiceLine.getQuantity() * invoiceLine.getPrice().getAmount())
                                .price(invoiceLine.getPrice().getAmount())
                                .product(invoiceLine.getProduct())
                        //.taxMode() //TODO: Where is the value?
                ).collect(Collectors.toList()) : null)
                .description(invoice.getDescription())
                .dueDate(TypeUtil.stringToInstant(invoice.getDue()).atOffset(ZoneOffset.UTC))
                .id(invoice.getId())
                .product(invoice.getProduct())
                .shopID(invoice.getShopId());

        fillStatusInfo(result, invoice.getStatus());
        return result;
    }

    private void fillStatusInfo(Invoice invoice, InvoiceStatus status) {
        if (status.isSetFulfilled()) {
            invoice.setStatus(Invoice.StatusEnum.FULFILLED);
            invoice.setReason(status.getFulfilled().getDetails());
            return;
        }

        if (status.isSetPaid()) {
            invoice.setStatus(Invoice.StatusEnum.PAID);
            return;
        }

        if (status.isSetUnpaid()) {
            invoice.setStatus(Invoice.StatusEnum.UNPAID);
            return;
        }

        if (status.isSetCancelled()) {
            invoice.setStatus(Invoice.StatusEnum.CANCELLED);
            invoice.setReason(status.getCancelled().getDetails());
            return;
        }

        throw new IllegalArgumentException(
                String.format("Invoice status %s cannot be processed", status));
    }

}
