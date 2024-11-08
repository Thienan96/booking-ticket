package com.example.booking.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.booking.base.constant.ReturnCodeEnum;
import com.example.booking.dto.base.ActionResult;
import com.example.booking.dto.indto.BookingTicketInDto;
import com.example.booking.entity.TicketEntity;
import com.example.booking.entity.TicketUserEntity;
import com.example.booking.entity.UserEntity;
import com.example.booking.repository.TicketRepos;
import com.example.booking.repository.TicketUserRepos;
import com.example.booking.repository.UserRepos;
import com.example.booking.service.UserService;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@SuppressWarnings("all")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {
    private final TicketUserRepos ticketUserRepos;
    private final UserRepos userRepos;
    private final TicketRepos ticketRepos;

    /**
     * 1. @Lock entity
     * 2. @Transactional
     */
    @Transactional(readOnly = false)
    @Override
    public ActionResult booking(BookingTicketInDto input) {
        Long userId = 1L;
        ActionResult result = new ActionResult();
        try {
            UserEntity user = userRepos.findById(userId).get();
            Optional<TicketEntity> opTicket = ticketRepos
                    .findById(input.getTicketId());
            if(opTicket.isPresent()) {
                TicketEntity ticket = opTicket.get();
                if(ticket.getSoldAmount() < ticket.getAmount()) {
                    Integer soldAmount = ticket.getSoldAmount() + 1;
                    TicketUserEntity ticketUser = new TicketUserEntity();
                    ticketUser.setTicket(ticket);
                    ticketUser.setUser(user);
                    ticketUser.setTicketCode(ticket.getCode() + "-" + soldAmount.toString());
                    ticketUserRepos.save(ticketUser);
                    ticket.setSoldAmount(soldAmount);
                    ticketRepos.save(ticket);
                    result.setReturnCode(ReturnCodeEnum.OK);
                } else {
                    result.setReturnCode(ReturnCodeEnum.TICKET_SOLD_OUT);
                }
            } else {
                result.setReturnCode(ReturnCodeEnum.TICKET_CATEGORY_NOT_EXIST);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
            result.setReturnCode(ReturnCodeEnum.SOMETHING_WRONG);

        }
        return result;
    }

}
