package com.main.traveltour.service.staff;

import com.main.traveltour.entity.Invoices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface InvoicesService {
    Page<Invoices> findAllBySearchTerm(String searchTerm, Pageable pageable);

    Invoices findByInvoiceId(String invoiceId);
}
