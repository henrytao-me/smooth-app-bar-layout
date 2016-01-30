/*
 * Copyright 2015 "Henry Tao <hi@henrytao.me>"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.henrytao.smoothappbarlayoutdemo.activity;

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.henrytao.smoothappbarlayoutdemo.R;

/**
 * Created by henrytao on 10/11/15.
 */
public class BaseActivity extends AppCompatActivity {

  private static final String[] DONATE_ITEMS = {"donate_coffee", "donate_toast", "donate_pizza", "donate_dinner"};

  private static IInAppBillingService mBillingService;

  private static List<PurchaseItem> mPurchaseItems;

  private static ServiceConnection mServiceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      mBillingService = IInAppBillingService.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      mBillingService = null;
    }
  };

  private ProgressDialog mProgressDialog;

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1001) {
      int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
      String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
      String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
      if (resultCode == RESULT_OK) {
        new AlertDialog.Builder(BaseActivity.this)
            .setMessage(getString(R.string.text_thanks_for_donate))
            .setPositiveButton(R.string.close, null)
            .show();
      } else {
        showDonateDialogErrorCallback(null);
      }
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
    serviceIntent.setPackage("com.android.vending");
    bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mBillingService != null) {
      unbindService(mServiceConnection);
    }
    hideProgressDialog();
  }

  public void showDonateDialog() {
    if (mPurchaseItems == null) {
      showProgressDialog(getString(R.string.text_donate_progress_title), getString(R.string.text_donate_progress_message));
    }
    requestItemsForPurchase(new AsyncCallback<List<PurchaseItem>>(this) {
      @Override
      public void onError(Throwable throwable) {
        if (getContext() instanceof BaseActivity && !((BaseActivity) getContext()).isFinishing()) {
          BaseActivity context = (BaseActivity) getContext();
          context.hideProgressDialog();
          context.showDonateDialogErrorCallback(throwable);
        }
      }

      @Override
      public void onSuccess(List<PurchaseItem> data) {
        if (getContext() instanceof BaseActivity && !((BaseActivity) getContext()).isFinishing()) {
          BaseActivity context = (BaseActivity) getContext();
          context.hideProgressDialog();
          context.showDonateDialogSuccessCallback(data);
        }
      }
    });
  }

  private void hideProgressDialog() {
    if (mProgressDialog != null) {
      mProgressDialog.dismiss();
      mProgressDialog = null;
    }
  }

  private void onDonate(PurchaseItem item) {
    if (item == null || !item.isValid() || mBillingService == null) {
      return;
    }
    try {
      Bundle buyIntentBundle = mBillingService.getBuyIntent(3, getPackageName(), item.getId(), "inapp", "");
      PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
      startIntentSenderForResult(pendingIntent.getIntentSender(), 1001, new Intent(), 0, 0, 0);
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (IntentSender.SendIntentException e) {
      e.printStackTrace();
    }
  }

  private void requestItemsForPurchase(final AsyncCallback<List<PurchaseItem>> callback) {
    if (mPurchaseItems != null && mPurchaseItems.size() > 0) {
      if (callback != null) {
        callback.onSuccess(mPurchaseItems);
      }
      return;
    }
    new AsyncTask<IInAppBillingService, Void, AsyncResult<List<PurchaseItem>>>() {

      @Override
      protected AsyncResult<List<PurchaseItem>> doInBackground(IInAppBillingService... params) {
        List<PurchaseItem> result = new ArrayList<>();
        Throwable exception = null;
        IInAppBillingService billingService = params[0];

        if (billingService == null) {
          exception = new Exception("Unknow");
        } else {
          ArrayList<String> skuList = new ArrayList<>(Arrays.asList(DONATE_ITEMS));
          Bundle querySkus = new Bundle();
          querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
          try {
            Bundle skuDetails = billingService.getSkuDetails(3, getPackageName(), "inapp", querySkus);
            int response = skuDetails.getInt("RESPONSE_CODE");
            if (response == 0) {
              ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");
              PurchaseItem purchaseItem;
              for (String item : responseList) {
                purchaseItem = new PurchaseItem(new JSONObject(item));
                if (purchaseItem.isValid()) {
                  result.add(purchaseItem);
                }
              }
            }
          } catch (RemoteException e) {
            e.printStackTrace();
            exception = e;
          } catch (JSONException e) {
            e.printStackTrace();
            exception = e;
          }
        }
        return new AsyncResult<>(result, exception);
      }

      @Override
      protected void onPostExecute(AsyncResult<List<PurchaseItem>> result) {
        if (!isFinishing() && callback != null) {
          Throwable error = result.getError();
          if (error == null && (result.getResult() == null || result.getResult().size() == 0)) {
            error = new Exception("Unknow");
          }
          if (error != null) {
            callback.onError(error);
          } else {
            mPurchaseItems = result.getResult();
            Collections.sort(mPurchaseItems, new Comparator<PurchaseItem>() {
              @Override
              public int compare(PurchaseItem lhs, PurchaseItem rhs) {
                return (int) ((lhs.getPriceAmountMicros() - rhs.getPriceAmountMicros()) / 1000);
              }
            });
            callback.onSuccess(mPurchaseItems);
          }
        }
      }
    }.execute(mBillingService);
  }

  private void showDonateDialogErrorCallback(Throwable throwable) {
    new AlertDialog.Builder(BaseActivity.this)
        .setMessage(getString(R.string.text_donate_error))
        .setPositiveButton(R.string.close, null)
        .show();
  }

  private void showDonateDialogSuccessCallback(final List<PurchaseItem> data) {
    PurchaseAdapter adapter = new PurchaseAdapter(this, data);
    AlertDialog.Builder builder = new AlertDialog.Builder(this)
        .setTitle(R.string.text_donate)
        .setCancelable(false)
        .setAdapter(adapter, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            onDonate(data.get(which));
          }
        })
        .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            new AlertDialog.Builder(BaseActivity.this)
                .setMessage(getString(R.string.text_help_offer))
                .setPositiveButton(R.string.close, null)
                .show();
          }
        });
    builder.show();
  }

  private void showProgressDialog(CharSequence title, CharSequence message) {
    if (isFinishing()) {
      return;
    }
    hideProgressDialog();
    mProgressDialog = ProgressDialog.show(this, title, message);
  }

  public static abstract class AsyncCallback<T> {

    public abstract void onError(Throwable throwable);

    public abstract void onSuccess(T data);

    private WeakReference<Context> mContextWeakReference;

    public AsyncCallback(Context context) {
      mContextWeakReference = new WeakReference<>(context);
    }

    public Context getContext() {
      return mContextWeakReference.get();
    }
  }

  public static class AsyncResult<T> {

    private Throwable mError;

    private T mResult;

    public AsyncResult(T result, Throwable error) {
      mResult = result;
      mError = error;
    }

    public Throwable getError() {
      return mError;
    }

    public T getResult() {
      return mResult;
    }
  }

  public static class PurchaseAdapter extends ArrayAdapter<PurchaseItem> {

    @Bind(R.id.description)
    TextView vDescription;

    @Bind(R.id.title)
    TextView vTitle;

    private List<PurchaseItem> mItems;

    public PurchaseAdapter(Context context, List<PurchaseItem> items) {
      super(context, 0);
      mItems = items == null ? new ArrayList<PurchaseItem>() : items;
    }

    @Override
    public int getCount() {
      return mItems.size();
    }

    @Override
    public PurchaseItem getItem(int position) {
      return mItems.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase, parent, false);
      }
      ButterKnife.bind(this, convertView);
      vTitle.setText(getItem(position).getDescription());
      vDescription.setText(getItem(position).getPrice());
      return convertView;
    }
  }

  public static class PurchaseItem {

    private String mDescription;

    private String mId;

    private String mPrice;

    private long mPriceAmountMicros;

    private String mPriceCurrencyCode;

    private String mTitle;

    private String mType;

    public PurchaseItem(JSONObject item) {
      try {
        if (item != null) {
          mId = item.getString("productId");
          mType = item.getString("type");
          mTitle = item.getString("title");
          mDescription = item.getString("description");
          mPrice = item.getString("price");
          mPriceAmountMicros = item.getLong("price_amount_micros");
          mPriceCurrencyCode = item.getString("price_currency_code");
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    public String getDescription() {
      return mDescription;
    }

    public String getId() {
      return mId;
    }

    public String getPrice() {
      return mPrice;
    }

    public long getPriceAmountMicros() {
      return mPriceAmountMicros;
    }

    public String getPriceCurrencyCode() {
      return mPriceCurrencyCode;
    }

    public String getTitle() {
      return mTitle;
    }

    public String getType() {
      return mType;
    }

    public boolean isValid() {
      return !TextUtils.isEmpty(mId);
    }
  }
}
