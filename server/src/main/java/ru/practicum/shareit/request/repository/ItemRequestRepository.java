package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query(value = "select r from ItemRequest r where r.requestor.id != :requestorId")
    Page<ItemRequest> findByNotInRequestorId(Long requestorId, Pageable pageable);

    List<ItemRequest> findRequestByRequestorIdOrderByCreatedDesc(Long requestorId);

}
