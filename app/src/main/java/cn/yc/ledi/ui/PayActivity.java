package cn.yc.ledi.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.mob.paysdk.MobPayAPI;
import com.mob.paysdk.OnPayListener;
import com.mob.paysdk.PayResult;
import com.mob.paysdk.PaySDK;
import com.mob.paysdk.WXPayAPI;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.yc.ledi.R;
import cn.yc.ledi.base.BaseActivity;
import cn.yc.ledi.bean.PayCarOrder;
import cn.yc.ledi.utils.PreferencesUtils;

/**
 * Created by Administrator on 2018-03-04.
 */
@ContentView(R.layout.activity_pay)
public class PayActivity extends BaseActivity {
    @ViewInject(R.id.rg_money)
    RadioGroup rg_money;

    @ViewInject(R.id.btn_topay)
    Button btn_topay;

    int money = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initView(this);
    }


    @Override
    protected void initView(Context mContext) {
        super.initView(mContext);

        update_payButton(money);


        rg_money.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                switch (checkedId) {
                    case R.id.rb_money_1:
                        money = 50;
                        break;
                    case R.id.rb_money_2:
                        money = 100;
                        break;
                    case R.id.rb_money_3:
                        money = 200;
                        break;
                    case R.id.rb_money_4:
                        money = 300;
                        break;

                }
                update_payButton(money);
            }
        });


        btn_topay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPay();
            }
        });
    }

    private void startPay() {
        dialogManage.loading();

        String orderNo = "ZXCZ-" + PreferencesUtils.getInstance().getCurrentCarInfo().getCar_id() + "-" + System.currentTimeMillis();
        final PayCarOrder order = new PayCarOrder();
        order.setCar_id(PreferencesUtils.getInstance().getCurrentCarInfo().getCar_id());
        order.setOrderNo(orderNo);
        order.setAmount(PreferencesUtils.getInstance().getCurrentCarInfo().getCar_id().equals("1") ? 1 : money * 100);
        order.setSubject("在线充值");
        order.setBody(PreferencesUtils.getInstance().getCurrentCarInfo().getCarname() + "在线支付");
        final MobPayAPI payApi = PaySDK.createMobPayAPI(WXPayAPI.class);
        payApi.pay(order, new OnPayListener<PayCarOrder>() {
            @Override
            public boolean onWillPay(String s, PayCarOrder payOrder, MobPayAPI mobPayAPI) {
                return false;
            }

            @Override
            public void onPayEnd(PayResult payResult, PayCarOrder payOrder, MobPayAPI mobPayAPI) {

                if (payResult == PayResult.PAYRESULT_OK) {

                    dialogManage.alert("支付成功！"+payOrder.getCar_id(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                } else {
                    dialogManage.alert("支付失败！");
                }

            }
        });


    }

    private void update_payButton(int text) {

        btn_topay.setText("确认支付 ￥" + text + "元");
    }


}
