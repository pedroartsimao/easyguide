package com.easyguide.data.repository.user;

import android.support.annotation.NonNull;

import com.easyguide.data.entity.UserEntity;
import com.easyguide.data.entity.mapper.UserEntityMapper;
import com.easyguide.util.rxfirebase.RxFirebaseHandler;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import static com.google.common.base.Preconditions.checkNotNull;

public class UserRemoteDataSource implements UserDataSource {

    private final FirebaseAuth firebaseAuth;

    public UserRemoteDataSource(@NonNull FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public Observable<UserEntity> getUser() {
        return Observable.fromCallable(new Callable<UserEntity>() {
            @Override
            public UserEntity call() throws Exception {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    return UserEntityMapper.transform(firebaseUser);
                } else {
                    return null;
                }
            }
        });
    }

    @Override
    public Observable<Boolean> persistUser(@NonNull UserEntity user) {
        checkNotNull(user);
        final AuthCredential credential = GoogleAuthProvider.getCredential(user.getIdToken(), null);
        return Observable.create(new Observable.OnSubscribe<AuthResult>() {
            @Override
            public void call(final Subscriber<? super AuthResult> subscriber) {
                RxFirebaseHandler.assignOnTask(subscriber, firebaseAuth.signInWithCredential(credential));
            }
        }).map(new Func1<AuthResult, Boolean>() {
            @Override
            public Boolean call(AuthResult authResult) {
                return authResult.getUser() != null;
            }
        });
    }
}
