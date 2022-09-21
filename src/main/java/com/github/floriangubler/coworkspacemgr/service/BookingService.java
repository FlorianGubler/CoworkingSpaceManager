package com.github.floriangubler.coworkspacemgr.service;

import com.github.floriangubler.coworkspacemgr.exception.BookingNotFoundException;
import com.github.floriangubler.coworkspacemgr.entity.BookingEntity;
import com.github.floriangubler.coworkspacemgr.repository.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingService {

    private final BookingRepository repository;
    private final String ADMINROLE = "ROLE_ADMIN";

    BookingService(BookingRepository repository) {
        this.repository = repository;
    }

    public List<BookingEntity> findUserBookings(UUID memberID) {
        log.info("Executing find all User Bookings ...");
        return repository.findAllByMemberId(memberID);
    }

    public List<BookingEntity> findBookings(Boolean anonymise, UUID userid) {
        log.info("Executing find all Bookings ...");
        List<BookingEntity> result = repository.findAll();
        if(anonymise){
            for(BookingEntity booking : result){
                if(!booking.getMember().getId().equals(userid)) {
                    booking.setMember(null);
                    booking.setId(null);
                }
            }
        }
        return result;
    }

    public BookingEntity create(BookingEntity booking) {
        log.info("Executing create booking with id " + booking.getId() + " ...");
        return repository.save(booking);
    }

    public BookingEntity update(BookingEntity updatedBooking, UUID bookingid) {
        if(repository.findById(bookingid).isPresent()){
            log.info("Executing update booking with id " + bookingid + " ...");
            updatedBooking.setId(bookingid);
            return repository.save(updatedBooking);
        } else{
            throw new BookingNotFoundException("Booking with given id not found");
        }
    }

    public void delete(UUID bookingid, Authentication authentication) {
        Optional<BookingEntity> delbooking = repository.findById(bookingid);
        if(delbooking.isPresent()){
            //Auth User is owner of Booking or is Admin
            if(delbooking.get().getMember().getId().equals(UUID.fromString(authentication.getName())) || authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()).contains(ADMINROLE)){
                log.info("Executing delete Booking with id " + bookingid + " ...");
                repository.deleteById(bookingid);
            }
        } else{
            throw new BookingNotFoundException("Requested Booking to delete not found");
        }
    }

}
