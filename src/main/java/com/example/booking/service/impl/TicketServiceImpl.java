package com.example.booking.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.booking.base.constant.ReturnCodeEnum;
import com.example.booking.base.service.cache.RedisService;
import com.example.booking.dto.base.ActionResult;
import com.example.booking.dto.indto.TicketInDto;
import com.example.booking.entity.TicketEntity;
import com.example.booking.repository.TicketRepos;
import com.example.booking.service.TicketService;

import lombok.RequiredArgsConstructor;

@Component
@SuppressWarnings("all")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TicketServiceImpl implements TicketService {
    private final TicketRepos ticketRepos;
    private final RedisService redisServive;

    @Override
    public ActionResult addTicket(TicketInDto ticketInDto) {
        ActionResult result = new ActionResult();
        try {
            TicketEntity entity = new TicketEntity();
            entity.setName(ticketInDto.getName());
            entity.setCode(ticketInDto.getCode());
            entity.setAmount(1000000);
            entity.setSoldAmount(0);
            entity.setValidDate(ticketInDto.getValidDate());
            result.setData(entity);
            ticketRepos.save(entity);
            result.setReturnCode(ReturnCodeEnum.OK);
        } catch (Exception e) {
            System.out.println(e);
            result.setReturnCode(ReturnCodeEnum.SOMETHING_WRONG);
        }
        return result;
    }

    @Override
    public ActionResult testRedis() {
        ActionResult result = new ActionResult();
        try {
            redisServive.set("user:session:123", "session_token_value");
            redisServive.set("user:profile:123", "name", "John Doe");
            result.setReturnCode(ReturnCodeEnum.OK);
        } catch (Exception e) {
            System.out.println(e);
        }

        return result;
    }

}
