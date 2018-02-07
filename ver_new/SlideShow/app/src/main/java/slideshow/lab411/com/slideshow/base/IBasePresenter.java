package slideshow.lab411.com.slideshow.base;

public interface IBasePresenter<V extends IBaseView> {
    void onAttach(V view);

    void onDetach();
}
