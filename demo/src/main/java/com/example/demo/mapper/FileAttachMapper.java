package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.FileAttach;
import org.apache.ibatis.annotations.Mapper;

/*
 * 附件相关接口
 * 
 * */
@Mapper

public interface FileAttachMapper extends BaseMapper<FileAttach> {

	


	FileAttach selectById(Long fileId);
}
