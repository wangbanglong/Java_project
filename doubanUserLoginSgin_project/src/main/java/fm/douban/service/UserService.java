package fm.douban.service;

import fm.douban.model.User;
import fm.douban.model.UserQueryParam;
import org.springframework.data.domain.Page;

public interface UserService {
//增
public User add(User user);
//通过主键查寻
public User get(String id);

//条件查询支持分页
public Page<User> list( UserQueryParam param);
//改
public boolean modfiy(User user);
//删
public boolean delete(String id);
}
