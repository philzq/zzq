package zzq.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface IJobAndTriggerService {
	Page getJobAndTriggerDetails(Page page);
}
