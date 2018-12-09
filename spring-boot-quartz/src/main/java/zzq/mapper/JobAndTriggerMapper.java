package zzq.mapper;

import zzq.entity.JobAndTrigger;

import java.util.List;

public interface JobAndTriggerMapper {

	/**
	 * 获取任务和触发器详情
	 * @return
	 */
	List<JobAndTrigger> getJobAndTriggerDetails();
}
