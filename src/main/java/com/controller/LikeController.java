package com.controller;

import com.async.EventModel;
import com.async.EventProducer;
import com.async.EventType;
import com.model.EntityType;
import com.model.HostHolder;
import com.service.LikeService;
import com.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/7/14.
 */
@Controller
public class LikeController {
    private static final Logger logger= LoggerFactory.getLogger(IndexController.class);

    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    QuestionService questionService;

    @RequestMapping(path = "/like/{questionid}")
    //@ResponseBody
    public String like(@PathVariable("questionid") int questionid){
        eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(hostHolder.getUser().getId()).setEntityId(questionid).setEntityType(EntityType.ENTITY_QUESTION).setExt("questionid",String.valueOf(questionid)).setEntityOwnerId(questionService.getById(questionid).getUserId()));
        long likecount=likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionid);
        //return WendaUtil.getJSONString(0, String.valueOf(likecount));
        return "redirect:/question/"+questionid;
    }

    @RequestMapping(path = "/dislike/{questionid}")
    //@ResponseBody
    public String dislike(@PathVariable("questionid") int questionid){

        long likecount=likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionid);
        //return WendaUtil.getJSONString(0, String.valueOf(likecount));
        return "redirect:/question/"+questionid;
    }

}