package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select * from bookings where booker_id = ?1 and " +
            "end_date < current_timestamp order by start_date desc", nativeQuery = true)
    Page<Booking> findByBookerIdAndEndIsBefore(Long bookerId, Pageable pageable);

    @Query(value = "select * from bookings where booker_id  = ?1 " +
            "and status = ?2 order by start_date desc", nativeQuery = true)
    Page<Booking> findByBookerIdAndStatus(Long bookerId, String status, Pageable pageable);


    Page<Booking> findByBooker_IdOrderByStartDesc(Long bookerId, Pageable pageable);

    @Query(value = "select * from bookings where booker_id = ?1 and start_date > current_timestamp " +
            "order by start_date desc", nativeQuery = true)
    Page<Booking> findByBookerIdAndStartIsAfter(Long bookerId, Pageable pageable);

    @Query(value = "select * from bookings where booker_id = ?1 and start_date <= current_timestamp " +
            "and end_date >= current_timestamp order by start_date desc", nativeQuery = true)
    Page<Booking> findByBookerIdCurrent(Long bookerId, Pageable pageable);

    @Query(value = "select b from Booking b where b.item.owner.id  = :ownerId " +
            "and b.status = :status order by b.start desc")
    Page<Booking> findByOwnerIdStatus(Long ownerId, Status status, Pageable pageable);

    @Query(value = "select b from Booking b where b.item.owner.id = :ownerId " +
            "and b.end < current_timestamp order by b.start desc")
    Page<Booking> findByOwnerIdPast(Long ownerId, Pageable pageable);

    @Query(value = "select b from Booking b where b.item.owner.id = :ownerId " +
            "order by b.start desc")
    Page<Booking> findByOwnerId(Long ownerId, Pageable pageable);

    @Query(value = "select b from Booking b where b.item.owner.id = :ownerId " +
            "and b.start > current_timestamp order by b.start desc")
    Page<Booking> findByOwnerIdFuture(Long ownerId,Pageable pageable);

    @Query(value = "select b from Booking b where b.item.owner.id = :ownerId and b.start <= current_timestamp " +
            "and b.end >= current_timestamp order by b.start desc")
    Page<Booking> findByOwnerIdCurrent(Long ownerId, Pageable pageable);

    @Query(value = "select * from bookings where item_id = ?1 and start_date < current_timestamp " +
            "and status = 'APPROVED' order by end_date desc", nativeQuery = true)
    List<Booking> findLastBooking(Long itemId);

    @Query(value = "select * from bookings where item_id = ?1 and start_date > current_timestamp " +
            "and status = 'APPROVED' order by start_date", nativeQuery = true)
    List<Booking> findNextBooking(Long itemId);

    @Query(value = "select * from bookings where booker_id = ?1 and item_id = ?2 " +
            "and status = 'APPROVED' and end_date < current_timestamp", nativeQuery = true)
    List<Booking> findBookingForComment(Long bookerId, Long itemId);

}
