package com.example.booking.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.booking.base.constant.RedisKey;
import com.example.booking.base.constant.ReturnCodeEnum;
import com.example.booking.base.service.cache.RedisService;
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
    private final RedisService redisService;

    /**
     * 1. @Lock entity
     * 2. @Transactional
     */
    // @Transactional(readOnly = false)
    @Override
    public ActionResult booking(BookingTicketInDto input) {
        Long userId = 1L;
        ActionResult result = new ActionResult();
        try {
            Integer amount = null;
            Integer soldAmount = null;
            // UserEntity user = userRepos.findById(userId).get();
            Boolean ticketOnSell = redisService.setIfAbsent(RedisKey.TICKET_ON_SELL + ":" + input.getTicketId(), "id", input.getTicketId());
            if(ticketOnSell) {
                Optional<TicketEntity> opTicket = ticketRepos
                .findById(input.getTicketId());
                if(opTicket.isPresent()) {
                    TicketEntity ticket = opTicket.get();
                    amount = ticket.getAmount();
                    soldAmount = ticket.getSoldAmount();
                    redisService.set(RedisKey.TICKET_ON_SELL + ":" + input.getTicketId(), "amount", amount);
                    redisService.set(RedisKey.TICKET_ON_SELL + ":" + input.getTicketId(), "soldAmount", soldAmount); 
                } else {
                    redisService.set(RedisKey.TICKET_ON_SELL + ":" + input.getTicketId(), "id", null);
                    result.setReturnCode(ReturnCodeEnum.TICKET_NOT_EXIST);
                }
            } else {
                Long ticketId = Long.valueOf(redisService.get(RedisKey.TICKET_ON_SELL + ":" + input.getTicketId(), "id").toString());
                if(ticketId == null) {
                    result.setReturnCode(ReturnCodeEnum.TICKET_NOT_EXIST);
                } else {
                    Object obAmount = Optional.ofNullable(redisService.get(RedisKey.TICKET_ON_SELL + ":" + input.getTicketId(), "amount")).orElse(null);
                    Object obSoldAmount = Optional.ofNullable(redisService.get(RedisKey.TICKET_ON_SELL + ":" + input.getTicketId(), "soldAmount")).orElse(null);;
                    if(obAmount != null && obSoldAmount != null) {
                        amount = (Integer) obAmount;
                        soldAmount = (Integer) obSoldAmount;
                    } else {
                        Thread.sleep(100);
                        amount = (Integer) redisService.get(RedisKey.TICKET_ON_SELL + ":" + input.getTicketId(), "amount");
                        soldAmount = (Integer) redisService.get(RedisKey.TICKET_ON_SELL + ":" + input.getTicketId(), "soldAmount");
                    }
                }
            }
            if(soldAmount < amount) {
                redisService.increment(RedisKey.TICKET_ON_SELL + ":" + input.getTicketId(), "soldAmount");
                result.setReturnCode(ReturnCodeEnum.OK);
            }

                            //     Integer soldAmount = ticket.getSoldAmount() + 1;
                //     TicketUserEntity ticketUser = new TicketUserEntity();
                //     ticketUser.setTicket(ticket);
                //     ticketUser.setUser(user);
                //     ticketUser.setTicketCode(ticket.getCode() + "-" + soldAmount.toString());
                //     ticketUserRepos.save(ticketUser);
                //     ticket.setSoldAmount(soldAmount);
                //     ticketRepos.save(ticket);
                //     result.setReturnCode(ReturnCodeEnum.OK);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
            result.setReturnCode(ReturnCodeEnum.SOMETHING_WRONG);

        }
        return result;
    }

}
