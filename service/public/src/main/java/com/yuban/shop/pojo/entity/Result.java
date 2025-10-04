package com.yuban.shop.pojo.entity;

import com.yuban.shop.pojo.enums.HttpCodeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private String code; // 响应码（遵循5位字符串规范）
    private String message; // 响应信息描述
    private Object data; // 返回的数据

    public Result(HttpCodeEnum httpCodeEnum,Object data){
        this.code = httpCodeEnum.getCode();
        this.message = httpCodeEnum.getMsg();
        if (data!=null){
            this.data = data;
        }
    }

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }


    // 增删改 成功响应（无数据）
    public static Result success() {
        return new Result(HttpCodeEnum.SUCCESS, null);
    }

    // 查询 成功响应（有数据）
    public static Result success(Object data) {
        return new Result(HttpCodeEnum.SUCCESS, data);
    }

    // 失败响应
    public static Result error(HttpCodeEnum httpCodeEnum) {
        return new Result(httpCodeEnum, null);
    }
    public static Result error( String message) {
        return new Result("40000", message);
    }


}