package com.example.demo.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/*
 * 响应返回数据基类
 * 传入泛型T，返回具体数据data
 * */
@Data
@ToString
@EqualsAndHashCode
public class RespBase<T> {


    private Boolean success = true;
    private Integer status = 200;
    private String message = "成功!";
    private T data;

    public RespBase(T data) {
        // TODO Auto-generated constructor stub
        this.data=data;
    }

    public RespBase() {
        // TODO Auto-generated constructor stub
    }
}