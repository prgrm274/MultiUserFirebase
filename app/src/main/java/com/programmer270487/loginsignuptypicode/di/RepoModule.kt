package com.programmer270487.loginsignuptypicode.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.programmer270487.loginsignuptypicode.data.repo.AuthRepo
import com.programmer270487.loginsignuptypicode.data.repo.AuthRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepoModule {
    @Provides
    @Singleton
    fun provideAuthRepo(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
    ): AuthRepo {
        return AuthRepoImpl(firebaseAuth,firestore,)
    }
}