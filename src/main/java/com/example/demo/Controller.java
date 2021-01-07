package com.example.demo;

import com.example.demo.msmq.MSMQConfig;
import com.example.demo.msmq.template.IMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Controller {

    @Autowired
    private MSMQConfig msmqConfig;

    @Autowired
    private IMQTemplate mqTemplate;

    @GetMapping(value = {"", "index", "/"})
    public ModelAndView index(ModelAndView mv) {
        mv.setViewName("index");
        return mv;
    }

    @PostMapping(value = "send")
    public Map<String, String> send(String message) {
        Map<String, String> result = new HashMap<>();
        mqTemplate.sendMessage(msmqConfig.getQueueName(), message);
        result.put("code", "200");
        return result;
    }
}
