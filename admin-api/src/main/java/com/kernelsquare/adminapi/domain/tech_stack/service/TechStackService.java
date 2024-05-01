package com.kernelsquare.adminapi.domain.tech_stack.service;

import com.kernelsquare.adminapi.domain.tech_stack.dto.CreateTechStackRequest;
import com.kernelsquare.adminapi.domain.tech_stack.dto.CreateTechStackResponse;
import com.kernelsquare.adminapi.domain.tech_stack.dto.FindAllTechStacksResponse;
import com.kernelsquare.adminapi.domain.tech_stack.dto.UpdateTechStackRequest;
import com.kernelsquare.core.common_response.error.code.TechStackErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;
import com.kernelsquare.domainmysql.domain.tech_stack.repository.TechStackReader;
import com.kernelsquare.domainmysql.domain.tech_stack.repository.TechStackStore;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechStackService {
	private final TechStackReader techStackReader;
	private final TechStackStore techStackStore;

	@Transactional
	public CreateTechStackResponse createTechStack(CreateTechStackRequest createTechStackRequest) {
		TechStack techStack = CreateTechStackRequest.toEntity(createTechStackRequest);
		try {
			TechStack saveTechStack = techStackStore.store(techStack);

			return CreateTechStackResponse.from(saveTechStack);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(TechStackErrorCode.TECH_STACK_ALREADY_EXISTED);
		}
	}

	@Transactional(readOnly = true)
	public FindAllTechStacksResponse findAllTechStacks() {
		List<TechStack> techStackList = techStackReader.findAll();

		return FindAllTechStacksResponse.from(techStackList);
	}

	@Transactional
	public void updateTechStack(Long techStackId, UpdateTechStackRequest updateTechStackRequest) {
		TechStack techStack = techStackReader.find(techStackId);

		boolean isSkillExists = techStackReader.existsBySkill(updateTechStackRequest.skill());

		if (isSkillExists) {
			throw new BusinessException(TechStackErrorCode.TECH_STACK_ALREADY_EXISTED);
		}

		techStack.update(updateTechStackRequest.skill());
	}

	@Transactional
	public void deleteTechStack(Long techStackId) {
		techStackStore.delete(techStackId);
	}
}
