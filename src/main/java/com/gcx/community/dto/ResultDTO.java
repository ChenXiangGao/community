package com.gcx.community.dto;

import com.gcx.community.exception.CustomizeErrorCode;
import com.gcx.community.exception.CustomizeException;
import lombok.Data;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 服务器对于请求的返回DTO对象
 * 包括状态码，信息等
 */

@Data
@ResponseBody
public class ResultDTO {
    private Integer code;
    private String message;

    /**
     * 对传入的code和message进行封装并返回一个ResultDTO的JSON对象
     * @param code
     * @param message
     * @return
     */
    public static ResultDTO errorReturn(Integer code, String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    /**
     * 将抛出异常 errorCode中的code和message封装进ResultDTO
     * 这样写的目的是
     *      将异常部分的代码都抽取出来交给exception来处理，增加复用性
     *      使程序能给全局的处理异常，就不用单独的再写捕捉和处理异常的代码
     * @param errorCode
     * @return
     */
    public static ResultDTO errorReturn(CustomizeErrorCode errorCode) {
        return errorReturn(errorCode.getCode(), errorCode.getMessage());
    }

    /**
     * 将拦截到的异常 exception中的code和message封装进ResultDTO
     * @param exception
     * @return
     */
    public static ResultDTO errorReturn(CustomizeException exception) {
        return errorReturn(exception.getCode(), exception.getMessage());
    }

    public static ResultDTO successReturn() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }
}
