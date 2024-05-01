package com.kernelsquare.memberapi.domain.coffeechat.service;

import com.kernelsquare.core.common_response.error.code.CoffeeChatErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmongodb.domain.coffeechat.repository.MongoChatMessageReader;
import com.kernelsquare.domainmysql.domain.coffeechat.command.CoffeeChatCommand;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import com.kernelsquare.domainmysql.domain.coffeechat.info.CoffeeChatInfo;
import com.kernelsquare.domainmysql.domain.coffeechat.repository.CoffeeChatStore;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberReader;
import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;
import com.kernelsquare.domainmysql.domain.reservation.repository.ReservationReader;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.coffeechat.dto.*;
import com.kernelsquare.memberapi.domain.coffeechat.validation.CoffeeChatValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoffeeChatService {
	private final CoffeeChatStore coffeeChatStore;
	private final MongoChatMessageReader mongoChatMessageReader;
	private final ReservationReader reservationReader;
	private final MemberReader memberReader;

	@Transactional
	public CreateCoffeeChatRoomResponse createCoffeeChatRoom(CreateCoffeeChatRoomRequest createCoffeeChatRoomRequest) {
		ChatRoom chatRoom = CreateCoffeeChatRoomRequest.toEntity(createCoffeeChatRoomRequest);

		ChatRoom saveChatRoom = coffeeChatStore.store(chatRoom);

		return CreateCoffeeChatRoomResponse.from(saveChatRoom);
	}

	@Transactional
	public EnterCoffeeChatRoomResponse enterCoffeeChatRoom(EnterCoffeeChatRoomRequest enterCoffeeChatRoomRequest,
		MemberAdapter memberAdapter) {

		Reservation reservation = reservationReader.find(enterCoffeeChatRoomRequest.reservationId());

		ChatRoom chatRoom = reservation.getChatRoom();

		CoffeeChatValidation.validateChatRoom(chatRoom);

		switch (CoffeeChatValidation.validatePermission(reservation, memberAdapter)) {
			case MENTOR -> {
				return mentorEnter(enterCoffeeChatRoomRequest, chatRoom);
			}
			case MENTEE -> {
				return menteeEnter(enterCoffeeChatRoomRequest, chatRoom);
			}
			default -> throw new BusinessException(CoffeeChatErrorCode.AUTHORITY_NOT_VALID);
		}
	}

	public EnterCoffeeChatRoomResponse mentorEnter(EnterCoffeeChatRoomRequest enterCoffeeChatRoomRequest,
		ChatRoom chatRoom) {

		chatRoom.activateRoom(enterCoffeeChatRoomRequest.articleTitle());

		return EnterCoffeeChatRoomResponse.of(enterCoffeeChatRoomRequest.articleTitle(), chatRoom);
	}

	public EnterCoffeeChatRoomResponse menteeEnter(EnterCoffeeChatRoomRequest enterCoffeeChatRoomRequest,
		ChatRoom chatRoom) {
		CoffeeChatValidation.validateChatRoomActive(chatRoom);

		return EnterCoffeeChatRoomResponse.of(enterCoffeeChatRoomRequest.articleTitle(), chatRoom);
	}

	public FindChatHistoryResponse findChatHistory(String roomKey) {
		List<FindMongoChatMessage> chatHistory = mongoChatMessageReader.findAllByRoomKey(roomKey).stream()
			.map(FindMongoChatMessage::from)
			.toList();

		return FindChatHistoryResponse.of(chatHistory);
	}

	@Transactional(readOnly = true)
	public CoffeeChatInfo coffeeChatRequest(CoffeeChatCommand.RequestCommand command) {
		Member recipient = memberReader.findMember(command.recipientId());
		Member sender = command.sender();

		CoffeeChatValidation.validateCoffeeChatRequest(sender, recipient);

		return CoffeeChatInfo.of(sender,recipient);
	}
}
