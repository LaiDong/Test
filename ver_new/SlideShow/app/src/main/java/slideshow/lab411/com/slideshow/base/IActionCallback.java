package slideshow.lab411.com.slideshow.base;

public interface IActionCallback<V extends Object> {
    void onSuccess(V result);

    void onFailed(V result);
}
