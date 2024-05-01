package com.kernelsquare.memberapi.domain.reservation_article.service;

import com.kernelsquare.core.common_response.error.code.ReservationArticleErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.core.dto.Pagination;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import com.kernelsquare.domainmysql.domain.coffeechat.repository.CoffeeChatStore;
import com.kernelsquare.domainmysql.domain.hashtag.entity.Hashtag;
import com.kernelsquare.domainmysql.domain.hashtag.repository.HashtagReader;
import com.kernelsquare.domainmysql.domain.hashtag.repository.HashtagStore;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberReader;
import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;
import com.kernelsquare.domainmysql.domain.reservation.repository.ReservationReader;
import com.kernelsquare.domainmysql.domain.reservation.repository.ReservationStore;
import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;
import com.kernelsquare.domainmysql.domain.reservation_article.repository.ReservationArticleReader;
import com.kernelsquare.domainmysql.domain.reservation_article.repository.ReservationArticleStore;
import com.kernelsquare.memberapi.domain.hashtag.dto.FindHashtagResponse;
import com.kernelsquare.memberapi.domain.hashtag.dto.UpdateHashtagRequest;
import com.kernelsquare.memberapi.domain.reservation.dto.FindReservationResponse;
import com.kernelsquare.memberapi.domain.reservation.dto.UpdateReservationRequest;
import com.kernelsquare.memberapi.domain.reservation_article.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReservationArticleService {
	private final MemberReader memberReader;
	private final ReservationArticleReader reservationArticleReader;
	private final ReservationArticleStore reservationArticleStore;
	private final ReservationReader reservationReader;
	private final ReservationStore reservationStore;
	private final CoffeeChatStore coffeeChatStore;
	private final HashtagReader hashtagReader;
	private final HashtagStore hashtagStore;

	@Transactional
	public CreateReservationArticleResponse createReservationArticle(
		CreateReservationArticleRequest createReservationArticleRequest) {
		Member member = memberReader.findMember(createReservationArticleRequest.memberId());

		ReservationArticle reservationArticle = CreateReservationArticleRequest.toEntity(
			createReservationArticleRequest, member);

		// 예약창이 있는 경우 생성 불가 (1인 1예약창 정책)
		if (reservationReader.existsMyReservationSinceCurrentTime(
			member.getId(),
			LocalDateTime.now()
		)) {
			throw new BusinessException(ReservationArticleErrorCode.TOO_MANY_RESERVATION_ARTICLE);
		}

		ReservationArticle saveReservationArticle = reservationArticleStore.store(reservationArticle);

		// HashTag 저장
		for (String hashTags : createReservationArticleRequest.hashTags()) {
			Hashtag hashTag = Hashtag.builder()
				.content(hashTags)
				.reservationArticle(saveReservationArticle)
				.build();

			hashtagStore.store(hashTag);
		}

		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDateTime startTime = LocalDateTime.MAX;
		LocalDateTime endTime = LocalDateTime.MIN;

		for (LocalDateTime dateTime : createReservationArticleRequest.dateTimes()) {
			// 새로운 Chatroom 생성
			ChatRoom chatroom = ChatRoom.builder()
				.roomKey(UUID.randomUUID().toString())
				.expirationTime(dateTime.plusMinutes(30))
				.build();

			coffeeChatStore.store(chatroom);

			if (startTime.isAfter(dateTime)) {
				startTime = dateTime;
			}

			if (endTime.isBefore(dateTime.plusMinutes(30))) {
				endTime = dateTime.plusMinutes(30);
			}

			// Reservation 생성 및 설정
			Reservation reservation = Reservation.builder()
				.startTime(dateTime)
				.endTime(dateTime.plusMinutes(30))
				.reservationArticle(saveReservationArticle)
				.chatRoom(chatroom)
				.build();

			reservationStore.store(reservation);
		}

		// 3일 기간 체크
		Long checkDurationDay = ChronoUnit.SECONDS.between(startTime, endTime);
		if (checkDurationDay > 3 * 24 * 60 * 60) {
			throw new BusinessException(ReservationArticleErrorCode.RESERVATION_TIME_LIMIT);
		}

		// 예약 생성 기한 체크 로직 (7일 이후, 한달 이전)
		if (!(startTime.isAfter(currentDateTime.plusDays(6)) && startTime.isBefore(
			currentDateTime.plusMonths(1)))) {
			throw new BusinessException(ReservationArticleErrorCode.RESERVATION_PERIOD_LIMIT);
		}

		saveReservationArticle.addStartTime(startTime);
		saveReservationArticle.addEndTime(endTime);

		return CreateReservationArticleResponse.from(saveReservationArticle);
	}

	@Transactional(readOnly = true)
	public PageResponse<FindAllReservationArticleResponse> findAllReservationArticle(Pageable pageable) {

		Integer currentPage = pageable.getPageNumber() + 1;
		Page<ReservationArticle> pages = reservationArticleReader.findAllPage(pageable);
		Integer totalPages = pages.getTotalPages();

		if (totalPages == 0)
			totalPages += 1;

		if (currentPage > totalPages) {
			throw new BusinessException(ReservationArticleErrorCode.PAGE_NOT_FOUND);
		}

		Pagination pagination = Pagination.toEntity(totalPages, pages.getSize(), currentPage.equals(totalPages));

		List<FindAllReservationArticleResponse> responsePages = pages.getContent().stream()
			.map(article -> {
				Long coffeeChatCount = reservationArticleReader.countBeforeMyReservationArticleEndTime(
					article.getMember().getId(),
					LocalDateTime.now());
				Long availableReservationCount = reservationReader.countAvailableInReservationArticle(
					article.getId());
				Long totalReservationCount = reservationReader.countInReservationArticle(article.getId());
				Boolean articleStatus = checkIfReservationWithinTheAvailablePeriod(article.getStartTime());
				return FindAllReservationArticleResponse.of(
					article.getMember(),
					article,
					articleStatus,
					coffeeChatCount,
					availableReservationCount
					, totalReservationCount
				);
			})
			.toList();

		return PageResponse.of(pagination, responsePages);
	}

	private Boolean checkIfReservationWithinTheAvailablePeriod(LocalDateTime startTime) {
		boolean check = false;
		LocalDateTime currentTime = LocalDateTime.now();

		LocalDate minStartDate = startTime.toLocalDate();
		LocalDate currentDate = currentTime.toLocalDate();

		if (currentDate.isAfter(minStartDate.minusDays(7)) && currentDate.isBefore(minStartDate.minusDays(1))) {
			check = true;
		}
		return check;
	}

	@Transactional(readOnly = true)
	public FindReservationArticleResponse findReservationArticle(Long postId) {
		ReservationArticle reservationArticle = reservationArticleReader.find(postId);

		Member member = reservationArticle.getMember();

		List<Hashtag> hashtags = hashtagReader.findAllInReservationArticle(postId);
		List<FindHashtagResponse> findHashtagResponses = hashtags.stream()
			.map(FindHashtagResponse::from)
			.toList();

		List<Reservation> reservations = reservationReader.findAllInReservationArticle(postId);
		List<FindReservationResponse> findReservationResponses = reservations.stream()
			.map(FindReservationResponse::from)
			.toList();

		return FindReservationArticleResponse.of(member, reservationArticle, findHashtagResponses,
			findReservationResponses);
	}

	@Transactional
	public void updateReservationArticle(Long postId, UpdateReservationArticleRequest updateReservationArticleRequest) {
		ReservationArticle reservationArticle = reservationArticleReader.find(postId);

		// 멘토인지, 본인이 쓴 글인지 확인
		if (!reservationArticle.getMember().getId().equals(updateReservationArticleRequest.memberId())) {
			throw new BusinessException(ReservationArticleErrorCode.MENTOR_MISMATCH);
		}

		// 제목이나 내용은 ReservationArticle 의 update 로 변경
		reservationArticle.update(updateReservationArticleRequest.title(), updateReservationArticleRequest.content(),
			updateReservationArticleRequest.introduction());

		// changehashtags 가 존재한다면 아래 로직
		if (updateReservationArticleRequest.changeHashtags() != null) {
			List<UpdateHashtagRequest> changeHashtags = updateReservationArticleRequest.changeHashtags();
			List<String> addHashtags = new ArrayList<>();
			Map<Long, String> removeHashtags = new HashMap<>();

			int addHashtagCount = 0;
			int removeHashtagCount = 0;
			for (UpdateHashtagRequest hashtagRequest : changeHashtags) {
				if (hashtagRequest.changed().equals("add")) {
					addHashtagCount++;
					addHashtags.add(hashtagRequest.content());
				} else if (hashtagRequest.changed().equals("remove")) {
					removeHashtagCount++;
					removeHashtags.put(hashtagRequest.hashtagId(), hashtagRequest.content());
				} else {
					throw new BusinessException(ReservationArticleErrorCode.STATUS_NOT_FOUND);
				}
			}

			// 수정된 해시태그 10개 넘는지 체크로직
			long currentHashtagCount = hashtagReader.countInReservationArticle(postId);
			long updatedHashtagCount = currentHashtagCount + addHashtagCount - removeHashtagCount;
			if (updatedHashtagCount >= 10) {
				throw new BusinessException(ReservationArticleErrorCode.TOO_MANY_HASHTAG);
			}

			// 체크로직을 통과했다면 add 는 더하고 remove 는 제거 (repository 로 반영)
			removeHashtags.forEach((id, content) -> hashtagStore.delete(id));
			for (String hashTags : addHashtags) {
				Hashtag hashTag = Hashtag.builder()
					.content(hashTags)
					.reservationArticle(reservationArticle)
					.build();

				hashtagStore.store(hashTag);
			}
		}

		// changeReservations 가 존재한다면 아래 로직
		if (updateReservationArticleRequest.changeReservations() != null) {
			List<UpdateReservationRequest> changeReservations = updateReservationArticleRequest.changeReservations();
			List<LocalDateTime> addReservations = new ArrayList<>();
			Map<Long, LocalDateTime> removeReservations = new HashMap<>();

			int addReservationCount = 0;
			int removeReservationCount = 0;
			for (UpdateReservationRequest reservationRequest : changeReservations) {
				if (reservationRequest.changed().equals("add")) {
					addReservationCount++;
					addReservations.add(reservationRequest.startTime());
				} else if (reservationRequest.changed().equals("remove")) {
					removeReservationCount++;
					removeReservations.put(reservationRequest.reservationId(), reservationRequest.startTime());
				}
			}

			// 수정된 날짜들이 10개가 넘는지 체크로직
			long currentReservationCount = reservationReader.countInReservationArticle(postId);
			long updatedReservationCount = currentReservationCount + addReservationCount - removeReservationCount;
			if (updatedReservationCount >= 10) {
				throw new BusinessException(ReservationArticleErrorCode.TOO_MANY_RESERVATION);
			}

			// 수정된 날짜들이 최대 기간 3일을 넘는지 체크로직
			Map<Long, LocalDateTime> currentReservations = new HashMap<>();
			List<Reservation> currentReservationsList = reservationReader.findAllInReservationArticle(postId);
			for (Reservation reservation : currentReservationsList) {
				currentReservations.put(reservation.getId(), reservation.getStartTime());
			}

			if (!currentReservations.isEmpty()) {
				PriorityQueue<LocalDateTime> minHeap = new PriorityQueue<>();
				PriorityQueue<LocalDateTime> maxHeap = new PriorityQueue<>((o1, o2) -> o2.compareTo(o1));

				for (LocalDateTime startTime : currentReservations.values()) {
					minHeap.offer(startTime);
					maxHeap.offer(startTime);
				}

				if (!addReservations.isEmpty()) {
					for (LocalDateTime startTime : addReservations) {
						minHeap.offer(startTime);
						maxHeap.offer(startTime);
					}
				}

				for (LocalDateTime startTime : removeReservations.values()) {
					minHeap.removeIf(dateTime -> dateTime.equals(startTime));
					maxHeap.removeIf(dateTime -> dateTime.equals(startTime));
				}

				LocalDateTime minStartTime = minHeap.poll();
				LocalDateTime maxStartTime = maxHeap.poll();
				long durationInDays = ChronoUnit.DAYS.between(Objects.requireNonNull(minStartTime).toLocalDate(),
					Objects.requireNonNull(maxStartTime).toLocalDate());
				if (durationInDays > 3) {
					throw new BusinessException(ReservationArticleErrorCode.RESERVATION_TIME_LIMIT);
				}
			}

			// 두 체크로직을 통과했다면 add 는 더하고 remove 는 제거 (repository 로 반영)
			removeReservations.forEach((reservationId, startTime) -> reservationStore.delete(reservationId));
			for (LocalDateTime dateTime : addReservations) {
				// 새로운 Chatroom 생성
				ChatRoom chatroom = ChatRoom.builder()
					.roomKey(UUID.randomUUID().toString())
					.build();

				coffeeChatStore.store(chatroom);

				// Reservation 생성 및 설정
				Reservation reservation = Reservation.builder()
					.startTime(dateTime)
					.endTime(dateTime.plusMinutes(30))
					.reservationArticle(reservationArticle)
					.chatRoom(chatroom)
					.build();

				reservationStore.store(reservation);
			}
		}
	}

	@Transactional
	public void deleteReservationArticle(Long postId) {
		ReservationArticle reservationArticle = reservationArticleReader.find(postId);

		LocalDate currentDate = LocalDateTime.now().toLocalDate();
		LocalDate startDate = reservationArticle.getStartTime().toLocalDate();

		if (!currentDate.isBefore(startDate.minusDays(7))) {
			throw new BusinessException(ReservationArticleErrorCode.DELETE_ONLY_BEFORE_7DAYS);
		}

		reservationArticleStore.delete(postId);

		coffeeChatStore.deleteWithReservationArticle(postId);

		reservationStore.deleteAllByReservationArticleId(postId);

		hashtagStore.deleteAllWithReservationArticle(postId);
	}

}

