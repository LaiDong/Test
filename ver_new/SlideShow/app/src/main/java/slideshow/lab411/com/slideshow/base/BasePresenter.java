package slideshow.lab411.com.slideshow.base;

public class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {
    private static final String TAG = "BasePresenter";

    //    public final IDataManager mDataManager;
    public V mView;

//    @Inject
//    public BasePresenter(IDataManager dataManager) {
//        mDataManager = dataManager;
//    }


    @Override
    public void onAttach(V view) {
        mView = view;
    }

    @Override
    public void onDetach() {
        mView = null;
    }

    public V getMvpView() {
        return mView;
    }

    public boolean isViewAttached() {
        return mView == null;

    }
}
