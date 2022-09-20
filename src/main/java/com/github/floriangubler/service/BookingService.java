package com.github.floriangubler.service;

import com.github.floriangubler.exception.BookingNotFoundException;
import com.github.floriangubler.model.BookingEntity;
import com.github.floriangubler.model.BookingStatus;
import com.github.floriangubler.repository.BookingRepository;
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

    public List<BookingEntity> findBookings(Boolean anonymise) {
        log.info("Executing find all User Bookings ...");
        List<BookingEntity> result = repository.findAll();
        if(anonymise){
            for(BookingEntity booking : result){
                booking.setMember(null);
                booking.setId(null);
            }
        }
        return result;
    }

    public Optional<BookingEntity> loadOne(UUID gameId) {
        log.info("Executing find game with id " + gameId + " ...");
        return repository.findById(gameId);
    }

    public BookingEntity create(BookingEntity booking, BookingStatus status) {
        log.info("Executing create game with id " + booking.getId() + " ...");
        return repository.save(game);
    }

    public BookingEntity update(BookingEntity updatedGame, BookingEntity bookingid) {
        if(repository.findById(updatedGame.getId()).isPresent()){
            log.info("Executing update game with id " + updatedGame.getId() + " ...");
            return repository.save(updatedGame);
        } else{
            throw new IllegalArgumentException("Object with given id not found");
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
