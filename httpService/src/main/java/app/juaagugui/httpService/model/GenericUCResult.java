package app.juaagugui.httpService.model;

/**
 * Created by juan on 22/11/15.
 */
public class GenericUCResult<T> {
    protected T executor;

    public GenericUCResult(T executor) {
        this.executor = executor;
    }

}
