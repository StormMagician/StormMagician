package org.flowable.flowablespringboot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.flowable.rocketmq.MQProducter;
import org.flowable.rocketmq.MQPushConsumer;
import org.flowable.rocketmq.TaskProperties;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRestController {

	@Autowired
	private HttpServletRequest request;
	
    @Autowired
    private MyService myService;

    @RequestMapping(value="/process", method= RequestMethod.POST)
    public void startProcessInstance() {
        myService.startProcess(request.getParameter("startMessage"));
    }

    @RequestMapping(value="/tasks", method= RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public List<TaskRepresentation> getTasks(@RequestParam String assignee) {
        List<Task> tasks = myService.getTasks(assignee);
        List<TaskRepresentation> dtos = new ArrayList<TaskRepresentation>();
        for (Task task : tasks) {
            dtos.add(new TaskRepresentation(task.getId(), task.getName()));
        }
        return dtos;
    }
    
    
    @RequestMapping(value="/send1", method= RequestMethod.POST)
    public void send1() {
    	MQProducter producter = new MQProducter();
    	try {
			producter.init();
			TaskProperties taskProperties = new TaskProperties("123", 
					"12301", new Date().toString(), "期刊", "0001", "orgSplit");
			producter.sendMessage(taskProperties,"orgSplit", null);
			producter.distroy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @RequestMapping(value="/send2", method= RequestMethod.POST)
    public void send2() {
    	MQProducter producter = new MQProducter();
    	try {
			producter.init();
			TaskProperties taskProperties = new TaskProperties("123", 
					"12301", new Date().toString(), "期刊", "0001", "orgMatch");
			producter.sendMessage(taskProperties,"orgMatch", "${isMatch == false}");
			producter.distroy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @RequestMapping(value="/send3", method= RequestMethod.POST)
    public void send3() {
    	MQProducter producter = new MQProducter();
    	try {
			producter.init();
			TaskProperties taskProperties = new TaskProperties("123", 
					"12301", new Date().toString(), "期刊", "0001", "orgMatch");
			producter.sendMessage(taskProperties,"orgMatch", "${isMatch == true}");
			producter.distroy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @RequestMapping(value="/send4", method= RequestMethod.POST)
    public void send4() {
    	MQProducter producter = new MQProducter();
    	try {
			producter.init();
			TaskProperties taskProperties = new TaskProperties("123", 
					"12301", new Date().toString(), "期刊", "0001", "orgNormalize");
			producter.sendMessage(taskProperties,"orgNormalize", null);
			producter.distroy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    static class TaskRepresentation {

        private String id;
        private String name;

        public TaskRepresentation(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

    }
    
    static class StartProcessRepresentation {

        private String assignee;

        public String getAssignee() {
            return assignee;
        }

        public void setAssignee(String assignee) {
            this.assignee = assignee;
        }
    }

}