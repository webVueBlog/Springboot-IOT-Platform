package com.webVueBlog.data.quartz;

import com.webVueBlog.iot.domain.DeviceJob;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（允许并发执行）
 * 
 *
 *
 */
public class QuartzJobExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, DeviceJob deviceJob) throws Exception
    {
        JobInvokeUtil.invokeMethod(deviceJob);
    }
}
