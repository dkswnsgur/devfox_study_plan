package com.example.SpringBatchTutoria.job.core.domain.orders;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository  extends JpaRepository<Orders, Integer> {
}
