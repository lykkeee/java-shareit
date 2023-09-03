package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select * from bookings where booker_id = ?1 and " +
            "end_date < current_timestamp order by start_date desc", nativeQuery = true)
    List<Booking> findByBookerIdAndEndIsBefore(Long bookerId);

    @Query(value = "select * from bookings where booker_id  = ?1 " +
            "and status = ?2 order by start_date desc", nativeQuery = true)
    List<Booking> findByBookerIdAndStatus(Long bookerId, String status);


    List<Booking> findByBooker_IdOrderByStartDesc(Long bookerId);

    @Query(value = "select * from bookings where booker_id = ?1 and start_date > current_timestamp " +
            "order by start_date desc", nativeQuery = true)
    List<Booking> findByBookerIdAndStartIsAfter(Long bookerId);

    @Query(value = "select * from bookings where booker_id = ?1 and start_date <= current_timestamp " +
            "and end_date >= current_timestamp order by start_date desc", nativeQuery = true)
    List<Booking> findByBookerIdCurrent(Long bookerId);

    @Query(value = "select b from Booking b where b.item.owner.id  = :ownerId " +
            "and b.status = :status order by b.start desc")
    List<Booking> findByOwnerIdStatus(Long ownerId, Status status);

    @Query(value = "select b from Booking b where b.item.owner.id = :ownerId " +
            "and b.end < current_timestamp order by b.start desc")
    List<Booking> findByOwnerIdPast(Long ownerId);

    @Query(value = "select b from Booking b where b.item.owner.id = :ownerId " +
            "order by b.start desc")
    List<Booking> findByOwnerId(Long ownerId);

    @Query(value = "select b from Booking b where b.item.owner.id = :ownerId " +
            "and b.start > current_timestamp order by b.start desc")
    List<Booking> findByOwnerIdFuture(Long ownerId);

    @Query(value = "select b from Booking b where b.item.owner.id = :ownerId and b.start <= current_timestamp " +
            "and b.end >= current_timestamp order by b.start desc")
    List<Booking> findByOwnerIdCurrent(Long ownerId);

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
