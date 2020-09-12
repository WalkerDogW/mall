package site.javaee.mall.common.exception;

/**
 * 消息状态码，前两位业务场景，后三位错误类型
 *
 * @author shkstart
 * @create 2020-09-10 21:26
 */
public enum BizCodeEnume {
    UNKNOW_EXCEPTION(10000, "系统未知异常"),
    VALID_EXCEPTION(10001, "参数格式校验失败");

    private Integer code;
    private String msg;

    BizCodeEnume(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
