package com.apass.zufang.service.operation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.zufang.domain.dto.ApprintmentQueryParams;
import com.apass.zufang.domain.entity.SubmitMessage;
import com.apass.zufang.mapper.zfang.SubmitMessageMapper;
import com.apass.zufang.utils.ResponsePageBody;
@Service
public class SubmitMessageService {
	@Autowired
	private SubmitMessageMapper submitMessageMapper;
	/**
	 * getSubmitMessageList
	 * @param entity
	 * @return
	 */
	public ResponsePageBody<SubmitMessage> getSubmitMessageList(ApprintmentQueryParams entity) {
		ResponsePageBody<SubmitMessage> pageBody = new ResponsePageBody<SubmitMessage>();
        List<SubmitMessage> list = submitMessageMapper.getSubmitMessageList(entity);
        pageBody.setRows(list);
        list = submitMessageMapper.getSubmitMessageList(null);
        pageBody.setTotal(list.size());
        pageBody.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
        return pageBody;
	}
}