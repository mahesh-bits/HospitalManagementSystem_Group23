package com.ss.notification.service.repositories;

import com.ss.notification.service.entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByRecipient(String recipient, Pageable pageable);

    Page<Notification> findByType(String type, Pageable pageable);

    Page<Notification> findByStatus(String status, Pageable pageable);
}
