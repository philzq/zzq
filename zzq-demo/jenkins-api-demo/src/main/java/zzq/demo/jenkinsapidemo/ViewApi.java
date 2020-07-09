package zzq.demo.jenkinsapidemo;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.View;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * View(视图) 相关操作
 *
 * 例如对视图的增、删、改、查等操作
 */
@Slf4j
public class ViewApi {

    // Jenkins 对象
    private JenkinsServer jenkinsServer;
    // http 客户端对象
    private JenkinsHttpClient jenkinsHttpClient;

    /**
     * 构造方法中调用连接 Jenkins 方法
     */
    ViewApi() {
        JenkinsApi jenkinsApi = new JenkinsApi();
        // 连接 Jenkins
        jenkinsServer = JenkinsConnect.connection();
        // 设置客户端连接 Jenkins
        jenkinsHttpClient = JenkinsConnect.getClient();
    }

    /**
     * 创建视图
     */
    public void createView() {
        try {
            // 创建一个 xml 字符串，里面设置一个 view 描述信息
            String xml = "<listView _class=\"hudson.model.ListView\">\n" +
                    "<description>用于测试的视图</description>\n" +
                    "</listView>";
            // 创建 view
            jenkinsServer.createView("test-view", xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取视图基本信息
     */
    public View getView(String viewName) {
        View view = null;
        try {
            // 获取视图基本信息
            view = jenkinsServer.getView(viewName);
            System.out.println(view.getName());
            System.out.println(view.getUrl());
            System.out.println(view.getDescription());
            // 获取视图xml信息
            String viewXml = jenkinsHttpClient.get("/view/" + viewName + "/api/xml");
            System.out.println(viewXml);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 获取视图配置 XML 信息
     */
    public void getViewConfig() {
        try {
            // 视图名
            String viewName = "test-view";
            // 获取视图配置xml信息
            String viewConfigXml = jenkinsHttpClient.get("/view/" + viewName + "/config.xml");
            System.out.println(viewConfigXml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新视图信息
     */
    public void updateView() {
        try {
            // 创建一个 xml 字符串，里面设置一个要修改的某些字段,具体xml可以到jenkins查看
            // 例如，下面xml文件是从地址：https://Jenkins-IP/jenkins/view/test-view/config.xml 获取的
            String xml = "<hudson.model.ListView>\n" +
                    "<name>test-view</name>\n" +
                    "<description>用于测试的视图1111</description>\n" +
                    "<filterExecutors>false</filterExecutors>\n" +
                    "<filterQueue>false</filterQueue>\n" +
                    "<properties class=\"hudson.model.View$PropertyList\"/>\n" +
                    "<jobNames>\n" +
                    "<comparator class=\"hudson.util.CaseInsensitiveComparator\"/>\n" +
                    "</jobNames>\n" +
                    "<jobFilters/>\n" +
                    "<columns>\n" +
                    "<hudson.views.StatusColumn/>\n" +
                    "<hudson.views.WeatherColumn/>\n" +
                    "<hudson.views.JobColumn/>\n" +
                    "<hudson.views.LastSuccessColumn/>\n" +
                    "<hudson.views.LastFailureColumn/>\n" +
                    "<hudson.views.LastDurationColumn/>\n" +
                    "<hudson.views.BuildButtonColumn/>\n" +
                    "<hudson.plugins.favorite.column.FavoriteColumn plugin=\"favorite@2.3.2\"/>\n" +
                    "</columns>\n" +
                    "<recurse>false</recurse>\n" +
                    "</hudson.model.ListView>";
            jenkinsServer.updateView("test-view", xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除视图
     */
    public void deleteView() {
        try {
            String viewName = "test-view";
            jenkinsHttpClient.post("/view/" + viewName + "/doDelete");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将一个试图下的所有job复制到另一个试图
     * @param originView
     * @param destView
     */
    public void copyView(String originView,String destView){
        View view = getView(originView);
        if(view != null) {
            List<Job> jobs = view.getJobs();
            if (jobs != null && !jobs.isEmpty()) {
                for (Job job : jobs) {
                    String name = job.getName();
                    String destJobName = name+"test";
                    if (name.contains("old")) {
                        try {
                            String preJenkinsCommand = "java -jar jenkins-cli.jar -s " + JenkinsConnect.JENKINS_URL + " -auth " + JenkinsConnect.JENKINS_USERNAME + ":" + JenkinsConnect.JENKINS_PASSWORD +" ";
                            String copyJobCommand =  preJenkinsCommand + "copy-job \"" + name + "\" \"" + destJobName + "\"";
                            log.info("执行复制jobcmd命名："+copyJobCommand);
                            Runtime.getRuntime().exec("cmd /c " + copyJobCommand);
                            String addJobToView = preJenkinsCommand + "add-job-to-view "+destView+" \""+destJobName+"\"";
                            log.info("执行将job添加到视图cmd命名："+addJobToView);
                            Runtime.getRuntime().exec("cmd /c " + addJobToView);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    public static void main(String[] args) throws Exception{
        ViewApi viewApi = new ViewApi();
        // 创建视图
        //viewApi.createView();
        // 获取视图信息
        String viewName = "123";
        String destView = "456";
        View view = viewApi.getView(viewName);
        viewApi.copyView(viewName,destView);
        System.out.println(view);
        // 获取视图配置xml信息
        //viewApi.getViewConfig();
        // 更新视图信息
        //viewApi.updateView();
        // 删除视图
        //viewApi.deleteView();
    }
}
