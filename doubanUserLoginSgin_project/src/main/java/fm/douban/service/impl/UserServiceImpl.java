package fm.douban.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import fm.douban.model.User;
import fm.douban.model.UserQueryParam;
import fm.douban.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongSupplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {
  private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

  //注入数据库自带的工具类MongoTemplate
  @Autowired
  private MongoTemplate mongoTemplate;


  //增加一条记录
  @Override
  public User add(User user) {
    return mongoTemplate.insert(user);
  }



  //主键查找一条记录
  @Override
  public User get(String id) {
    return mongoTemplate.findById(id, User.class);
  }



//获取一个对象，多条件查找并返回记录
  @Override
  public Page<User> list(UserQueryParam param) {
// 作为服务，要对入参进行判断，不能假设被调用时，入参一定正确
    if (param == null) {
      LOG.error("input song data is not correct.");
      return null;
    }
    // 总条件
    Criteria criteria = new Criteria();
    // 可能有多个子条件
    List<Criteria> subCris = new ArrayList();

    //条件判断 通过传入的对象，对其成员变量在数据库中逐个查找
    if (StringUtils.hasText(param.getLoginName())) {
      subCris.add(Criteria.where("loginName").is(param.getLoginName()));
    }
    if (StringUtils.hasText(param.getPassword())) {
      subCris.add(Criteria.where("password").is(param.getPassword()));
    }
    if (StringUtils.hasText(param.getMobile())) {
      subCris.add(Criteria.where("mobile").is(param.getMobile()));
    }


    // 必须至少有一个查询条件。即传入的对象有几个成员变量的数据 本例只有一个loginName传入进来了
    if (subCris.isEmpty()) {
      LOG.error("input song query param is not correct.");
      return null;
    }

    // 三个子条件以 and 关键词连接成总条件对象，相当于 name='' and lyrics='' and subjectId=''
    criteria.andOperator(subCris.toArray(new Criteria[]{}));
    // 条件对象构建查询对象
    Query query = new Query(criteria);

    //查询并返回 由于可能有多个对象，采用集合存储
    List<User> users = mongoTemplate.find(query, User.class);

    //调用PageRequest.of方法构建分页对象 注入到查询对象
    Pageable pageable = PageRequest.of(0 , 20);
    query.with(pageable);
    //查询总数
    long count = mongoTemplate.count(query, User.class);
    //构建分页器
    Page<User> pageResult = PageableExecutionUtils.getPage(users, pageable, new LongSupplier() {
      @Override
      public long getAsLong() {
        return count;
      }
    });

    return pageResult;
  }




//修改 传入一个有主键的对象，通过查询后修改指定数据
  @Override
  public boolean modfiy(User user) {
    //通过传入对象的成员变量主键id查找到该对象
    Criteria criteria=Criteria.where("id").is(user.getId());
    Query query = new Query(criteria);

    //构建修改对象，将传入进来的user数据对同id的数据库数据进行修改
    Update updateData = new Update();
    updateData.set("loginName",user.getLoginName());

    // 执行修改，修改返回结果的是一个对象
    UpdateResult result = mongoTemplate.updateFirst(query, updateData, User.class);
     // 修改的记录数大于 0 ，表示修改成功
    return result!=null && result.getModifiedCount()>0;
  }


//删除 通过传入有主键的对象，删除数据库中同主键的对象
  @Override
  public boolean delete(String id) {
    User user=new User();
    user.setId(id);
    DeleteResult result = mongoTemplate.remove(user);
    return result!=null&&result.getDeletedCount()>0;
  }

}
