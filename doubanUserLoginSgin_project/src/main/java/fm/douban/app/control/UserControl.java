package fm.douban.app.control;

import fm.douban.model.User;
import fm.douban.model.UserLoginInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fm.douban.model.UserQueryParam;
import fm.douban.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "app")
public class UserControl {

  private static final Logger LOG = LoggerFactory.getLogger(UserControl.class);
  @Autowired
  private UserService userService;


  @PostConstruct
  public void init() {
    LOG.info("UserControl 启动啦");
    LOG.info("userService 注入啦");
  }


  @GetMapping(path = "/login")
  public String loginPage(Model model) {
    return "login";
  }


//登录页处理  从数据库中读取数据，判断是否登录成功
  @PostMapping(path = "/authenticate")
  @ResponseBody
  public Map login(@RequestParam String name, @RequestParam String password, HttpServletRequest request,
                   HttpServletResponse response) {
    Map returnData = new HashMap();

    // 根据登录名查询用户
    User regedUser = getUserByLoginName(name);

    // 如果查询返回为null 即找不到此登录用户
    if (regedUser == null) {
      returnData.put("result", false);
      returnData.put("message", "userName not correct");
      return returnData;
    }
  //如果返回对象的password和输入的相同，则登录成功。写入session
    if (regedUser.getPassword().equals(password)) {

      UserLoginInfo userLoginInfo = new UserLoginInfo();
      userLoginInfo.setUserId(password);
      userLoginInfo.setUserName(name);
      // 取得 HttpSession 对象
      HttpSession session = request.getSession();
      // 写入登录信息
      session.setAttribute("userLoginInfo", userLoginInfo);
      returnData.put("result", true);
      returnData.put("message", "login successfule");
    } else {
      returnData.put("result", false);
      returnData.put("message", "userName or password not correct");
    }

    return returnData;
  }


  @GetMapping(path = "/sign")
  public String signPage(Model model) {
    return "sign";
  }


//注册页处理 从数据库中判断是否注册成功
  @PostMapping(path = "/register")
  @ResponseBody
  public Map registerAction(@RequestParam String name, @RequestParam String password, @RequestParam String mobile,
                            HttpServletRequest request, HttpServletResponse response) {

    Map returnData = new HashMap();
    User regedUserregedUser = getUserByLoginName(name);
//返回不为null则表示该用户已存在
 if(regedUserregedUser!=null){
   returnData.put("userAdd","login name already exist");
   return returnData;

 }
 //不存在则创建
   User user = new User();
   user.setLoginName(name);
   user.setPassword(password);
   user.setMobile(mobile);
   User useradd = userService.add(user);

   //判断是否自动生成id
   if (useradd!=null&&useradd.getId()!=null) {
     returnData.put("userAdd", "register successfule");
     returnData.put("user", useradd);
     return returnData;
   }
     returnData.put("userAdd", "register failed");
     return returnData;
  }


  //输入用户名获取用户
  private User getUserByLoginName(String loginName) {
    //创建UserQueryParam对象，用于携带参数传入条件查询方法中判断
    UserQueryParam userQueryParam=new UserQueryParam();
    userQueryParam.setLoginName(loginName);
    //查询并返回分页
    Page<User> userResult =userService.list(userQueryParam);
    //定义一个空 User用于接收
    User user=null;

    //判断 返回结果不为空，返回结果的内容不为空，且返回内容>0.则取第1个
    if (userResult != null && userResult.getContent() != null && userResult.getContent().size() > 0) {
       user = userResult.getContent().get(0);
    }
    return user;
  }

}
