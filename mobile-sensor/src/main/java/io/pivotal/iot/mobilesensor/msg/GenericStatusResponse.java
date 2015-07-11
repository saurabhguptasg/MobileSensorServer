package io.pivotal.iot.mobilesensor.msg;

/**
 * @author sgupta
 * @since 6/26/15.
 */
public class GenericStatusResponse<T> {
  private final String status;
  private final T data;

  public static final GenericStatusResponse OK = new GenericStatusResponse<>("ok", null);
  public static final GenericStatusResponse ERR = new GenericStatusResponse<>("error", null);


  public GenericStatusResponse(String status, T data) {
    this.status = status;
    this.data = data;
  }

  public GenericStatusResponse() {
    this.status = null;
    this.data = null;
  }

  public String getStatus() {
    return status;
  }

  public Object getData() {
    return data;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("GenericStatusResponse{");
    sb.append("status='").append(status).append('\'');
    sb.append(", data=").append(data == null ? null : data.toString());
    sb.append('}');
    return sb.toString();
  }

  public static <T> GenericStatusResponse<T> okWithData(T data) {
    return new GenericStatusResponse<T>("ok", data);
  }

  public static <T> GenericStatusResponse<T> errWithData(T data) {
    return new GenericStatusResponse<T>("error", data);
  }

}
