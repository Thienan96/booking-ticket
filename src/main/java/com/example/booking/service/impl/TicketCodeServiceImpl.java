package com.example.booking.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.booking.base.constant.ReturnCodeEnum;
import com.example.booking.dto.base.ActionResult;
import com.example.booking.dto.indto.BookingTicketInDto;
import com.example.booking.dto.indto.TicketInDto;
import com.example.booking.entity.TicketCodeEntity;
import com.example.booking.entity.TicketEntity;
import com.example.booking.repository.TicketCodeRepos;
import com.example.booking.repository.TicketRepos;
import com.example.booking.service.TicketService;
import com.example.booking.service.TicketCodeService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.stream.IntStream;

@Component
@SuppressWarnings("all")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TicketCodeServiceImpl implements TicketCodeService {
    private final TicketRepos ticketRespos;
    private final TicketCodeRepos ticketCodeRepos;
    

    @Transactional
    @Override
    public ActionResult genTicketCode(BookingTicketInDto input) {
        ActionResult result = new ActionResult();
        try {
            Optional<TicketEntity> opTicket = ticketRespos
                    .findById(input.getTicketId());
            if (opTicket.isPresent()) {
                TicketEntity ticket = opTicket.get();
                Integer totalAmount = ticket.getAmount();
                Integer batchSize = 1000;
                if(totalAmount < batchSize) {
                    batchSize = totalAmount;
                }
                final Integer finalBatchSize = batchSize;
                List<TicketCodeEntity> ticketCodeEntities = new ArrayList<>(finalBatchSize);
                //
                IntStream.rangeClosed(1, totalAmount).forEach(i -> {
                    TicketCodeEntity ticketCode = new TicketCodeEntity();
                    ticketCode.setTicket(ticket);
                    ticketCode.setCode(ticket.getCode() + "-" + i);
                    ticketCodeEntities.add(ticketCode);
                    // Insert batch when we reach the batch size
                    if (i % finalBatchSize == 0) {
                        ticketCodeRepos.saveAll(ticketCodeEntities);
                        ticketCodeRepos.flush();
                        ticketCodeEntities.clear();  // Clear the batch
                    }
                });
        
                // Save remaining products if any
                if (!ticketCodeEntities.isEmpty()) {
                    ticketCodeRepos.saveAll(ticketCodeEntities);
                    ticketCodeRepos.flush();
                }
                //
                result.setReturnCode(ReturnCodeEnum.OK);
            } else {
                result.setReturnCode(ReturnCodeEnum.TICKET_CATEGORY_NOT_EXIST);
            }
        } catch (Exception e) {
            System.out.println(e);
            result.setReturnCode(ReturnCodeEnum.SOMETHING_WRONG);
        }
        return result;
    }

}
