package com.github.floriangubler.coworkspacemgr.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BookingEntityReq implements Serializable {

    private UUID memberId;

    private Date date;

    private BookingTime time;

    private BookingStatus status;
}
