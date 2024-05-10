package com.kerenlsqaure.applicationadmin.domain.notice.mapper;

import com.kerenlsqaure.applicationadmin.domain.notice.command.NoticeCommand;
import com.kerenlsqaure.applicationadmin.domain.notice.dto.NoticeDto;
import com.kerenlsqaure.applicationadmin.domain.notice.info.NoticeInfo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
	componentModel = "spring",
	injectionStrategy = InjectionStrategy.CONSTRUCTOR,
	unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface NoticeDtoMapper {
	NoticeCommand.CreateCommand toCommand(NoticeDto.CreateRequest request);

	NoticeCommand.UpdateCommand toCommand(NoticeDto.UpdateRequest request);

	NoticeDto.FindResponse toSingleResponse(NoticeInfo noticeInfo);

	NoticeDto.FindAllResponse toFindAllResponse(NoticeInfo noticeInfo);
}
