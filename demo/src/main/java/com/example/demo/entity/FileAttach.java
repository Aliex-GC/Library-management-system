package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("fileattach")
public class FileAttach {

	@TableId(value = "ID",type = IdType.AUTO)
	private Long id;

	@TableField(value = "FILENAME")
	private String fileName;

	@TableField(value = "FILEPATH")
	private String filePath;

	@TableField(value = "EXT")
	private String ext;
}
