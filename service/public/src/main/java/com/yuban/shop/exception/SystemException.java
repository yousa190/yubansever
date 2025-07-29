package com.yuban.shop.exception;


import com.yuban.shop.pojo.enums.HttpCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
public class SystemException extends RuntimeException {
        private String code;
        private String message;

        public SystemException(HttpCodeEnum httpCodeEnum) {
                super(httpCodeEnum.getMsg());
                this.code = httpCodeEnum.getCode();
                this.message = httpCodeEnum.getMsg();
        }
}
