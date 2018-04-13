package com.android.nanodegree.moviesapp.util;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Khalifa on 3/17/2018.
 *
 */
public class RxWrapper {

    /**
     * Represents a generic action that needs to be executed by another party.
     *
     * @param <T> Type of the return that this action results in
     */
    public interface Action<T> {

        T run() throws Throwable;
    }

    /**
     * Represents a callback to return a generic result.
     *
     * @param <Y> Type of the generic result returned by callback
     */
    public interface Callback<Y> {

        void onSuccess(Y result);

        void onError(Throwable error);

        void onFinish();
    }

    private CompositeSubscription mCompositeSubscription;

    public RxWrapper() {
        mCompositeSubscription = new CompositeSubscription();
    }

    /**
     * Performs the passed {@code Action} asynchronously and notified the passed {@code Callback}
     * with the result.
     *
     * @param action   Action to be performed.
     * @param callback Callback after Action competes of fails to be performed due to an exception.
     * @param <T>      Type of the return of the action.
     */
    protected <T> void performActionAsync(final Action<T> action, final Callback<T> callback) {
        final Subscription subscription = Single.create(
                new Single.OnSubscribe<T>() {
                    @Override
                    public void call(SingleSubscriber<? super T> singleSubscriber) {
                        try {
                            singleSubscriber.onSuccess(action.run());
                        } catch (Throwable t) {
                            t.printStackTrace();
                            singleSubscriber.onError(t);
                        }
                    }
                }
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new SingleSubscriber<T>() {
                    @Override
                    public void onSuccess(T value) {
                        if (callback != null) {
                            callback.onSuccess(value);
                            callback.onFinish();
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                        if (callback != null) {
                            callback.onError(error);
                            callback.onFinish();
                        }
                    }
                }
        );
        mCompositeSubscription.add(subscription);
    }

    /**
     * Performs the passed {@code observable} asynchronously and notified the passed
     * {@code Callback} with the result.
     *
     * @param observable   RX observable to be observed.
     * @param callback Callback after Action competes of fails to be performed due to an exception.
     * @param <T>      Type of the return of the action.
     */
    protected <T> void performActionAsync(final Observable<T> observable,
                                          final Callback<T> callback) {
        if (observable != null) {
            Subscription subscription = observable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Subscriber<T>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    throwable.printStackTrace();
                                    if (callback != null) {
                                        callback.onError(throwable);
                                        callback.onFinish();
                                    }
                                }

                                @Override
                                public void onNext(T value) {
                                    if (callback != null) {
                                        callback.onSuccess(value);
                                        callback.onFinish();
                                    }
                                }
                            }
                    );
            mCompositeSubscription.add(subscription);
        }
    }

    protected void clearSubscriptions() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
