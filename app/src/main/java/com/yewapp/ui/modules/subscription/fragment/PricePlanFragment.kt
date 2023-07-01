package com.yewapp.ui.modules.subscription.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode.OK
import com.google.common.collect.ImmutableList
import com.yewapp.R
import com.yewapp.data.network.api.subscription.PlanDetails
import com.yewapp.databinding.PricePlanFragmentBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.subscription.adapter.PriceAndPlaneAdapter
import com.yewapp.ui.modules.subscription.extras.GoogleBillingSignature
import com.yewapp.ui.modules.subscription.navigator.PricePlanNavigator
import com.yewapp.ui.modules.subscription.vm.PricePlanFragmentViewModel
import java.io.IOException


class PricePlanFragment(override val layoutId: Int = R.layout.price_plan_fragment) :
    BaseFragment<PricePlanNavigator, PricePlanFragmentViewModel, PricePlanFragmentBinding>(),
    PricePlanNavigator {
    private var isSuccess = false
    private lateinit var billingClient: BillingClient
    private lateinit var priceAndPlaneAdapter: PriceAndPlaneAdapter
    override fun onViewModelCreated(viewModel: PricePlanFragmentViewModel) {
        viewModel.setNavigator(this)

    }
    override fun onViewBound(viewDataBinding: PricePlanFragmentBinding) {
        viewModel?.getSubscriptionPlansList()
        adapterInitialize()
        val purchasesUpdatedListener =
            PurchasesUpdatedListener { billingResult, purchases ->
                if (billingResult.responseCode == OK && purchases != null) {
                    for (purchase in purchases) {
                        handlePurchase(purchase)
                    }
                } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                    Log.d("TAG", "Item already owned")
                    isSuccess = true

                } else if (billingResult.responseCode == BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED) {
                    Log.d("TAG", "Purchase feature not supported")
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Something went wrong please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

             billingClient = BillingClient.newBuilder(requireContext())
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

    }
    override fun onStart() {
        super.onStart()
        addObserver()
    }

    override fun navigateToBuySubscription() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {

            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                startPurchase()
            }

        })
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bind(PricePlanFragmentViewModel::class.java, inflater, container)
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.subscriptionPlans.observe(this, Observer {
            priceAndPlaneAdapter.setItems(it.list)
        })
    }

    private fun adapterInitialize() {
        val list = mutableListOf<PlanDetails>()
        priceAndPlaneAdapter = PriceAndPlaneAdapter(list)

        viewDataBinding.recylerViewChoosePlan.adapter = priceAndPlaneAdapter.apply {
            setHasStableIds(true)
        }
        viewDataBinding.recylerViewChoosePlan.isNestedScrollingEnabled = true
    }

    private var acknowledgePurchaseResponseListener =
        AcknowledgePurchaseResponseListener { billingResult ->
            if (billingResult.responseCode == OK) {
                isSuccess = true

            }
        }
private fun handlePurchase(purchase: Purchase) {
    val consumeParams = ConsumeParams.newBuilder()
        .setPurchaseToken(purchase.purchaseToken)
        .build()
    val listener = ConsumeResponseListener { billingResult, s ->
        if (billingResult.responseCode == OK) {

        }
    }
    billingClient.consumeAsync(consumeParams, listener)
    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
        if (!verifyValidSignature(purchase.originalJson, purchase.signature)) {
            Log.d("TAG", "Invalid Purchase")
        }
        if (!purchase.isAcknowledged) {
            val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            billingClient.acknowledgePurchase(
                acknowledgePurchaseParams,
                acknowledgePurchaseResponseListener
            )
            isSuccess = true
            Log.d("ItemPurchased",purchase.originalJson)
        } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
            Log.d("TAG", "Pending Purchase")

        } else if (purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE) {
            Log.d("TAG", "Unspecified state Purchase")

        }

    }
}
    private fun verifyValidSignature(singedData: String, signature: String): Boolean {
        return try {
            val base64Key =
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAi2+73UW4+rBIzwnPnykm7mdBRmelSoZtpBoVOSCyLGrmFba3myFD1+T97/2QvVeXF4pkdkpBK5YDreZ64gL7+ao/kNj9HCt/uV/BdoA5HzJnSzcsCbErde3uREPafmpc1ZcBVWf65JjgkaeTc6VTKh7PBKW7s3GW98weGsqDeCxzx0sCLmtJYRbzVksAYGsUFA6jDLWCcqtJcIk/SczYo4g4zgzhzD/92P5qrm4RvLgOLqYCW1WYHl29Mv1ODlzL5q3WiK16hz3fGBYwsf1+hb6EZEXuiknjTJwqXQzi26yrNI5TpZAl4t/suyhxeQEBhksiN40vZgIewICJ8+wpOQIDAQAB"
            val security = GoogleBillingSignature()
            security.verifyPurchase(base64Key, singedData, signature)
        } catch (e: IOException) {
            false
        }
    }
    private fun startPurchase() {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("yew_monthly_sub")
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
        billingClient.queryProductDetailsAsync(params.build()) { billingResult, productDetailsList ->

            for (product in productDetailsList) {
                val offerToken = product.subscriptionOfferDetails?.get(0)?.offerToken
                val productDetailsParamsList = listOf(
                    offerToken?.let {
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(product)
                            .setOfferToken(it)
                            .build()
                    }
                )
                val billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productDetailsParamsList)
                    .build()
                val billingResult = billingClient.launchBillingFlow(requireActivity(), billingFlowParams)
            }
            Log.d("TAG", productDetailsList.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (billingClient != null){
            billingClient.endConnection()
        }
    }

    override fun onBackPress() {}
}
