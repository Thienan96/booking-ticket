package com.example.booking.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.booking.base.constant.RedisKey;
import com.example.booking.base.constant.ReturnCodeEnum;
import com.example.booking.base.security.JwtUtils;
import com.example.booking.base.service.cache.RedisService;
import com.example.booking.dto.base.ActionResult;
import com.example.booking.dto.indto.BookingTicketInDto;
import com.example.booking.dto.indto.CreateUserDto;
import com.example.booking.dto.indto.UserLoginInDto;
import com.example.booking.dto.outdto.JwtResponse;
import com.example.booking.entity.ClientEntity;
import com.example.booking.entity.RoleEntity;
import com.example.booking.entity.TicketEntity;
import com.example.booking.entity.TicketUserEntity;
import com.example.booking.entity.UserEntity;
import com.example.booking.repository.ClientRepos;
import com.example.booking.repository.RoleRepos;
import com.example.booking.repository.TicketRepos;
import com.example.booking.repository.TicketUserRepos;
import com.example.booking.repository.UserRepos;
import com.example.booking.service.UserService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

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
    private final PasswordEncoder passwordEncoder;
    private final RoleRepos roleRepos;
    private final ClientRepos clientRepos;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
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

    @Override
    public ActionResult createUser(CreateUserDto input) {
        ActionResult result = new ActionResult();
        try {
            ClientEntity client = new ClientEntity();
            client.setName(input.getName());
            client.setPassword(passwordEncoder.encode(input.getPassword()));
            client.setEmail(input.getEmail());
            List<Long> roles = input.getRoles();
            if(input.getRoles() != null) {
                Set<RoleEntity> roleEntities = new HashSet<>();
                for(Long roleId : roles) {
                    RoleEntity roleEntity = roleRepos.findById(roleId).get();
                    roleEntities.add(roleEntity);
                }
                client.setRoles(roleEntities);
                clientRepos.save(client);
            } else {
                result.setReturnCode(ReturnCodeEnum.SOMETHING_WRONG);
                return result;
            }
            result.setReturnCode(ReturnCodeEnum.OK);
        } catch (Exception e) {
            System.out.println(e);
            result.setReturnCode(ReturnCodeEnum.SOMETHING_WRONG);
        }
        return result;
        
    }

    @Override
    public ActionResult getCurrentUserInfor() {
        ActionResult result = new ActionResult();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            result.setData(authentication.getPrincipal());
            result.setReturnCode(ReturnCodeEnum.OK);
        } catch (Exception e) {
            System.out.println(e);
            result.setReturnCode(ReturnCodeEnum.SOMETHING_WRONG);
        }
        return result;
        
    }

    @Override
    public ActionResult login(UserLoginInDto input){
        ActionResult result = new ActionResult();
        try {
            final Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                input.getUserName(), input.getPassword()));
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            if(auth.isAuthenticated()) {
                String accessToken = jwtUtils.generateToken(auth, null);
                result.setReturnCode(ReturnCodeEnum.OK);
				result.setData(new JwtResponse(accessToken, redisService.getTokenExpireTime()));
            }
    } catch (DisabledException e) {
			String errorBody = "{\"error\":\"USER_DISABLED\"}";
			result.setReturnCode(ReturnCodeEnum.USER_DISABLED);
			result.setData(errorBody);
		} catch (BadCredentialsException e) {
			String errorBody = "{\"error\":\"INVALID_CREDENTIALS\"}";
			result.setReturnCode(ReturnCodeEnum.INVALID_CREDENTIALS);
			result.setData(errorBody);
		}
        return result;
        
    }

    @Override
    public ActionResult testUser(){
        ActionResult result = new ActionResult();
        ClientEntity client = new ClientEntity();
        client = clientRepos.findById(1L).get();
        // client.getRoles()                  
        result.setData(client);
        return result;
    }

}
