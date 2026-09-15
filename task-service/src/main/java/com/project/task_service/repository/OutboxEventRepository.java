package com.project.task_service.repository;

import com.project.task_service.entity.OutboxEvent;
import com.project.task_service.enums.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    /**
     * SQL explain:
     * UPDLOCK - Đọc nhưng row để chuẩn bị update
     * READPAST - Bỏ qua event khi bị lock thay vì chờ
     * ROWLOCK - Ưu tiên row-level lock thay vì lock phạm vi lớn hơn
     *
     * Lưu ý:
     * Query này chỉ SELECT + LOCK.
     * Việc PENDING -> PROCESSING được thực hiện
     * trong OutboxClaimService cùng transaction.
     *
     * @return Lấy 1 batch PENDING và chuyển thành PROCESSING một cách atmomic
     */
    @Query(value = """
        SELECT TOP 100 *
        FROM outbox_events WITH(UPDLOCK, READPAST, ROWLOCK)
        WHERE status = 'PENDING'
            AND (
                next_retry_at IS NULL
                OR next_retry_at <= :now
            )
        ORDER BY id
    """, nativeQuery = true)
    List<OutboxEvent> findPendingForUpdate(@Param("now") LocalDateTime now);


    // Reclaim stuck event
    @Query(value = """
    SELECT TOP 100 *
    FROM outbox_events WITH(UPDLOCK, READPAST, ROWLOCK)
    WHERE status = 'PROCESSING'
      AND claimed_at < :threshold
    ORDER BY id
""", nativeQuery = true)
    List<OutboxEvent> findStuckEvents(@Param("threshold") LocalDateTime threshold);
}
