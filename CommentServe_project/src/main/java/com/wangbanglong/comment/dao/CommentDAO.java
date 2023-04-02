package com.wangbanglong.comment.dao;

import com.wangbanglong.comment.dataobject.CommentDO;
import com.wangbanglong.comment.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentDAO {

    int batchAdd(@Param("list") List<CommentDO> userDOs);

    List<CommentDO> findAll();

    int insert(CommentDO commentDO);

    int update(CommentDO userDO);

    int delete(@Param("id") long id);

    List<Comment> findByRefId(@Param("refId") String refId);

    List<CommentDO> findByUserIds(@Param("userIds") List<Long> ids);
}
