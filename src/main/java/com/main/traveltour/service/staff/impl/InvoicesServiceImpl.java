package com.main.traveltour.service.staff.impl;

import com.main.traveltour.entity.Invoices;
import com.main.traveltour.repository.InvoicesRepository;
import com.main.traveltour.service.staff.InvoicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InvoicesServiceImpl implements InvoicesService {

    @Autowired
    private InvoicesRepository repo;

    @Override
    public Page<Invoices> findAllBySearchTerm(String searchTerm, Pageable pageable) {
        return repo.findAllBySearchTerm(searchTerm, pageable);
    }

    @Override
    public Invoices findByInvoiceId(String invoiceId) {
        return repo.findById(invoiceId);
    }
}
