package com.tianhe.pay.di.module;

import com.tianhe.pay.data.UseCase;
import com.tianhe.pay.data.crm.coupon.QueryCouponUseCase;
import com.tianhe.pay.data.crm.member.QueryMemberUseCase;
import com.tianhe.pay.data.crm.storedvaluecard.QueryStoredValueCardUseCase;
import com.tianhe.pay.di.PerFragment;
import com.tianhe.pay.ui.crm.coupon.QueryCouponContract;
import com.tianhe.pay.ui.crm.coupon.QueryCouponPresenter;
import com.tianhe.pay.ui.crm.member.QueryMemberContract;
import com.tianhe.pay.ui.crm.member.QueryMemberPresenter;
import com.tianhe.pay.ui.crm.storedvaluecard.QueryStoredValueCardContract;
import com.tianhe.pay.ui.crm.storedvaluecard.QueryStoredValueCardPresenter;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

public interface CrmModule {
//    @PerFragment
//    @ContributesAndroidInjector
//    abstract QueryMemberFragment queryMemberFragment();
//
//    @PerFragment
//    @ContributesAndroidInjector
//    abstract QueryStoredValueCardFragment queryStoredValueCardFragment();
//
//    @PerFragment
//    @ContributesAndroidInjector
//    abstract QueryCouponFragment queryCouponFragment();

    @Module
    abstract class Member {
        @PerFragment
        @Binds
        abstract QueryMemberContract.Presenter queryMemberPresenter(QueryMemberPresenter presenter);

        @Provides
        @PerFragment
        @Named("queryMember")
        static UseCase providerQueryMember(QueryMemberUseCase task) {
            return task;
        }

    }

    @Module
    abstract class Coupon {
        @PerFragment
        @Binds
        abstract QueryCouponContract.Presenter queryCouponPresenter(QueryCouponPresenter presenter);

        @Provides
        @PerFragment
        @Named("queryCoupon")
        static UseCase providerQueryCoupon(QueryCouponUseCase task) {
            return task;
        }

    }

    @Module
    abstract class StoredValueCard {
        @PerFragment
        @Binds
        abstract QueryStoredValueCardContract.Presenter queryStoredValueCardPresenter(QueryStoredValueCardPresenter presenter);

        @Provides
        @PerFragment
        @Named("queryStoredValue")
        static UseCase providerQueryStoredValue(QueryStoredValueCardUseCase task) {
            return task;
        }
    }
}
