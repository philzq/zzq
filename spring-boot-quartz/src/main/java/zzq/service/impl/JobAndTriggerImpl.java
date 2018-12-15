package zzq.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzq.mapper.JobAndTriggerMapper;
import zzq.service.IJobAndTriggerService;


@Service
public class JobAndTriggerImpl implements IJobAndTriggerService {

	@Autowired
	private JobAndTriggerMapper jobAndTriggerMapper;
	
	public Page getJobAndTriggerDetails(Page page) {
		page.setRecords(jobAndTriggerMapper.getJobAndTriggerDetailsPage(page));
		return page;
	}

}