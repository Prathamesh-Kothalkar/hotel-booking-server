package dev.prathamesh.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import dev.prathamesh.model.BookingModel;
import dev.prathamesh.model.RoomModel;
import dev.prathamesh.types.BookingStatus;
import dev.prathamesh.types.RoomSearchRequest;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class RoomSpecification {

    public static Specification<RoomModel> search(
            RoomSearchRequest request) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            /*
             * HOTEL LOCATION
             */
            if (request.getLocation() != null &&
                !request.getLocation().isBlank()) {

                predicates.add(
                    cb.like(
                        cb.lower(
                            root.get("hotel").get("location")
                        ),
                        "%" + request.getLocation().toLowerCase() + "%"
                    )
                );
            }

            /*
             * HOTEL NAME
             */
            if (request.getHotelName() != null &&
                !request.getHotelName().isBlank()) {

                predicates.add(
                    cb.like(
                        cb.lower(
                            root.get("hotel").get("name")
                        ),
                        "%" + request.getHotelName().toLowerCase() + "%"
                    )
                );
            }

            /*
             * ROOM TYPE
             */
            if (request.getRoomType() != null &&
                !request.getRoomType().isBlank()) {

                predicates.add(
                    cb.equal(
                        cb.lower(root.get("roomType")),
                        request.getRoomType().toLowerCase()
                    )
                );
            }

            /*
             * MIN PRICE
             */
            if (request.getMinPrice() != null) {

                predicates.add(
                    cb.greaterThanOrEqualTo(
                        root.get("price"),
                        request.getMinPrice()
                    )
                );
            }

            /*
             * MAX PRICE
             */
            if (request.getMaxPrice() != null) {

                predicates.add(
                    cb.lessThanOrEqualTo(
                        root.get("price"),
                        request.getMaxPrice()
                    )
                );
            }

            /*
             * ROOM MUST HAVE ENOUGH BEDS
             */
            if (request.getGuests() != null) {

                predicates.add(
                    cb.greaterThanOrEqualTo(
                        root.get("noOfBeds"),
                        request.getGuests()
                    )
                );
            }

            /*
             * DATE AVAILABILITY
             */
            if (request.getCheckIn() != null &&
                request.getCheckOut() != null) {

                Subquery<Long> subquery =
                        query.subquery(Long.class);

                Root<BookingModel> booking =
                        subquery.from(BookingModel.class);

                subquery.select(
                    cb.literal(1L)
                );

                Predicate sameRoom =
                    cb.equal(
                        booking.get("room"),
                        root
                    );

                Predicate overlapping =
                    cb.and(

                        cb.lessThan(
                            booking.get("checkInDate"),
                            request.getCheckOut()
                        ),

                        cb.greaterThan(
                            booking.get("checkOutDate"),
                            request.getCheckIn()
                        )
                    );

                Predicate activeBooking =
                    booking.get("status")
                           .in(
                               BookingStatus.PENDING,
                               BookingStatus.CONFIRMED
                           );

                subquery.where(
                    cb.and(
                        sameRoom,
                        overlapping,
                        activeBooking
                    )
                );

                predicates.add(
                    cb.not(
                        cb.exists(subquery)
                    )
                );
            }

            /*
             * ROOM STATUS
             */
            predicates.add(
                cb.equal(
                    root.get("status"),
                    dev.prathamesh.types.RoomStatus.AVAILABLE
                )
            );

            return cb.and(
                predicates.toArray(new Predicate[0])
            );
        };
    }
}