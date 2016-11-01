package com.easyguide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.easyguide.data.repository.preferences.PreferencesLocalDataSource;
import com.easyguide.data.repository.preferences.PreferencesRepository;
import com.easyguide.data.repository.user.UserRemoteDataSource;
import com.easyguide.data.repository.user.UserRepository;
import com.easyguide.util.GoogleAuthenticationProvider;
import com.easyguide.util.schedulers.SchedulerProvider;
import com.google.firebase.auth.FirebaseAuth;

public class Injection {

    public static UserRepository provideUserRepository(@NonNull Context context) {
        return UserRepository.getInstance(
                new UserRemoteDataSource(
                        FirebaseAuth.getInstance(),
                        GoogleAuthenticationProvider.provideGoogleApiClient(context, context.getString(R.string.default_web_client_id))
                )
        );
    }

    public static PreferencesRepository providePreferencesRepository(@NonNull Context context) {
        return PreferencesRepository.getInstance(new PreferencesLocalDataSource(context));
    }

    public static SchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }

}
