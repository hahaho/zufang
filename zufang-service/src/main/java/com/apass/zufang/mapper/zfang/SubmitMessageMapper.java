package com.apass.zufang.mapper.zfang;
import java.util.List;
import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.dto.SubmitMessageQueryParams;
import com.apass.zufang.domain.entity.SubmitMessage;
public interface SubmitMessageMapper extends GenericMapper<SubmitMessage,Long> {
	/**
	 * getSubmitMessageList
	 * @param entity
	 * @return
	 */
	public List<SubmitMessage> getSubmitMessageList(SubmitMessageQueryParams entity);
}