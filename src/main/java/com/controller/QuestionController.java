package com.controller;

import com.model.*;
import com.service.CommentService;
import com.service.QuestionService;
import com.service.UserService;
import com.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
@Controller
public class QuestionController {
    private static final Logger logger= LoggerFactory.getLogger(IndexController.class);

    @Autowired
    QuestionService questionService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;

    @RequestMapping(value = "/question/add",method = RequestMethod.POST)
    //@ResponseBody
    public String addquestion(@RequestParam("title")String title,
                              @RequestParam("content")String content){
        try {
            Question question=new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if(hostHolder.getUser()==null){
                question.setUserId(WendaUtil.ANONYMOUS_USERID);
            }else {
                question.setUserId(hostHolder.getUser().getId());
            }

            if(questionService.addquestion(question)>0){
                return "redirect:/";
                //return WendaUtil.getJSONString(0);
            }

        }catch (Exception e){
            logger.info("增加题目失败");
        }
        return "redirect:/";
        //return WendaUtil.getJSONString(1,"shibai");
    }

    @RequestMapping("/question/{qid}")
    public String questiondetail(@PathVariable("qid")int qid,
                                 Model model){
        Question question=questionService.getById(qid);
        model.addAttribute("question",question);
        model.addAttribute("owner",userService.getUser(question.getUserId()));

        //
        List<Comment> commentList=commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> comments=new ArrayList<>();
        for(Comment comment:commentList){
            ViewObject vo=new ViewObject();
            vo.set("comment",comment);
            vo.set("user",userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments",comments);

        if (hostHolder.getUser() == null) {
            return "redirect:loginpage";
        }

        return "detail";
    }

    @RequestMapping("/addquestionpage")
    public String addquestionpage(){
        return "addquestionpage";
    }
}

